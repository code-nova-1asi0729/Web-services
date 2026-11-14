package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.UpdateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingStatus;
import com.codenova.vigilia.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

/**
 * Residential building whose devices Vigilia monitors.
 */
@Getter
public class Building extends AbstractDomainAggregateRoot<Building> {

    private Long id;
    private String name;
    private BuildingCode code;
    private String address;
    private String district;
    private Integer totalUnits;
    private BuildingStatus status;

    private Building() {}

    public Building(RegisterBuildingCommand command) {
        this.name = command.name();
        this.code = command.code();
        this.address = command.address();
        this.district = command.district();
        this.totalUnits = command.totalUnits();
        this.status = BuildingStatus.ACTIVE;
    }

    public static Building reconstitute(Long id, String name, BuildingCode code, String address,
                                        String district, Integer totalUnits, BuildingStatus status) {
        var building = new Building();
        building.id = id;
        building.name = name;
        building.code = code;
        building.address = address;
        building.district = district;
        building.totalUnits = totalUnits;
        building.status = status;
        return building;
    }

    public void update(UpdateBuildingCommand command) {
        this.name = command.name();
        this.address = command.address();
        this.district = command.district();
        this.totalUnits = command.totalUnits();
        this.status = command.status();
    }
}
