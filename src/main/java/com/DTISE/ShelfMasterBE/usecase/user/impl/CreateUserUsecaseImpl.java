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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists");
        }
        String verificationToken = UUID.randomUUID().toString();
        User newUser = req.toEntity();
        newUser.setPassword("");
        newUser.setVerificationToken(verificationToken);
        newUser.setTokenExpiry(OffsetDateTime.now().plusMinutes(10));
        Set<Role> roles = new HashSet<>();
        newUser.setRoles(roles);
        Optional<Role> defaultRole;
        defaultRole = roleRepository.findByName("USER");
        if (defaultRole.isEmpty()) {
            throw new RuntimeException("Default role not found");
        }
        newUser.getRoles().add(defaultRole.get());
        try {
            newUser = userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Can't save user, " + e.getMessage());
        }
        sendEmailUsecase.sendVerificationEmail(newUser.getEmail(), newUser.getVerificationToken());

        return new RegisterResponse(newUser.getId(), newUser.getEmail());
    }

}
