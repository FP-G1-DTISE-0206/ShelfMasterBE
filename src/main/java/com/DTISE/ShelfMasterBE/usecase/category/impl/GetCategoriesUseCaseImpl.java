package com.DTISE.ShelfMasterBE.usecase.category.impl;

import com.DTISE.ShelfMasterBE.infrastructure.category.dto.GetCategoryResponse;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.usecase.category.GetCategoriesUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetCategoriesUseCaseImpl implements GetCategoriesUseCase {
    private final CategoryRepository categoryRepository;

    public GetCategoriesUseCaseImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<GetCategoryResponse> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(category -> new GetCategoryResponse(
                        category.getId(),
                        category.getName()
                ));
    }
}
