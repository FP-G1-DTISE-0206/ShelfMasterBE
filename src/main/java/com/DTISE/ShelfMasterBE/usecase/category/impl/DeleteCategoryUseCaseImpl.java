package com.DTISE.ShelfMasterBE.usecase.category.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.ProductCategory;
import com.DTISE.ShelfMasterBE.infrastructure.category.repository.CategoryRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductCategoryRepository;
import com.DTISE.ShelfMasterBE.usecase.category.DeleteCategoryUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Set;

@Service
public class DeleteCategoryUseCaseImpl implements DeleteCategoryUseCase {
    private final CategoryRepository categoryRepository;

    public DeleteCategoryUseCaseImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void deleteCategory(Long id) {
        deleteCategoryFromCategory(id);
    }

    private void deleteCategoryFromCategory(Long id) {
        categoryRepository
                .findById(id)
                .map(existingCategory -> {
                    existingCategory.setDeletedAt(OffsetDateTime.now());
                    return categoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new DataNotFoundException("There's no category with ID: " + id));
    }
}
