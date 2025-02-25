package com.DTISE.ShelfMasterBE.infrastructure.cart.repository;

import com.DTISE.ShelfMasterBE.entity.Cart;
import com.DTISE.ShelfMasterBE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository <Cart, BigInteger> {
    List<Cart> findByUser(User user);
    Optional<Cart> findByUserAndProduct(User user, com.DTISE.ShelfMasterBE.entity.Product product);

}
