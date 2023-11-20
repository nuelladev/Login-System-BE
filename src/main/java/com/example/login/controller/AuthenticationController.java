package com.example.login.controller;

import com.example.login.requests.AuthenticationRequests;
import com.example.login.requests.AuthenticationResponse;
import com.example.login.service.AuthenticationService;
import com.example.login.service.EmailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequests authenticationRequest) {
        AuthenticationResponse response;
        try {
            response = authenticationService.authenticate(authenticationRequest);
            // Consider the situation where authentication succeeds but the email service fails.
            try {
                emailService.sendLoginAlert(authenticationRequest.getUsername());
            } catch (Exception e) {
                // Log the exception and decide how you want to handle it. For example, you may choose to ignore it
                // and still return a successful response, or you might return a different status code.
                log.error("Failed to send login alert email", e);
            }
            // Respond with the authentication response even if the email sending fails.
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            log.error("Authentication failed due to bad credentials", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        } catch (Exception e) {
            log.error("Authentication failed due to an unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication process failed");
        }
    }
}
