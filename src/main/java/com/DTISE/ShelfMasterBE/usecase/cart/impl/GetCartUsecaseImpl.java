package com.DTISE.ShelfMasterBE.usecase.cart.impl;

import com.DTISE.ShelfMasterBE.entity.Cart;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartItemResponse;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.GetCartResponse;
import com.DTISE.ShelfMasterBE.infrastructure.cart.repository.CartRepository;
import com.DTISE.ShelfMasterBE.usecase.cart.GetCartUsecase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetCartUsecaseImpl implements GetCartUsecase {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public GetCartUsecaseImpl(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public GetCartResponse execute(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser(user);

        List<CartItemResponse> items = cartItems.stream().map(cart ->
                new CartItemResponse(
                        cart.getId(),
                        BigInteger.valueOf(cart.getProduct().getId()),
                        cart.getProduct().getName(),
                        cart.getQuantity(),
                        cart.getIsProcessed(),
                        cart.getCreatedAt(),
                        cart.getUpdatedAt()
                )
        ).collect(Collectors.toList());


        return new GetCartResponse(user.getId(), items);
    }

}
