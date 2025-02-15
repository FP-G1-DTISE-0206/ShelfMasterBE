package com.DTISE.ShelfMasterBE.entity;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "mutation_type")
@SQLRestriction("deleted_at IS NULL")
public class MutationType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutation_type_id_gen")
    @SequenceGenerator(name = "mutation_type_id_gen", sequenceName = "mutation_type_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "origin_type", nullable = false)
    private MutationEntityType originType;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "destination_type", nullable = false)
    private MutationEntityType destinationType;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    @PreRemove
    protected void onRemove() {
        deletedAt = OffsetDateTime.now();
    }
}
