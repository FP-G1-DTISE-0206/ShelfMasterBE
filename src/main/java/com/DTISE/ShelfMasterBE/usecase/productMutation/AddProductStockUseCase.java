package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AddProductStockRequest;

public interface AddProductStockUseCase {
    Long addProductStock(AddProductStockRequest req);
}
