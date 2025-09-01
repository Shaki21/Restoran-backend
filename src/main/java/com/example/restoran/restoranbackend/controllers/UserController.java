package com.example.restoran.restoranbackend.controllers;

import com.example.restoran.restoranbackend.services.UserService;
import com.example.restoran.restoranbackend.model.Role;
import com.example.restoran.restoranbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody User user) {
    try {
      if (user.getUsername() == null || user.getPassword() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password are required");
      }
      // Default role and other fields are optional in this step
      if (user.getRole() == null) {
        user.setRole(Role.USER);
      }
      User createdUser = userService.createUser(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    } catch (DataIntegrityViolationException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
    }
  }

  @GetMapping
  public ResponseEntity<?> getAllUsers() {
    try {
      return ResponseEntity.ok(userService.getAllUsers());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getUserById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(userService.getUserById(id));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @GetMapping("/username/{username}")
  public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
    try {
      User user = userService.getUserByUsername(username);
      return ResponseEntity.ok(user);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @GetMapping("/current")
  public ResponseEntity<?> getCurrentUser(Authentication authentication) {
    String username = authentication.getName();

    try {
      User user = userService.getUserByUsername(username);

      user.setPassword(null);

      return ResponseEntity.ok(user);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @PutMapping("/{username}")
  public ResponseEntity<?> updateUserInfo(@PathVariable String username, @RequestBody User updatedUserInfo) {
    try {
      User existingUser = userService.getUserByUsername(username);

      // Update only the fields that are not null
      existingUser.setFirstname(updatedUserInfo.getFirstname());
      existingUser.setLastname(updatedUserInfo.getLastname());
      existingUser.setPosition(updatedUserInfo.getPosition());

      // Update the rest of the fields
      existingUser = userService.updateUserInfo(existingUser);

      return ResponseEntity.ok(existingUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @PatchMapping("/update/{username}")
  public ResponseEntity<?> updateUserName(@PathVariable String username, @RequestBody User updatedUser) {
    try {
      User existingUser = userService.getUserByUsername(username);

      if (updatedUser.getFirstname() != null) {
        existingUser.setFirstname(updatedUser.getFirstname());
      }
      if (updatedUser.getLastname() != null) {
        existingUser.setLastname(updatedUser.getLastname());
      }

      existingUser = userService.updateUserInfo(existingUser);

      return ResponseEntity.ok(existingUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    try {
      userService.deleteUserById(id);
      return ResponseEntity.ok("User deleted");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }
}
