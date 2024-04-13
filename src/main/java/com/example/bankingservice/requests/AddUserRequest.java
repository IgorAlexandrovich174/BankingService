package com.example.bankingservice.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AddUserRequest(String login, String password, String name,
                             LocalDate dateOfBirth, List<String> phoneNumbers, List<String> mails, BigDecimal balance) {
}
