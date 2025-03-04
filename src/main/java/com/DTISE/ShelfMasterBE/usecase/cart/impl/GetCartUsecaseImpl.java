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

import java.math.BigDecimal;
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
    public GetCartResponse execute(String email) {
        User user = userRepository.findByEmailContainsIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUserOrderByIdDesc(user);
        List<CartItemResponse> items = cartItems.stream().map(cart ->
                new CartItemResponse(
                        cart.getId(),
                        cart.getProduct().getId(),
                        cart.getProduct().getName(),
                        cart.getQuantity(),
                        cart.getIsProcessed(),
                        cart.getCreatedAt(),
                        cart.getUpdatedAt()
                )
        ).collect(Collectors.toList());

        int totalQuantity = cartItems.stream().mapToInt(Cart::getQuantity).sum();

        BigDecimal totalPrice = cartItems.stream().map(cart -> cart.getProduct().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);


        return new GetCartResponse(user.getId(), items, totalQuantity, totalPrice);
    }

}
