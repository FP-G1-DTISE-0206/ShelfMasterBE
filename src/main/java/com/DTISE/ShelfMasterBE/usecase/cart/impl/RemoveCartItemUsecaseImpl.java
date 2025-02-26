package com.DTISE.ShelfMasterBE.usecase.cart.impl;

import com.DTISE.ShelfMasterBE.entity.Cart;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.cart.repository.CartRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.cart.RemoveCartItemUsecase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class RemoveCartItemUsecaseImpl implements RemoveCartItemUsecase {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public RemoveCartItemUsecaseImpl(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void execute(Long userId, Long cartId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findById(BigInteger.valueOf(cartId))
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

//        if (!productRepository.existsById(cart.getProduct().getId())) {
//            throw new RuntimeException("Product still linked, cannot delete cart item.");
//        }

        cartRepository.delete(cart);

    }
}
