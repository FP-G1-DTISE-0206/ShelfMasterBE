package com.DTISE.ShelfMasterBE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_gen")
    @SequenceGenerator(name = "order_id_gen", sequenceName = "order_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "latest_status", nullable = false)
    private OrderStatus latestStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "order_id")
    private Set<OrderItem> orderItems = new HashSet<>();

    @NotNull
    @ColumnDefault("0")
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @NotNull
    @Column(name = "final_price", nullable = false)
    private BigDecimal finalPrice;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;

    @NotNull
    @Column(name = "address_id", nullable = false)
    private Long addressId;

    @Column(name="gateway_trx_id")
    private String midtransTokenUrl;

    @Column(name="payment_proof")
    private String manualTransferProof;

    @NotNull
    @Column(name = "shipping_cost", nullable = false)
    @ColumnDefault("0.0")
    private Double shippingCost;

    @NotNull
    @Size(max = 255)
    @Column(name = "shipping_method", nullable = false)
    private String shippingMethod;

    @NotNull
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @UpdateTimestamp
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