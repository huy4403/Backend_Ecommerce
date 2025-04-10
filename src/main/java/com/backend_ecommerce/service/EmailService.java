package com.backend_ecommerce.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.MailSendException;

public interface EmailService {

    void sendVerificationOtpEmail(String userEmail,
                                  String otp,
                                  String subject,
                                  String text)
            throws MessagingException, MailSendException;

    void notification(String email, String subject, String text) throws MessagingException, MailSendException;
}
