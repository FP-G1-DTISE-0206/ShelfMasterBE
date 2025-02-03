package com.DTISE.ShelfMasterBE.infrastructure.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryResponse {
    private Long id;
    private String name;
}
