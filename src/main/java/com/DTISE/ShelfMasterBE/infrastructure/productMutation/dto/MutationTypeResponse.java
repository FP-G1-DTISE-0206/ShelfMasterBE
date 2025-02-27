package com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutationTypeResponse {
    private Long id;
    private MutationEntityType originType;
    private MutationEntityType destinationType;
}
