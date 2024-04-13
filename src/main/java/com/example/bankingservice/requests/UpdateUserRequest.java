package com.example.bankingservice.requests;

import java.util.List;

public record UpdateUserRequest(List<String> phoneNumbers, List<String> mails) {

}
