package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingStatus;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters.BuildingCodeAttributeConverter;
import com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity that maps the buildings table.
 */
@Entity
@Table(name = "buildings")
@Getter
@Setter
@NoArgsConstructor
public class BuildingPersistenceEntity extends AuditableAbstractPersistenceEntity {

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
}
