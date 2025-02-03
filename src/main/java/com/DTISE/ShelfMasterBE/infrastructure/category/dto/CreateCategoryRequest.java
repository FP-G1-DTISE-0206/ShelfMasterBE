package com.DTISE.ShelfMasterBE.infrastructure.category.dto;

import com.DTISE.ShelfMasterBE.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    private String name;

    public Category toEntity() {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
