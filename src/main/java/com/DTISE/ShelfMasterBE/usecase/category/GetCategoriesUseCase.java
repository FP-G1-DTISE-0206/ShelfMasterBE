package com.DTISE.ShelfMasterBE.usecase.category;

import com.DTISE.ShelfMasterBE.infrastructure.category.dto.GetCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetCategoriesUseCase {
    Page<GetCategoryResponse> getCategories(Pageable pageable);
}
