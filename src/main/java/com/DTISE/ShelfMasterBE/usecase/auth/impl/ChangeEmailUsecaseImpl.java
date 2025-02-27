package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateEmailException;
import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.auth.ChangeEmailUsecase;
import com.DTISE.ShelfMasterBE.usecase.auth.SendEmailUsecase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChangeEmailUsecaseImpl implements ChangeEmailUsecase {
    private final UserRepository userRepository;
    private final SendEmailUsecase sendEmailUsecase;

    public ChangeEmailUsecaseImpl(UserRepository userRepository, SendEmailUsecase sendEmailUsecase) {
        this.userRepository = userRepository;
        this.sendEmailUsecase = sendEmailUsecase;
    }

    @Override
    public void changeEmail(String email, String newEmail) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(newEmail);
        user.setIsVerified(false);
        user.setUserName("");
        user.setPassword("");
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setTokenExpiry(OffsetDateTime.now().plusMinutes(10));
        try {
            User savedUser = userRepository.save(user);
            sendEmailUsecase.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());
        } catch (Exception e) {
            throw new RuntimeException("Can't save user, " + e.getMessage());
        }
    }
}
