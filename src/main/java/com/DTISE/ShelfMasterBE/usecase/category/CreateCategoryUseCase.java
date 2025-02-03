package com.DTISE.ShelfMasterBE.usecase.category;

import com.DTISE.ShelfMasterBE.infrastructure.category.dto.CreateCategoryRequest;
import com.DTISE.ShelfMasterBE.infrastructure.category.dto.CreateCategoryResponse;

public interface CreateCategoryUseCase {
    CreateCategoryResponse createCategory(CreateCategoryRequest request);
}
