package com.example.bankingservice.repository;

import com.example.bankingservice.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<Mail, Integer> {
    boolean existsByMailIn(List<String> mail);
    boolean existsByMailInAndUserIdNot(List<String> mail, Integer userId);
    void deleteByUserId(Integer userId);

}
