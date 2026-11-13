package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.entities;

import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentCategory;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentStatus;
import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.converters.RatingScorePersistenceConverter;
import com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity that maps the incidents table.
 */
@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor
public class IncidentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private Long buildingId;

    @Column(nullable = false)
    private Long residentId;

    private Long deviceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentCategory category;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Convert(converter = RatingScorePersistenceConverter.class)
    private RatingScore ratingScore;

    @Column(length = 300)
    private String ratingComment;

    @Column(nullable = false)
    private Instant reportedAt;

    private Instant startedAt;

    private Instant resolvedAt;

    private Instant ratedAt;
}
