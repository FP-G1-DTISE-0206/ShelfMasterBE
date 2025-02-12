package com.DTISE.ShelfMasterBE.infrastructure.userAddress.repository;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.UserAddress;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUser(@NotNull User user);

    List<UserAddress> findByUser_Id(Long userId);

    Optional<UserAddress> findByIdAndUser_Id(Long id, Long userId);

    void deleteByIdAndUser_Id(Long id, Long userId);

    List<UserAddress> findByUser_IdOrderByIsDefaultDescIdAsc(Long userId);

    @Modifying
    @Query("UPDATE UserAddress ua SET ua.isDefault = false WHERE ua.user.id = :userId")
    void updateIsDefaultToFalse(@Param("userId") Long userId);

    long countByUser_Id(Long userId);
}
