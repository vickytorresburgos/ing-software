package edu.um.umbook.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void enviarEmailBienvenida(String email) {
        // Simulating SMTP server interaction
        System.out.println("250 OK: Email sent to " + email);
        System.out.println("Message: Welcome to UMBook! Your account is now active.");
    }
}
