package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceType;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters.DeviceCodePersistenceConverter;
import com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * JPA entity that maps the devices table.
 */
@Entity
@Table(name = "devices", uniqueConstraints = @UniqueConstraint(columnNames = {"buildingId", "code"}))
@Getter
@Setter
@NoArgsConstructor
public class DevicePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private Long buildingId;

    @Column(nullable = false)
    @Convert(converter = DeviceCodePersistenceConverter.class)
    private DeviceCode code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate installationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceStatus status;
}
