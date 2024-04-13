package com.example.bankingservice.filter;

import com.example.bankingservice.repository.UserRepository;
import com.example.bankingservice.util.SecurityContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    private final SecretKey secretKey;
    private final UserRepository userRepository;

    public AuthorizationFilter(UserRepository userRepository, SecretKey secretKey) {
        this.userRepository = userRepository;
        this.secretKey = secretKey;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String header = request.getHeader("authorization");
        if (header == null || !header.toLowerCase().startsWith("bearer ")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("incorrect authorization header");
            return;
        }
        String token = header.substring(7);
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build().parseUnsecuredClaims(token)
                    .getPayload();
        } catch (JwtException exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("invalid token");
        }
        Integer userId = claims.get("id",Integer.class);
        SecurityContext.setAuthenticatedUserId(userId);
//        String base64Credentials = header.substring(6);
//        String decodedCredentials = new String(Base64.getDecoder().decode(token));
//        String login = decodedCredentials.split(":")[0];
//        String passwordHash = DigestUtils.md5DigestAsHex(decodedCredentials.split(":")[1].getBytes(StandardCharsets.UTF_8));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
