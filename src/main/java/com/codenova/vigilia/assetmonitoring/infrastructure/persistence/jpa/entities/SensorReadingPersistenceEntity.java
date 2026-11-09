package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.ReadingStatus;
import com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity that maps the sensor_readings table.
 */
@Entity
@Table(name = "sensor_readings")
@Getter
@Setter
@NoArgsConstructor
public class SensorReadingPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private Long sensorId;

    @Column(nullable = false)
    private Long equipmentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MetricType metric;

    // "value" is an SQL keyword, so the column has another name
    @Column(name = "reading_value", nullable = false)
    private Double value;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReadingStatus status;

    @Column(nullable = false)
    private Instant recordedAt;
}
