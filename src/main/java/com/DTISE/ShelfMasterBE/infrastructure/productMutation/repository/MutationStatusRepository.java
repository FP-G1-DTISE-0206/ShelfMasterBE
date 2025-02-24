package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.common.enums.MutationStatusEnum;
import com.DTISE.ShelfMasterBE.entity.MutationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MutationStatusRepository extends JpaRepository<MutationStatus, Long> {
    Optional<MutationStatus> findFirstByName(MutationStatusEnum statusEnum);
}
