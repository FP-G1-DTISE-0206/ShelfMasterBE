package com.DTISE.ShelfMasterBE.usecase.userAddress.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.repository.UserAddressRepository;
import com.DTISE.ShelfMasterBE.usecase.userAddress.DeleteUserAddressUsecase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class DeleteUserAddressUsecaseImpl implements DeleteUserAddressUsecase {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    public DeleteUserAddressUsecaseImpl(UserRepository userRepository, UserAddressRepository userAddressRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public void deleteUserAddress(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        userAddressRepository.findByIdAndUser_Id(id, user.getId()).map( userAddress -> {
            userAddress.setDeletedAt(OffsetDateTime.now());
            return userAddressRepository.save(userAddress);
        });
    }
}
