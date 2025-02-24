package com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository;

import com.DTISE.ShelfMasterBE.entity.MutationType;
import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MutationTypeRepository extends JpaRepository<MutationType, Long> {
    Optional<MutationType> findFirstByOriginTypeAndDestinationType(MutationEntityType origin, MutationEntityType destination);
}
