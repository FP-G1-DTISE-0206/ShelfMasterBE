package com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import com.DTISE.ShelfMasterBE.entity.MutationType;
import com.DTISE.ShelfMasterBE.entity.ProductMutationLog;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.GetProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMutationResponse {
    private Long id;
    private MutationEntityType originType;
    private MutationEntityType destinationType;
    private MutationOriginResponse origin;
    private MutationDestinationResponse destination;
    private GetProductResponse product;
    private Long quantity;
    private UserResponse requester;
    private UserResponse processor;
    private Boolean isApproved;
    private ProductMutationLogResponse latestLog;
    private Set<ProductMutationLog> logs;
}
