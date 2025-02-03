package com.DTISE.ShelfMasterBE.usecase.admin.impl;

import com.DTISE.ShelfMasterBE.common.tools.AdminWarehouseMapper;
import com.DTISE.ShelfMasterBE.common.tools.UserRoleMapper;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.admin.dto.AdminResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import com.DTISE.ShelfMasterBE.usecase.admin.GetAdminsUsecase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class GetAdminsUsecaseImpl implements GetAdminsUsecase {

    private final UserRepository userRepository;

    public GetAdminsUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<AdminResponse> getAdmins(Pageable pageable, String search) {
        return userRepository.findAdminsBySearch(search, pageable).map(
                admin -> new AdminResponse(
                        admin.getId(),
                        admin.getEmail(),
                        admin.getUserName(),
                        admin.getImageUrl(),
                        UserRoleMapper.mapUserRoleResponse(admin.getRoles()),
                        AdminWarehouseMapper.mapAdminWarehouseResponse(admin.getWarehouses())
                ));
    }
    

}
