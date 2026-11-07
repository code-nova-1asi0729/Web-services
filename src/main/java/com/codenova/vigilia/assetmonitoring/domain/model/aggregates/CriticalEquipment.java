package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentType;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters.EquipmentCodeAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"buildingId", "code"}))
public class CriticalEquipment extends AbstractAggregateRoot<CriticalEquipment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long buildingId;

    @Column(nullable = false)
    @Convert(converter = EquipmentCodeAttributeConverter.class)
    private EquipmentCode code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentType type;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate installationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    protected CriticalEquipment() {}

    public CriticalEquipment(RegisterEquipmentCommand command) {
        this.buildingId = command.buildingId();
        this.code = command.code();
        this.name = command.name();
        this.type = command.type();
        this.location = command.location();
        this.installationDate = command.installationDate();
        this.status = EquipmentStatus.OPERATIONAL;
    }

    public void update(ModifyEquipmentCommand command) {
        this.name = command.name();
        this.type = command.type();
        this.location = command.location();
        this.installationDate = command.installationDate();
        this.status = command.status();
    }

    public boolean isDecommissioned() {
        return this.status == EquipmentStatus.DECOMMISSIONED;
    }

    public static CriticalEquipment reconstitute(Long id, Long buildingId, EquipmentCode code, String name,
                                                 EquipmentType type, String location, LocalDate installationDate,
                                                 EquipmentStatus status) {
        var equipment = new CriticalEquipment();
        equipment.id = id;
        equipment.buildingId = buildingId;
        equipment.code = code;
        equipment.name = name;
        equipment.type = type;
        equipment.location = location;
        equipment.installationDate = installationDate;
        equipment.status = status;
        return equipment;
    }
}
