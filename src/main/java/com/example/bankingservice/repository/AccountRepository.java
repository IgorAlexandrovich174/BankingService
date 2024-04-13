package com.example.bankingservice.repository;

import com.example.bankingservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query(value = """
            SELECT * FROM accounts WHERE accounts.user_id = (
                               select users.id from users where login = :login) FOR UPDATE""",
            nativeQuery = true)
    Optional<Account> findAccountByLogin(String login);

    Optional<Account> findAccountById(Integer userId);
}
