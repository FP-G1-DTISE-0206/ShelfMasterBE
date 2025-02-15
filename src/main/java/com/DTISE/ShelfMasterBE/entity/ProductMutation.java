package com.DTISE.ShelfMasterBE.entity;

import com.DTISE.ShelfMasterBE.common.enums.MutationEntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product_mutation")
@SQLRestriction("deleted_at IS NULL")
public class ProductMutation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_mutation_id_gen")
    @SequenceGenerator(name = "product_mutation_id_gen", sequenceName = "product_mutation_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "mutation_type_id", nullable = false)
    private Long mutationTypeId;

    @NotNull
    @Column(name = "origin_id", nullable = false)
    private Long originId;

    @NotNull
    @Column(name = "destination_id", nullable = false)
    private Long destinationId;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull
    @Column(name = "requested_by", nullable = false)
    private Long requestedBy;

    @NotNull
    @Column(name = "processed_by")
    private Long processedBy;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = false;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "mutation_type_id")
    private MutationType mutationType;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "requested_by", referencedColumnName = "id")
    private User requestedByUser;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "processed_by", referencedColumnName = "id")
    private User processedByUser;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "product_mutation_id")
    private Set<ProductMutationLog> mutationLogs = new HashSet<>();

    public Class<?> getOriginEntityType() {
        return getEntityType(mutationType.getOriginType());
    }

    public Class<?> getDestinationEntityType() {
        return getEntityType(mutationType.getDestinationType());
    }

    private Class<?> getEntityType(MutationEntityType type) {
        return switch (type) {
            case MutationEntityType.USER -> User.class;
            case MutationEntityType.VENDOR -> Vendor.class;
            case MutationEntityType.WAREHOUSE -> Warehouse.class;
            default -> null;
        };
    }

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
