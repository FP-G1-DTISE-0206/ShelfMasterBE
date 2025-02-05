package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.usecase.auth.SendEmailUsecase;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendEmailUsecaseImpl implements SendEmailUsecase {
    @Value("${spring.application.protocol}") // Load from application.properties
    private String protocol;
    @Value("${spring.application.host}") // Load from application.properties
    private String host;
    @Value("${spring.application.port}") // Load from application.properties
    private String port;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String emailDestination, String token) {
        try {
            String subject = "Verify Your Email";
            String verificationUrl = protocol + "://" + host + ":" + port + "/api/v1/auth/verify?token=" + token;
            String content = "<p>Click the link below to verify your email:</p>"
                    + "<p><a href=\"" + verificationUrl + "\">Verify Email</a></p>";
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailDestination);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email, " + e.getMessage());
        }

    }
}
