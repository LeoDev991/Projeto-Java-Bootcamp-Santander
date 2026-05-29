package com.portfolio.commerce.service;

import com.portfolio.commerce.dto.AuthRequest;
import com.portfolio.commerce.dto.AuthResponse;
import com.portfolio.commerce.entity.User;
import com.portfolio.commerce.exception.ResourceNotFoundException;
import com.portfolio.commerce.repository.UserRepository;
import com.portfolio.commerce.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("User authenticated email={}", request.email());
        return new AuthResponse(jwtService.generate(user.getEmail(), user.getRoles()), "Bearer");
    }
}
