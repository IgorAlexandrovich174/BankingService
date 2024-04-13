package com.example.bankingservice.dto;

import java.time.LocalDate;
import java.util.List;


public record UserDto(Integer id, String name, LocalDate dateOfBirth, List<String> mails,
                      List<String> phoneNumbers) {
}
