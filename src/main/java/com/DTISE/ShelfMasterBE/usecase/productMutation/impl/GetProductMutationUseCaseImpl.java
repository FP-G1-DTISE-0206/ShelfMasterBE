package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.enums.MutationStatus;
import com.DTISE.ShelfMasterBE.common.tools.ProductMapper;
import com.DTISE.ShelfMasterBE.common.tools.UserMapper;
import com.DTISE.ShelfMasterBE.entity.ProductMutation;
import com.DTISE.ShelfMasterBE.entity.ProductMutationLog;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.GetProductMutationResponse;
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

    public GetProductMutationUseCaseImpl(ProductMutationRepository productMutationRepository) {
        this.productMutationRepository = productMutationRepository;
    }

    @Override
    public Page<GetProductMutationResponse> getProductMutations(Pageable pageable, Long warehouseId) {
        return productMutationRepository.getAllByWarehouseId(pageable, warehouseId)
                .map(productMutation -> new GetProductMutationResponse(
                        productMutation.getId(),
                        productMutation.getMutationType(),
                        mapMutationOriginResponse(getOriginEntity(productMutation)),
                        mapMutationDestinationResponse(getDestinationEntity(productMutation)),
                        ProductMapper.mapGetProductResponse(productMutation.getProduct()),
                        productMutation.getQuantity(),
                        UserMapper.mapUserResponse(productMutation.getRequestedByUser()),
                        (productMutation.getProcessedBy() == null
                                ? null : UserMapper.mapUserResponse(productMutation.getProcessedByUser())),
                        productMutation.getIsApproved(),
                        getLatestProductMutationLog(productMutation.getMutationLogs()),
                        productMutation.getMutationLogs()
                ));
    }

    private ProductMutationLogResponse getLatestProductMutationLog(Set<ProductMutationLog> logs) {
        return logs
                .stream()
                .max(Comparator.comparing(ProductMutationLog::getCreatedAt))
                .map(productMutationLog -> new ProductMutationLogResponse(
                        productMutationLog.getId(),
                        MutationStatus.valueOf(productMutationLog.getMutationStatus().getName().toString())
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
