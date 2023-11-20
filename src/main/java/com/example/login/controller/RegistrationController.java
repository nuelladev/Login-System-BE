package com.example.login.controller;

import com.example.login.requests.AuthenticationResponse;
import com.example.login.requests.RegisterRequests;
import com.example.login.service.AuthenticationService;
import com.example.login.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequests registerRequest) {
        try {
            // The register method returns an AuthenticationResponse object
            AuthenticationResponse authResponse = authenticationService.register(registerRequest);

            // Assuming your email service has a method to send a registration confirmation
            emailService.sendRegistrationConfirmation(registerRequest.getEmail(), authResponse.getJwtToken());

            return ResponseEntity.ok("User registered successfully. Please check your email to confirm your account. Verify your email address: " );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
