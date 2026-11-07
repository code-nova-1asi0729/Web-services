package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertSeverity;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
import com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity that maps the alerts table.
 */
@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
public class AlertPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private Long equipmentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MetricType metric;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    // last_value is a reserved word in MySQL 8, so the column uses another name
    @Column(name = "last_reading_value", nullable = false)
    private Double lastValue;

    @Column(nullable = false)
    private Instant detectedAt;

    private Instant resolvedAt;
}
