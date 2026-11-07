package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentType;
import com.codenova.vigilia.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Critical equipment of a building, such as a water pump or an elevator, monitored by Vigilia.
 */
@Getter
public class CriticalEquipment extends AbstractDomainAggregateRoot<CriticalEquipment> {

    private Long id;
    private Long buildingId;
    private EquipmentCode code;
    private String name;
    private EquipmentType type;
    private String location;
    private LocalDate installationDate;
    private EquipmentStatus status;

    private CriticalEquipment() {}

    public CriticalEquipment(RegisterEquipmentCommand command) {
        this.buildingId = command.buildingId();
        this.code = command.code();
        this.name = command.name();
        this.type = command.type();
        this.location = command.location();
        this.installationDate = command.installationDate();
        this.status = EquipmentStatus.OPERATIONAL;
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
}
