package com.codenova.vigilia.incidents.domain.model.aggregates;

import com.codenova.vigilia.incidents.domain.model.commands.ReportIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentCategory;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentStatus;
import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.converters.RatingScoreAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
/**
 * Problem reported by a resident and followed until it is resolved and rated
 */
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Incident extends AbstractAggregateRoot<Incident> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long buildingId;

    @Column(nullable = false)
    private Long residentId;

    private Long equipmentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentCategory category;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Convert(converter = RatingScoreAttributeConverter.class)
    private RatingScore ratingScore;

    @Column(length = 300)
    private String ratingComment;

    @Column(nullable = false)
    private Instant reportedAt;

    private Instant startedAt;

    private Instant resolvedAt;

    private Instant ratedAt;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    protected Incident() {}

    public Incident(ReportIncidentCommand command) {
        this.buildingId = command.buildingId();
        this.residentId = command.residentId();
        this.equipmentId = command.equipmentId();
        this.category = command.category();
        this.description = command.description();
        this.status = IncidentStatus.REPORTED;
        this.reportedAt = Instant.now();
    }

    public boolean start() {
        if (status != IncidentStatus.REPORTED) return false;
        this.status = IncidentStatus.IN_PROGRESS;
        this.startedAt = Instant.now();
        return true;
    }

    public boolean resolve() {
        if (status == IncidentStatus.RESOLVED) return false;
        this.status = IncidentStatus.RESOLVED;
        this.resolvedAt = Instant.now();
        return true;
    }

    public boolean changeStatus(IncidentStatus newStatus) {
        if (newStatus == status) return true;
        return switch (newStatus) {
            case REPORTED -> false;
            case IN_PROGRESS -> start();
            case RESOLVED -> resolve();
        };
    }

    public boolean rate(RatingScore score, String comment) {
        if (status != IncidentStatus.RESOLVED || isRated()) return false;
        this.ratingScore = score;
        this.ratingComment = comment;
        this.ratedAt = Instant.now();
        return true;
    }

    public boolean isRated() {
        return ratingScore != null;
    }

    public static Incident reconstitute(Long id, Long buildingId, Long residentId, Long equipmentId,
                                        IncidentCategory category, String description, IncidentStatus status,
                                        RatingScore ratingScore, String ratingComment, Instant reportedAt,
                                        Instant startedAt, Instant resolvedAt, Instant ratedAt) {
        var incident = new Incident();
        incident.id = id;
        incident.buildingId = buildingId;
        incident.residentId = residentId;
        incident.equipmentId = equipmentId;
        incident.category = category;
        incident.description = description;
        incident.status = status;
        incident.ratingScore = ratingScore;
        incident.ratingComment = ratingComment;
        incident.reportedAt = reportedAt;
        incident.startedAt = startedAt;
        incident.resolvedAt = resolvedAt;
        incident.ratedAt = ratedAt;
        return incident;
    }
}
