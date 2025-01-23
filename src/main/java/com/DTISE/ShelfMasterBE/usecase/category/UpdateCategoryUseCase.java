package com.DTISE.ShelfMasterBE.usecase.category;

import com.DTISE.ShelfMasterBE.infrastructure.category.dto.UpdateCategoryRequest;
import com.DTISE.ShelfMasterBE.infrastructure.category.dto.UpdateCategoryResponse;

public interface UpdateCategoryUseCase {
    UpdateCategoryResponse updateCategory(Long id, UpdateCategoryRequest req);
}
