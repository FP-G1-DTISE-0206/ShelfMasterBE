package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.auth.SetupPasswordUsecase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class SetupPasswordUsecaseImpl implements SetupPasswordUsecase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupPasswordUsecaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void setupPassword(String token, String password) {
        userRepository.findByVerificationToken(token)
                .ifPresentOrElse(
                        user -> {
                            if (user.getTokenExpiry() != null && user.getTokenExpiry().isBefore(OffsetDateTime.now())) {
                                throw new DataNotFoundException("Token has expired.");
                            }
                            user.setPassword(passwordEncoder.encode(password));
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
