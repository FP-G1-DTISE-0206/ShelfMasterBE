package com.DTISE.ShelfMasterBE.usecase.userAddress.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.UserAddress;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressRequest;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressResponse;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.repository.UserAddressRepository;
import com.DTISE.ShelfMasterBE.usecase.userAddress.CreateUserAddressUsecase;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CreateUserAddressUsecaseImpl implements CreateUserAddressUsecase {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    public CreateUserAddressUsecaseImpl(UserRepository userRepository, UserAddressRepository userAddressRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public UserAddressResponse createUserAddress(UserAddressRequest req,String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        long addressCount= userAddressRepository.countByUser_Id(user.getId());
        if (addressCount >= 5) {
            throw new RuntimeException("User already has 5 addresses, cannot add more.");
        }
        UserAddress userAddress = req.toEntity();
        userAddress.setUser(user);
        UserAddress savedUserAddress = userAddressRepository.save(userAddress);
        return new UserAddressResponse(
                savedUserAddress
        );
    }
}
