package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SensorStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SerialNumber;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters.SerialNumberPersistenceConverter;
import com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity that maps the sensors table.
 */
@Entity
@Table(name = "sensors")
@Getter
@Setter
@NoArgsConstructor
public class SensorPersistenceEntity extends AuditableAbstractPersistenceEntity {

    // Null while the sensor is not assigned to an equipment
    private Long equipmentId;

    @Column(nullable = false, unique = true)
    @Convert(converter = SerialNumberPersistenceConverter.class)
    private SerialNumber serialNumber;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SensorStatus status;

    private Instant lastSeenAt;
}
