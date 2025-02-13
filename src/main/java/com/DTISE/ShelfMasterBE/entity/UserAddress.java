package com.DTISE.ShelfMasterBE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_address")
@SQLRestriction("deleted_at IS NULL")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_address_id_gen")
    @SequenceGenerator(name = "user_address_id_gen", sequenceName = "user_address_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "contact_name", nullable = false, length = Integer.MAX_VALUE)
    private String contactName;

    @NotNull
    @Column(name = "contact_number", nullable = false, length = Integer.MAX_VALUE)
    private String contactNumber;

    @Size(max = 255)
    @NotNull
    @Column(name = "area_id", nullable = false)
    private String areaId;

    @Size(max = 255)
    @NotNull
    @Column(name = "province", nullable = false)
    private String province;

    @NotNull
    @Column(name = "city", nullable = false, length = Integer.MAX_VALUE)
    private String city;

    @Size(max = 255)
    @NotNull
    @Column(name = "district", nullable = false)
    private String district;

    @NotNull
    @Column(name = "postal_code", nullable = false, length = Integer.MAX_VALUE)
    private String postalCode;

    @NotNull
    @Column(name = "address", nullable = false, length = Integer.MAX_VALUE)
    private String address;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

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