package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String adminEmail;

    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text)
            throws MessagingException, MailSendException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");

            helper.setFrom(adminEmail, "ĐOÀN HUY ECOMMERCE");
            helper.setSubject(subject);
            helper.setText(text+otp, true);
            helper.setTo(userEmail);
            javaMailSender.send(mimeMessage);
        } catch (MailException | UnsupportedEncodingException e) {
            throw new MailSendException("Failed to send email");
        }
    }

    @Override
    public void notification(String email, String subject, String text) throws MessagingException, MailSendException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");

            helper.setFrom(adminEmail, "ĐOÀN HUY ECOMMERCE");
            helper.setSubject(subject);
            helper.setText(text, true);
            helper.setTo(email);
            javaMailSender.send(mimeMessage);
        } catch (MailException | UnsupportedEncodingException e) {
            throw new MailSendException("Failed to send email");
        }
    }
}
