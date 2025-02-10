package com.DTISE.ShelfMasterBE.usecase.user.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateEmailException;
import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.RoleRepository;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.auth.SendEmailUsecase;
import com.DTISE.ShelfMasterBE.usecase.user.CreateUserUsecase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class CreateUserUsecaseImpl implements CreateUserUsecase {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendEmailUsecase sendEmailUsecase;

    public CreateUserUsecaseImpl(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, SendEmailUsecase sendEmailUsecase) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sendEmailUsecase = sendEmailUsecase;
    }

    @Override
    @Transactional
    public RegisterResponse createUser(RegisterRequest req) {
        Optional<User> existingUser = userRepository.findByEmail(req.getEmail());
        if (existingUser.isPresent()) {
            User newUser = existingUser.get();
            if (newUser.getIsVerified()) {
                throw new DuplicateEmailException("Email already exists");
            }
            return seedUser(newUser, req);
        }
        return seedUser(new User(), req);
    }

    private RegisterResponse seedUser(User newUser, RegisterRequest req) {
        if (newUser.getId() == null) { // If the user is new, initialize it from request
            newUser = req.toEntity();
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));

            newUser.setRoles(Collections.singleton(defaultRole));
        }
        newUser.setUserName("");
        newUser.setPassword("");
        newUser.setVerificationToken(UUID.randomUUID().toString());
        newUser.setTokenExpiry(OffsetDateTime.now().plusMinutes(10));
        try {
            User savedUser = userRepository.save(newUser);
            sendEmailUsecase.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());
            return new RegisterResponse(savedUser.getId(), savedUser.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Can't save user, " + e.getMessage());
        }
    }

}
