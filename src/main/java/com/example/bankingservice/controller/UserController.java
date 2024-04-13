package com.example.bankingservice.controller;

import com.example.bankingservice.dto.UserDto;
import com.example.bankingservice.entity.User;
import com.example.bankingservice.exception.InsufficientBalanceException;
import com.example.bankingservice.mappers.UserMapper;
import com.example.bankingservice.repository.UserRepository;
import com.example.bankingservice.requests.AuthRequest;
import com.example.bankingservice.requests.FindUserRequest;
import com.example.bankingservice.requests.UpdateUserRequest;
import com.example.bankingservice.service.UserService;
import com.example.bankingservice.util.SecurityContext;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {
//        @Value("${jwt.secret}")
//    private String jwtSecret;
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PutMapping("/transfer-money/{toLogin}/{amount}")
    public ResponseEntity<String> transferMoney(@PathVariable String toLogin, @PathVariable BigDecimal amount) throws
            InsufficientBalanceException,
            AccountNotFoundException {
        userService.transferMoney(SecurityContext.getAuthenticatedUserId(), toLogin, amount);
        return ResponseEntity.ok("Money transferred");
    }

    @PutMapping("/change/{userId}")
    public User change(@PathVariable Integer userId, @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.change(userId, updateUserRequest);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        String passwordHash = DigestUtils.md5DigestAsHex(authRequest.password().getBytes(StandardCharsets.UTF_8));
        Optional<User> user = userRepository.findByLoginAndPasswordHash(authRequest.login(), passwordHash);
        if (user.isPresent()) {
            JwtBuilder jwtBuilder = Jwts.builder()
                    .subject(authRequest.login())
                    .expiration(accessExpiration)
                    .signWith(Keys.hmacShaKeyFor("123123123123123123123123123123123123123123123123".getBytes()));
            jwtBuilder.claim("id", user.get().getId());
            return ResponseEntity.ok(
                    jwtBuilder
                            .compact());
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping()
    public List<UserDto> search(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = "10") int size,
                                @RequestParam(required = false, defaultValue = "name") String sortBy,
                                FindUserRequest request
    ) {
        return UserMapper.INSTANCE.toDto(userService.findUser(request, PageRequest.of(page, size, Sort.by(sortBy))));
    }

}
