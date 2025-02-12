package com.DTISE.ShelfMasterBE.usecase.userAddress.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.UserAddress;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressRequest;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressResponse;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.repository.UserAddressRepository;
import com.DTISE.ShelfMasterBE.usecase.userAddress.UpdateUserAddressUsecase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UpdateUserAddressUsecaseImpl implements UpdateUserAddressUsecase {
    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    public UpdateUserAddressUsecaseImpl(UserAddressRepository userAddressRepository, UserRepository userRepository) {
        this.userAddressRepository = userAddressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserAddressResponse updateUserAddress(Long id, UserAddressRequest req, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));

        return userAddressRepository.findByIdAndUser_Id(id, user.getId()).map(userAddress -> {
            userAddress.setContactName(req.getContactName());
            userAddress.setContactNumber(req.getContactNumber());
            userAddress.setProvince(req.getProvince());
            userAddress.setCity(req.getCity());
            userAddress.setDistrict(req.getDistrict());
            userAddress.setPostalCode(req.getPostalCode());
            userAddress.setAddress(req.getAddress());
            userAddress.setLatitude(req.getLatitude());
            userAddress.setLongitude(req.getLongitude());
            userAddress.setAreaId(req.getAreaId());
            return userAddressRepository.save(userAddress);
        }).map(
                UserAddressResponse::new
        ).orElseThrow(() -> new DataNotFoundException("User address not found"));
    }

    @Override
    @Transactional
    public UserAddressResponse setDefaultAddress(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        UserAddress userAddress = userAddressRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new DataNotFoundException("User address not found"));
        if (userAddress.getIsDefault()) {
            throw new RuntimeException( "Cannot set default address as it is already set");
        }
        userAddressRepository.updateIsDefaultToFalse(user.getId());
        userAddress.setIsDefault(true);
        UserAddress savedUserAddress = userAddressRepository.save(userAddress);
        return new UserAddressResponse(savedUserAddress);
    }
}
