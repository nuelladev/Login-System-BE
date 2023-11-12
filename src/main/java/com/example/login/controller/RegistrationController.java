package com.example.login.controller;


import com.example.login.model.User;
import com.example.login.repository.UserRepository;
import com.example.login.service.CustomUserDetailsService;
import com.example.login.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Check if user already exists
        if (userRepository.findByUsername(user.getUserName()) != null) {
            return ResponseEntity.badRequest().body("User with username already exists.");
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));


        User savedUser = customUserDetailsService.save(user);


        String subject = "Welcome to Our App!";
        String text = "Hi " + user.getUserName() + ",\n\nWelcome to Ella's Login System. We are glad you joined us!";
        emailService.sendMail(user.getEmail(), subject, text);


        return ResponseEntity.ok("User registered successfully!");
    }
}

