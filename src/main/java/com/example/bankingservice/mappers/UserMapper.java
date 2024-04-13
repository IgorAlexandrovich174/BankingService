package com.example.bankingservice.mappers;

import com.example.bankingservice.dto.UserDto;
import com.example.bankingservice.entity.Mail;
import com.example.bankingservice.entity.PhoneNumber;
import com.example.bankingservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<UserDto> toDto(List<User> user);

    default List<String> mailToString(List<Mail> value) {
        return value.stream()
                .map(Mail::getMail)
                .toList();
    }
    default List<String> phoneToString(List<PhoneNumber> value) {
        return value.stream()
                .map(PhoneNumber::getNumber)
                .toList();
    }
}
