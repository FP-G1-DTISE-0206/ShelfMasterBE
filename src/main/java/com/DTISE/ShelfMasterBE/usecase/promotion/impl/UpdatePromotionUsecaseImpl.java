package com.DTISE.ShelfMasterBE.usecase.promotion.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.Promotion;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionRequest;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionResponse;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.repository.PromotionRepository;
import com.DTISE.ShelfMasterBE.usecase.promotion.UpdatePromotionUsecase;
import org.springframework.stereotype.Service;

@Service
public class UpdatePromotionUsecaseImpl implements UpdatePromotionUsecase {
    private final PromotionRepository promotionRepository;

    public UpdatePromotionUsecaseImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public PromotionResponse updatePromotion(PromotionRequest req, Long id) {
        return promotionRepository.findById(id).map(existingPromotion -> {
            existingPromotion.setTitle(req.getTitle());
            existingPromotion.setDescription(req.getDescription());
            existingPromotion.setImageUrl(req.getImageUrl());
            existingPromotion.setProductUrl(req.getProductUrl());
            return promotionRepository.save(existingPromotion);
        }).map(PromotionResponse::new).orElseThrow(() -> new DataNotFoundException("There's no promotion with ID: " + id));
    }
}
