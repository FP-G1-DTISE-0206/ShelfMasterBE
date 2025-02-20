package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.common.tools.ProductMapper;
import com.DTISE.ShelfMasterBE.common.tools.UserMapper;
import com.DTISE.ShelfMasterBE.entity.*;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.MutationDestinationResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.MutationOriginResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationLogResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.VendorRepository;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.productMutation.GetProductMutationUseCase;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class GetProductMutationUseCaseImpl implements GetProductMutationUseCase {
    private final ProductMutationRepository productMutationRepository;
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final VendorRepository vendorRepository;

    public GetProductMutationUseCaseImpl(
            ProductMutationRepository productMutationRepository,
            UserRepository userRepository,
            WarehouseRepository warehouseRepository,
            VendorRepository vendorRepository
    ) {
        this.productMutationRepository = productMutationRepository;
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    @Transactional
    public Page<ProductMutationResponse> getProductMutations(Pageable pageable, String search, Long warehouseId) {
        validateUserAccess(warehouseId);
        return productMutationRepository.getAllBySearchAndWarehouseId(
                        search, pageable, warehouseId,
                        MutationEntityType.WAREHOUSE)
                .map(this::mapProductMutationResponse);
    }

    private void validateUserAccess(Long warehouseId) {
        if(warehouseId == null) return;
        User user = userRepository.findById(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        if (PermissionUtils.isSuperAdmin(user)) return;
        PermissionUtils.isAdminOfCurrentWarehouse(user, warehouseId);
    }

    private ProductMutationResponse mapProductMutationResponse(ProductMutation productMutation) {
        Hibernate.initialize(productMutation.getMutationType());
        MutationType mutationType = Hibernate.unproxy(productMutation.getMutationType(), MutationType.class);

        Hibernate.initialize(productMutation.getProduct());
        Product product = Hibernate.unproxy(productMutation.getProduct(), Product.class);

        Hibernate.initialize(productMutation.getRequestedByUser());
        User requestedByUser = Hibernate.unproxy(productMutation.getRequestedByUser(), User.class);

        User processedByUser = null;
        if (productMutation.getProcessedByUser() != null) {
            Hibernate.initialize(productMutation.getProcessedByUser());
            processedByUser = Hibernate.unproxy(productMutation.getProcessedByUser(), User.class);
        }

        Hibernate.initialize(productMutation.getMutationLogs());
        Set<ProductMutationLog> logs = productMutation.getMutationLogs();

        MutationOriginResponse originEntity = findOriginEntity(mutationType.getOriginType(), productMutation.getOriginId());
        MutationDestinationResponse destinationEntity = findDestinationEntity(mutationType.getDestinationType(), productMutation.getDestinationId());

        if (originEntity != null) Hibernate.initialize(originEntity);
        if (destinationEntity != null) Hibernate.initialize(destinationEntity);

        return new ProductMutationResponse(
                productMutation.getId(),
                mutationType.getOriginType(),
                mutationType.getDestinationType(),
                originEntity,
                destinationEntity,
                ProductMapper.mapGetProductResponse(product),
                productMutation.getQuantity(),
                UserMapper.mapUserResponse(requestedByUser),
                (processedByUser == null ? null : UserMapper.mapUserResponse(processedByUser)),
                productMutation.getIsApproved(),
                getLatestProductMutationLog(logs),
                logs
        );
    }

    private ProductMutationLogResponse getLatestProductMutationLog(Set<ProductMutationLog> logs) {
        return logs
                .stream()
                .max(Comparator.comparing(ProductMutationLog::getCreatedAt))
                .map(productMutationLog -> new ProductMutationLogResponse(
                        productMutationLog.getId(),
                        MutationStatusEnum.valueOf(productMutationLog.getMutationStatus().getName().toString())
                ))
                .orElse(null);
    }

    private MutationOriginResponse findOriginEntity(MutationEntityType mutationEntityType, Long entityId) {
        return switch (mutationEntityType) {
            case USER -> {
                User user = userRepository.findById(entityId)
                        .orElseThrow(() -> new DataNotFoundException("User not found"));
                yield new MutationOriginResponse(user.getId(), user.getUserName());
            }
            case WAREHOUSE -> {
                Warehouse warehouse = warehouseRepository.findById(entityId)
                        .orElseThrow(() -> new DataNotFoundException("Warehouse not found"));
                yield new MutationOriginResponse(warehouse.getId(), warehouse.getName());
            }
            case VENDOR -> {
                Vendor vendor = vendorRepository.findById(entityId)
                        .orElseThrow(() -> new DataNotFoundException("Vendor not found"));
                yield new MutationOriginResponse(vendor.getId(), vendor.getName());
            }
            default -> null;
        };
    }

    private MutationDestinationResponse findDestinationEntity(MutationEntityType mutationEntityType, Long entityId) {
        return switch (mutationEntityType) {
            case USER -> {
                User user = userRepository.findById(entityId)
                        .orElseThrow(() -> new DataNotFoundException("User not found"));
                yield new MutationDestinationResponse(user.getId(), user.getUserName());
            }
            case WAREHOUSE -> {
                Warehouse warehouse = warehouseRepository.findById(entityId)
                        .orElseThrow(() -> new DataNotFoundException("Warehouse not found"));
                yield new MutationDestinationResponse(warehouse.getId(), warehouse.getName());
            }
            case VENDOR -> {
                Vendor vendor = vendorRepository.findById(entityId)
                        .orElseThrow(() -> new DataNotFoundException("Vendor not found"));
                yield new MutationDestinationResponse(vendor.getId(), vendor.getName());
            }
            default -> null;
        };
    }
}
