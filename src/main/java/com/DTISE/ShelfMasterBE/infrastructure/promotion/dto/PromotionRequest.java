package com.DTISE.ShelfMasterBE.infrastructure.promotion.dto;

import com.DTISE.ShelfMasterBE.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRequest {
    private String title;
    private String description;
    private String imageUrl;
    private String productUrl;
    public Promotion toEntity() {
        Promotion promotion = new Promotion();
        promotion.setTitle(title);
        promotion.setDescription(description);
        promotion.setImageUrl(imageUrl);
        promotion.setProductUrl(productUrl);
        return promotion;
    }
}
