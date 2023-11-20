package com.example.login.service;

import com.example.login.model.Role;
import com.example.login.model.User;
import com.example.login.repository.UserRepository;
import com.example.login.requests.AuthenticationRequests;
import com.example.login.requests.AuthenticationResponse;
import com.example.login.requests.RegisterRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 JwtService jwtService,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public AuthenticationResponse register(RegisterRequests request) {
        // Check if user already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        // Create new user with encoded password and default role
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.ROLE_USER); // Adjust according to your Role setup

        // Save the new user to the repository
        User savedUser = userRepository.save(newUser);

        // Send a welcome email to the new user
        emailService.sendMail(newUser.getEmail(), "Welcome to Our Platform!",
                "Hello " + newUser.getUsername() + "! Welcome to our service. We are glad to have you join us!");

        // Generate token for the new user
        String token = jwtService.generateToken(savedUser);

        // Return authentication response
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequests request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Generate token for the authenticated user
        String token = generateToken((User)authentication.getPrincipal());

        // Return authentication response
        return new AuthenticationResponse(token);
    }

    // Helper method to generate token based on the User object
    private String generateToken(User user) {
        return jwtService.generateToken(user);
    }
}
