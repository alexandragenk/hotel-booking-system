package com.example.booking.controller;

import com.example.booking.dto.AuthRequest;
import com.example.booking.dto.AuthResponse;
import com.example.booking.entity.User;
import com.example.booking.security.JwtService;
import com.example.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        String token = userService.registerUser(request.getUsername(), request.getPassword(), User.Role.USER);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest request) {
        String token = userService.authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
