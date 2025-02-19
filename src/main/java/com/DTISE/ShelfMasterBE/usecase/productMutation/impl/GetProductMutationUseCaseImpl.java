package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.PermissionUtils;
import com.DTISE.ShelfMasterBE.common.tools.ProductMapper;
import com.DTISE.ShelfMasterBE.common.tools.UserMapper;
import com.DTISE.ShelfMasterBE.entity.ProductMutation;
import com.DTISE.ShelfMasterBE.entity.ProductMutationLog;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.Claims;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.MutationDestinationResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.MutationOriginResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.ProductMutationLogResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.ProductMutationRepository;
import com.DTISE.ShelfMasterBE.usecase.productMutation.GetProductMutationUseCase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Set;

@Service
public class GetProductMutationUseCaseImpl implements GetProductMutationUseCase {
    @PersistenceContext
    private EntityManager entityManager;

    private final ProductMutationRepository productMutationRepository;
    private final UserRepository userRepository;

    public GetProductMutationUseCaseImpl(
            ProductMutationRepository productMutationRepository,
            UserRepository userRepository
    ) {
        this.productMutationRepository = productMutationRepository;
        this.userRepository = userRepository;
    }

    @Override
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
        return new ProductMutationResponse(
                productMutation.getId(),
                productMutation.getMutationType().getOriginType(),
                productMutation.getMutationType().getDestinationType(),
                mapMutationOriginResponse(getOriginEntity(productMutation)),
                mapMutationDestinationResponse(getDestinationEntity(productMutation)),
                ProductMapper.mapGetProductResponse(productMutation.getProduct()),
                productMutation.getQuantity(),
                UserMapper.mapUserResponse(productMutation.getRequestedByUser()),
                (productMutation.getProcessedByUser() == null
                        ? null : UserMapper.mapUserResponse(productMutation.getProcessedByUser())),
                productMutation.getIsApproved(),
                getLatestProductMutationLog(productMutation.getMutationLogs()),
                productMutation.getMutationLogs()
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

    private MutationOriginResponse mapMutationOriginResponse(
            Object origin) {
        if (origin == null) return null;
        return new MutationOriginResponse(
                getEntityId(origin, "origin"),
                getEntityName(origin, "origin")
        );
    }

    private MutationDestinationResponse mapMutationDestinationResponse(
            Object destination) {
        if (destination == null) return null;
        return new MutationDestinationResponse(
                getEntityId(destination, "destination"),
                getEntityName(destination, "destination")
        );
    }

    private Long getEntityId(Object entity, String type) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return (Long) field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error mapping mutation " + type);
        }
    }

    private String getEntityName(Object entity, String type) {
        for(String fieldName: new String[]{"userName", "name"}) {
            try {
                Field field = entity.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return (String) field.get(entity);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }
        throw new RuntimeException("Error mapping mutation " + type);
    }

    private Object getOriginEntity(ProductMutation mutation) {
        Class<?> entityType = mutation.getOriginEntityType();
        return (entityType != null)
                ? entityManager.find(entityType, mutation.getOriginId())
                : null;
    }

    private Object getDestinationEntity(ProductMutation mutation) {
        Class<?> entityType = mutation.getDestinationEntityType();
        return (entityType != null)
                ? entityManager.find(entityType, mutation.getDestinationId())
                : null;
    }
}
