package com.DTISE.ShelfMasterBE.usecase.userAddress.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.UserAddress;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto.UserAddressResponse;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.repository.UserAddressRepository;
import com.DTISE.ShelfMasterBE.usecase.userAddress.GetUserAddressUsecase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetUserAddressUsecaseImpl implements GetUserAddressUsecase {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    public GetUserAddressUsecaseImpl(UserRepository userRepository, UserAddressRepository userAddressRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public List<UserAddressResponse> getUserAddress(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        return userAddressRepository.findByUser_IdOrderByIsDefaultDescIdAsc((user.getId())).stream().map(address ->
                new UserAddressResponse(
                        address.getId(),
                        address.getUser().getId(),
                        address.getContactName(),
                        address.getContactNumber(),
                        address.getProvince(),
                        address.getCity(),
                        address.getDistrict(),
                        address.getPostalCode(),
                        address.getAddress(),
                        address.getLatitude(),
                        address.getLongitude(),
                        address.getAreaId(),
                        address.getIsDefault()
                )).toList();
    }

    @Override
    public UserAddressResponse getUserAddressById(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        return userAddressRepository.findByIdAndUser_Id( id, user.getId()).map(
                address -> new UserAddressResponse(
                        address.getId(),
                        address.getUser().getId(),
                        address.getContactName(),
                        address.getContactNumber(),
                        address.getProvince(),
                        address.getCity(),
                        address.getDistrict(),
                        address.getPostalCode(),
                        address.getAddress(),
                        address.getLatitude(),
                        address.getLongitude(),
                        address.getAreaId(),
                        address.getIsDefault())
        ).orElseThrow(() -> new DataNotFoundException("User address not found"));
    }
}
