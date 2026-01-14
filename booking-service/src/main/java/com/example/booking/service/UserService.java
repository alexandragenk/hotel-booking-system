package com.example.booking.service;

import com.example.booking.entity.User;
import com.example.booking.repository.UserRepository;
import com.example.booking.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String registerUser(String username, String rawPassword, User.Role role) {
        User user = new User(username, passwordEncoder.encode(rawPassword), role);
        user = userRepository.save(user);
        return jwtService.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    public String authenticate(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return jwtService.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUserRole(Long id, User.Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(role);
        return userRepository.save(user);
    }
}
