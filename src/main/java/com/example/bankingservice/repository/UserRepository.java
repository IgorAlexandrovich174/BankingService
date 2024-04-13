package com.example.bankingservice.repository;

import com.example.bankingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    boolean existsByLogin(String login);
    Optional<User> findByLoginAndPasswordHash(String login, String passwordHash);
    @Modifying
    @Query(value = "UPDATE accounts SET balance = " +
           "CASE WHEN balance * 1.05 <= initial_balance * 2.07 " +
           "THEN balance * 1.05 ELSE initial_balance * 2.07 END", nativeQuery = true)
    void increaseBalance();

    boolean existsUserByLoginAndPasswordHash(String login, String passwordHash);
}
