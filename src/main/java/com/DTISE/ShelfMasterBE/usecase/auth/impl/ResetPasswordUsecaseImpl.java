package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateEmailException;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.auth.ResetPasswordUsecase;
import com.DTISE.ShelfMasterBE.usecase.auth.SendEmailUsecase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResetPasswordUsecaseImpl implements ResetPasswordUsecase {
    private final UserRepository userRepository;
    private final SendEmailUsecase sendEmailUsecase;

    public ResetPasswordUsecaseImpl(UserRepository userRepository, SendEmailUsecase sendEmailUsecase) {
        this.userRepository = userRepository;
        this.sendEmailUsecase = sendEmailUsecase;
    }

    @Override
    public void resetPassword(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User newUser = existingUser.get();
            if (!newUser.getIsVerified()) {
                throw new DuplicateEmailException("Email is unverified.");
            }
            newUser.setVerificationToken(UUID.randomUUID().toString());
            newUser.setTokenExpiry(OffsetDateTime.now().plusMinutes(10));
            try {
                User savedUser = userRepository.save(newUser);
                sendEmailUsecase.sendResetPassword(savedUser.getEmail(), savedUser.getVerificationToken());
            } catch (Exception e) {
                throw new RuntimeException("Can't save user, " + e.getMessage());
            }
        }
    }
}
