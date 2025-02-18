package com.DTISE.ShelfMasterBE.usecase.admin.impl;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.admin.ChangeAdminPasswordUsecase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangeAdminPasswordUsecaseImpl implements ChangeAdminPasswordUsecase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangeAdminPasswordUsecaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void changeAdminPassword(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
