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
            String subject = "ShelfMaster - Verify Your Email";

            String verificationUrl = protocol + "://" + host + ":" + port + "/setup-account?token=" + token;

            String content = "<div style=\"font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;\">"
                    + "<div style=\"max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                    + "<h2 style=\"color: #333; text-align: center;\">Welcome to ShelfMaster!</h2>"
                    + "<p style=\"color: #555;\">You're almost ready to start using ShelfMaster. Please confirm your email by clicking the button below:</p>"
                    + "<div style=\"text-align: center; margin: 20px 0;\">"
                    + "<a href=\"" + verificationUrl + "\" style=\"background-color: #28a745; color: white; padding: 12px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;\">Verify Email</a>"
                    + "</div>"
                    + "<p style=\"color: #777;\">This link will expire in <b>one hour</b>. If you did not request this email, please ignore it.</p>"
                    + "<p style=\"color: #777; text-align: center;\">&copy; 2025 ShelfMaster</p>"
                    + "</div>"
                    + "</div>";

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

    @Override
    public void sendResetPassword(String emailDestination, String token) {
        try {
            String subject = "ShelfMaster - Reset Your Password";

            String resetUrl = protocol + "://" + host + ":" + port + "/setup-password?token=" + token;

            String content = "<div style=\"font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;\">"
                    + "<div style=\"max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                    + "<h2 style=\"color: #333; text-align: center;\">Reset Your Password</h2>"
                    + "<p style=\"color: #555;\">We received a request to reset your password. Click the button below to set a new password:</p>"
                    + "<div style=\"text-align: center; margin: 20px 0;\">"
                    + "<a href=\"" + resetUrl + "\" style=\"background-color: #dc3545; color: white; padding: 12px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;\">Reset Password</a>"
                    + "</div>"
                    + "<p style=\"color: #777;\">If you did not request a password reset, please ignore this email. This link will expire in <b>one hour</b>.</p>"
                    + "<p style=\"color: #777; text-align: center;\">&copy; 2025 ShelfMaster</p>"
                    + "</div>"
                    + "</div>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailDestination);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send reset password link, " + e.getMessage());
        }
    }

}
