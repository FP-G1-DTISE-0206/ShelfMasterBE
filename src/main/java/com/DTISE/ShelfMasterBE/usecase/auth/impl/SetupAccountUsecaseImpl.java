package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.SetupAccountRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.auth.SetupAccountUsecase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class SetupAccountUsecaseImpl implements SetupAccountUsecase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupAccountUsecaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void setupAccount(SetupAccountRequest req) {
        userRepository.findByVerificationToken(req.getToken())
                .ifPresentOrElse(
                        user -> {
                            if (user.getTokenExpiry() != null && user.getTokenExpiry().isBefore(OffsetDateTime.now())) {
                                throw new DataNotFoundException("Token has expired.");
                            }
                            user.setUserName(req.getUserName());
                            user.setPassword(passwordEncoder.encode(req.getPassword()));
                            user.setIsVerified(true);
                            user.setVerificationToken(null); // Remove token after verification
                            user.setTokenExpiry(null);
                            userRepository.save(user);
                        },
                        () -> {
                            throw new DataNotFoundException("Invalid or expired token.");
                        }
                );
    }
}
