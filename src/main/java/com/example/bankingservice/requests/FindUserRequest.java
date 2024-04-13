package com.example.bankingservice.requests;

import java.time.LocalDate;

public record FindUserRequest(LocalDate dateOfBirth, String phoneNumber, String name, String mail) {
}
