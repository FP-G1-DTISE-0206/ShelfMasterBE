package com.DTISE.ShelfMasterBE.usecase.user.impl;

import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.ChangePasswordRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.user.ChangePasswordUsecase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordUsecaseImpl implements ChangePasswordUsecase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUsecaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean changePassword(ChangePasswordRequest req, String email) {
        return userRepository.findByEmailContainsIgnoreCase(email).map(user -> {
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
            try {
                userRepository.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }).orElse(false);
    }
}
