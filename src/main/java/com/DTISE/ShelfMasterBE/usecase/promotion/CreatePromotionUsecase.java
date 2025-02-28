package com.DTISE.ShelfMasterBE.usecase.promotion;

import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionRequest;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionResponse;

public interface CreatePromotionUsecase {
    PromotionResponse createPromotion(PromotionRequest req);
}
