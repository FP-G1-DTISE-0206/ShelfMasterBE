package com.DTISE.ShelfMasterBE.usecase.promotion;

import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionRequest;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionResponse;

public interface UpdatePromotionUsecase {
    PromotionResponse updatePromotion(PromotionRequest req, Long id);
}
