package com.example.bankingservice.service;

import com.example.bankingservice.entity.Account;
import com.example.bankingservice.entity.Mail;
import com.example.bankingservice.entity.PhoneNumber;
import com.example.bankingservice.entity.User;
import com.example.bankingservice.exception.InsufficientBalanceException;
import com.example.bankingservice.exception.ValidationException;
import com.example.bankingservice.repository.AccountRepository;
import com.example.bankingservice.repository.MailRepository;
import com.example.bankingservice.repository.PhoneNumberRepository;
import com.example.bankingservice.repository.UserRepository;
import com.example.bankingservice.requests.AddUserRequest;
import com.example.bankingservice.requests.FindUserRequest;
import com.example.bankingservice.requests.UpdateUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class UserService {
    private final AccountRepository accountRepository;
    private final MailRepository mailRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final UserRepository userRepository;


    public UserService(
            AccountRepository accountRepository,
            MailRepository mailRepository,
            PhoneNumberRepository phoneNumberRepository,
            UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.mailRepository = mailRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addUser(AddUserRequest addUserRequest) throws ValidationException {
            if (userRepository.existsByLogin(addUserRequest.login())) {
                throw new ValidationException("login is busy");
            }
            if (addUserRequest.balance().compareTo(ZERO) < 0) {
                throw new ValidationException("Negative balance is not possible");
            }
            Account account = new Account();
            account.setBalance(addUserRequest.balance());
            account.setInitialBalance(addUserRequest.balance());
            accountRepository.save(account);

            User user = new User();
            user.setLogin(addUserRequest.login());
            user.setPasswordHash(DigestUtils.md5DigestAsHex(addUserRequest.password().getBytes()));
            user.setName(addUserRequest.name());
            user.setDateOfBirth(addUserRequest.dateOfBirth());
            user.setAccountId(account.getId());
            user.setPhoneNumbers(transformNumbers(addUserRequest.phoneNumbers()));
            user.setMails(saveMails(addUserRequest.mails()));

            userRepository.save(user);
            account.setUserId(user.getId());
    }

    //todo
    @Transactional
    public User change(Integer userId, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user not found"));
        if (updateUserRequest.phoneNumbers() != null) {
            updatePhoneNumbers(user, updateUserRequest);
        }
        if (updateUserRequest.mails() != null) {
            updateMails(user, updateUserRequest);
        }
        return user;
    }

    private List<PhoneNumber> transformNumbers(List<String> numbers) {
        if (phoneNumberRepository.existsByNumberIn(numbers)) {
            throw new ValidationException("Number is busy");
        }
        List<PhoneNumber> listNumbers = new ArrayList<>();
        for (String number : numbers) {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setNumber(number);
            listNumbers.add(phoneNumber);
        }
        return listNumbers;
    }

    private List<Mail> saveMails(List<String> mails) {
        if (mailRepository.existsByMailIn(mails)) {
            throw new ValidationException("mail is busy");
        }
        List<Mail> listMails = new ArrayList<>();
        for (String inputMail : mails) {
            Mail mail = new Mail();
            mail.setMail(inputMail);
            listMails.add(mail);
        }
//        mailRepository.saveAll(listMails);
        return listMails;
    }

    public void updateMails(User user, UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.mails().isEmpty()) {
            throw new ValidationException("input mails is empty");
        }
        if (mailRepository.existsByMailInAndUserIdNot(updateUserRequest.mails(), user.getId())) {
            throw new ValidationException("mail not vacant");
        }
        mailRepository.deleteByUserId(user.getId());
        List<Mail> resultMails = updateUserRequest.mails().stream()
                .map(mail -> {
                    Mail mail1 = new Mail();
                    mail1.setMail(mail);
                    return mail1;
                }).toList();
        user.setMails(resultMails);
//        mailRepository.saveAll(resultMails);
    }

    public void updatePhoneNumbers(User user, UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.phoneNumbers().isEmpty()) {
            throw new ValidationException("input phones is empty");
        }
        if (phoneNumberRepository.existsByNumberInAndUserIdNot(updateUserRequest.phoneNumbers(), user.getId())) {
            throw new ValidationException("phone number not vacant");
        }
        phoneNumberRepository.deleteByUserId(user.getId());
        List<PhoneNumber> resultPhonesNumbers = updateUserRequest.phoneNumbers().stream()
                .map(phoneNumbers -> {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setNumber(phoneNumbers);
                    return phoneNumber;
                }).toList();
        user.setPhoneNumbers(resultPhonesNumbers);

    }

    @Transactional
    public void transferMoney(Integer fromUserId, String toLogin, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException {
        Account fromAccount = accountRepository.findAccountById(fromUserId)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));
        Account toAccount = accountRepository.findAccountByLogin(toLogin)
                .orElseThrow(() -> new AccountNotFoundException("Recipient account not found"));
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("incorrect amount");
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void increaseBalance() {
        userRepository.increaseBalance();
    }

    public List<User> findUser(FindUserRequest findUserRequest, PageRequest pageRequest) {
        Page<User> pageUsers = userRepository.findAll(
                where(dateOfBirthFrom(findUserRequest.dateOfBirth())
                        .and(hasPhoneNumber(findUserRequest.phoneNumber()))
                        .and(hasName(findUserRequest.name()))
                        .and(hasMail(findUserRequest.mail()))), pageRequest);
        return pageUsers.getContent();
    }


    Specification<User> dateOfBirthFrom(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return Specification.where(null);
        }
        return (user, cq, cb) -> cb.greaterThan(user.get("dateOfBirth"), dateOfBirth);
    }

    Specification<User> hasPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return Specification.where(null);
        }
        return (user, cq, cb) -> cb.equal(user.get("phoneNumbers").get("number"), phoneNumber);
    }

    Specification<User> hasName(String name) {
        if (name == null) {
            return Specification.where(null);
        }
        return (user, cq, cb) -> cb.like(user.get("name"), name + "%");
    }

    Specification<User> hasMail(String mail) {
        if (mail == null) {
            return Specification.where(null);
        }
        return (user, cq, cb) -> cb.equal(user.get("mails").get("mail"), mail);
    }
}

