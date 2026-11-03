package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.CreateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.UpdateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingStatus;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters.BuildingCodeAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;

/**
 * Residential building whose critical equipment Vigilia monitors.
 */
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Building extends AbstractAggregateRoot<Building> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    @Convert(converter = BuildingCodeAttributeConverter.class)
    private BuildingCode code;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private Integer totalUnits;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BuildingStatus status;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    protected Building() {}

    public Building(CreateBuildingCommand command) {
        this.name = command.name();
        this.code = command.code();
        this.address = command.address();
        this.district = command.district();
        this.totalUnits = command.totalUnits();
        this.status = BuildingStatus.ACTIVE;
    }

    public void update(UpdateBuildingCommand command) {
        this.name = command.name();
        this.address = command.address();
        this.district = command.district();
        this.totalUnits = command.totalUnits();
        this.status = command.status();
    }
}