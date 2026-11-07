package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentType;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters.EquipmentCodePersistenceConverter;
import com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * JPA entity that maps the critical_equipments table.
 */
@Entity
@Table(name = "critical_equipments", uniqueConstraints = @UniqueConstraint(columnNames = {"buildingId", "code"}))
@Getter
@Setter
@NoArgsConstructor
public class EquipmentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private Long buildingId;

    @Column(nullable = false)
    @Convert(converter = EquipmentCodePersistenceConverter.class)
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
}
