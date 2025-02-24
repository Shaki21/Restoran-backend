package com.example.restoran.restoranbackend.controllers;

import com.example.restoran.restoranbackend.model.User;
import com.example.restoran.restoranbackend.repository.UserRepository;
import com.example.restoran.restoranbackend.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthController(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  static class LoginRequest {
    public String username;
    public String password;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<User> userOptional = userRepository.findByUsername(request.username);

    if (!userOptional.isPresent()) {
      return ResponseEntity.status(401).body("User not found");
    }

    User user = userOptional.get();
    if (!passwordEncoder.matches(request.password, user.getPassword())) {
      return ResponseEntity.status(401).body("Invalid password");
    }

    String token = jwtService.generateToken(request.username);
    return ResponseEntity.ok(token);
  }
}