package com.example.bankingservice.repository;

import com.example.bankingservice.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Integer> {
    boolean existsByNumberIn(List<String> numbers);
    boolean existsByNumberInAndUserIdNot(List<String> numbers, Integer userId);
    void deleteByUserId(Integer userId);


}
