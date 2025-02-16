package com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto;

import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMutationLogResponse {
    private Long id;
    private MutationStatusEnum status;
}
