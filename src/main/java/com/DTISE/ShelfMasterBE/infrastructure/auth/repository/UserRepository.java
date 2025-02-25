package com.DTISE.ShelfMasterBE.infrastructure.auth.repository;

import com.DTISE.ShelfMasterBE.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailContainsIgnoreCase(String email);
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u " +
            "JOIN u.roles r " +
            "WHERE r.name = 'WH_ADMIN' " +
            "AND (:search IS NULL OR :search = '' " +
            "     OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findAdminsBySearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.roles r " +
            "WHERE (:search IS NULL OR :search = '' " +
            "     OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    Page<User> findUsersBySearch(@Param("search") String search, Pageable pageable);


    @Query("SELECT u FROM User u " +
            "JOIN u.roles r " +
            "WHERE r.name = 'WH_ADMIN' " +
            "AND (:search IS NULL OR :search = '' " +
            "     OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<User> findAdminsBySearchSimple(@Param("search") String search);

    Optional<User> findByVerificationToken(String token);

    Optional<User> findByVerificationTokenAndEmail(String verificationToken, String email);

    Optional<Object> findById(@NotNull(message = "User ID cannot be null") BigInteger userId);


}
