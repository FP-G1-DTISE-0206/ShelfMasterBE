package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.UserRoleMapper;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import com.DTISE.ShelfMasterBE.usecase.auth.VerifyEmailUsecase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class VerifyEmailUsecaseImpl implements VerifyEmailUsecase {
    private final UserRepository userRepository;

    public VerifyEmailUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void verifyUser(String token) {
        userRepository.findByVerificationToken(token)
                .ifPresentOrElse(
                        user -> {
                            if (user.getTokenExpiry() != null && user.getTokenExpiry().isBefore(OffsetDateTime.now())) {
                                throw new DataNotFoundException("Token has expired.");
                            }
                        },
                        () -> {
                            throw new DataNotFoundException("Invalid or expired token.");
                        }
                );
    }
}
