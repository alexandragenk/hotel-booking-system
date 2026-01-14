package com.example.booking.controller;

import com.example.booking.dto.AuthResponse;
import com.example.booking.entity.User;
import com.example.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<AuthResponse> createAdmin(@RequestParam String username, @RequestParam String password) {
        String token = userService.registerUser(username, password, User.Role.ADMIN);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateRole(@PathVariable Long id, @RequestParam User.Role role) {
        return ResponseEntity.ok(userService.updateUserRole(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> all() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
