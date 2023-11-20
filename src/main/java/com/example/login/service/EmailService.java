package com.example.login.service;

import com.example.login.model.User;
import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("oemmanuella559@gmail.com"); // replace with your desired "from" address
            mailSender.send(message);
        } catch (MailException e) {
            // Log the exception instead of throwing it, so it doesn't interrupt your flow
            // Consider using a logger to log this exception
            e.printStackTrace();
        }
    }

    public void sendRegistrationConfirmation(String email, String jwtToken) {
        String subject = "Confirm Your Registration";
        String text = String.format(
                "Dear user,\n\n" +
                        "Thank you for registering with us. Please confirm your email address to complete the registration process.\n\n" +
                        "Click on the link below or copy it into your browser to confirm your email address:\n" +
                        "http://localhost:8080/confirm?token=%s\n\n" +
                        "If you did not register for our service, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "The Team",
                jwtToken
        );
        sendMail(email, subject, text);
    }


    public void sendLoginAlert(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found with username: " + username));
        String subject = "New Login Detected";
        String text = String.format(
                "Dear %s,\n\n" +
                        "We detected a new login to your account. If this was you, you can safely ignore this message. If you did not log in, please contact our support team immediately.\n\n" +
                        "Best regards,\n" +
                        "Security Team",
                user.getUsername()
        );
        sendMail(user.getEmail(), subject, text);
    }

}
