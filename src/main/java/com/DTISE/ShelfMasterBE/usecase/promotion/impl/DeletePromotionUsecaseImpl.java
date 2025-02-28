package com.DTISE.ShelfMasterBE.usecase.promotion.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.repository.PromotionRepository;
import com.DTISE.ShelfMasterBE.usecase.promotion.DeletePromotionUsecase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class DeletePromotionUsecaseImpl implements DeletePromotionUsecase {
    private final PromotionRepository promotionRepository;

    public DeletePromotionUsecaseImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void deletePromotion(Long id) {
        promotionRepository
                .findById(id)
                .map(existingPromotion -> {
                    existingPromotion.setDeletedAt(OffsetDateTime.now());
                    return promotionRepository.save(existingPromotion);
                })
                .orElseThrow(() -> new DataNotFoundException("There's no promotion with ID: " + id));
    }
}
