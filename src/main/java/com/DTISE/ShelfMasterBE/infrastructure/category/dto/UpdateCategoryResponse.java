package com.DTISE.ShelfMasterBE.infrastructure.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryResponse {
    private Long id;
    private String name;
}
