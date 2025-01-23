package com.DTISE.ShelfMasterBE.usecase.category.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateCategoryNameException;
import com.DTISE.ShelfMasterBE.entity.Category;
import com.DTISE.ShelfMasterBE.infrastructure.category.dto.CreateCategoryRequest;
import com.DTISE.ShelfMasterBE.infrastructure.category.dto.CreateCategoryResponse;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.usecase.category.CreateCategoryUseCase;
import org.springframework.stereotype.Service;

@Service
public class CreateCategoryUseCaseImpl implements CreateCategoryUseCase {
    private final CategoryRepository categoryRepository;

    public CreateCategoryUseCaseImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest req) {
        if (categoryRepository.existsByName(req.getName())) {
            throw new DuplicateCategoryNameException("Category name must be unique.");
        }
        Category createdCategory = categoryRepository.save(req.toEntity());
        return new CreateCategoryResponse(
                createdCategory.getId(),
                createdCategory.getName());
    }
}
