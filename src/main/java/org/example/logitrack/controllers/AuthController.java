package org.example.logitrack.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.logitrack.entities.dto.auth.LoginRequestDTO;
import org.example.logitrack.entities.dto.auth.LoginResponseDTO;
import org.example.logitrack.entities.dto.user.UserDTO;
import org.example.logitrack.entities.models.User;
import org.example.logitrack.mappers.UserMapper;
import org.example.logitrack.security.JwtUtil;
import org.example.logitrack.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        User user = userService.findActiveByLogin(request.login())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getLogin(), user.getRole().name());
        UserDTO userDTO = userMapper.toDto(user);

        return ResponseEntity.ok(new LoginResponseDTO(token, userDTO));
    }
}