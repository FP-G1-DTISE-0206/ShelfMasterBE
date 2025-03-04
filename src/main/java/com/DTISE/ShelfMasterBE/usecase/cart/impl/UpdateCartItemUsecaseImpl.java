package com.DTISE.ShelfMasterBE.usecase.cart.impl;

import com.DTISE.ShelfMasterBE.entity.Cart;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.UpdateCartItemRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.UpdateCartItemResponse;
import com.DTISE.ShelfMasterBE.infrastructure.cart.repository.CartRepository;
import com.DTISE.ShelfMasterBE.usecase.cart.UpdateCartItemUsecase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.OffsetDateTime;

@Service
public class UpdateCartItemUsecaseImpl implements UpdateCartItemUsecase {

    private final CartRepository cartRepository;

    public UpdateCartItemUsecaseImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public UpdateCartItemResponse execute(Long cartId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.setQuantity(request.getQuantity());
        cart.setUpdatedAt(OffsetDateTime.now());
        cartRepository.save(cart);

        return new UpdateCartItemResponse(
                cart.getId(),
                cart.getProduct().getId(),
                cart.getProduct().getName(),
                cart.getQuantity(),
                cart.getIsProcessed(),
                cart.getUpdatedAt()
        );
    };
};
