package com.codenova.vigilia.incidents.domain.model.aggregates;

import com.codenova.vigilia.incidents.domain.model.commands.ReportIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentCategory;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentStatus;
import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
import com.codenova.vigilia.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.Instant;
/**
 * Problem reported by a resident and followed until it is resolved and rated
 */
@Getter
public class Incident extends AbstractDomainAggregateRoot<Incident> {

    private Long id;
    private Long buildingId;
    private Long residentId;
    private Long equipmentId;
    private IncidentCategory category;
    private String description;
    private IncidentStatus status;
    private RatingScore ratingScore;
    private String ratingComment;
    private Instant reportedAt;
    private Instant startedAt;
    private Instant resolvedAt;
    private Instant ratedAt;

    private Incident() {}

    public Incident(ReportIncidentCommand command) {
        this.buildingId = command.buildingId();
        this.residentId = command.residentId();
        this.equipmentId = command.equipmentId();
        this.category = command.category();
        this.description = command.description();
        this.status = IncidentStatus.REPORTED;
        this.reportedAt = Instant.now();
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
}
