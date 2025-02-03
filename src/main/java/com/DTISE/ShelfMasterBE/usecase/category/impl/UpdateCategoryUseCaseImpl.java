package com.DTISE.ShelfMasterBE.usecase.category.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.category.dto.UpdateCategoryRequest;
import com.DTISE.ShelfMasterBE.infrastructure.category.dto.UpdateCategoryResponse;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.usecase.category.UpdateCategoryUseCase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class UpdateCategoryUseCaseImpl implements UpdateCategoryUseCase {
    private final CategoryRepository categoryRepository;

    public UpdateCategoryUseCaseImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public UpdateCategoryResponse updateCategory(Long id, UpdateCategoryRequest req) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(req.getName());
                    existingCategory.setUpdatedAt(OffsetDateTime.now());
                    return categoryRepository.save(existingCategory);
                })
                .map(updatedCategory -> new UpdateCategoryResponse(
                        updatedCategory.getId(),
                        updatedCategory.getName()))
                .orElseThrow(() -> new DataNotFoundException("There's no category with ID: " + id));
    }
}
