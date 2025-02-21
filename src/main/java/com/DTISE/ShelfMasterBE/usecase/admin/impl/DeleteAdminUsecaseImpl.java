package com.DTISE.ShelfMasterBE.usecase.admin.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.admin.DeleteAdminUsecase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
@Service
public class DeleteAdminUsecaseImpl implements DeleteAdminUsecase {
    private final UserRepository userRepository;

    public DeleteAdminUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void deleteAdmin(Long id) {
        userRepository
                .findById(id)
                .map(existingUser -> {
                    existingUser.setDeletedAt(OffsetDateTime.now());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new DataNotFoundException("There's no product with ID: " + id));
    }
}
