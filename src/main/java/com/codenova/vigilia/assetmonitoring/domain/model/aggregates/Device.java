package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyDeviceCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterDeviceCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceType;
import com.codenova.vigilia.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Critical device of a building, such as a water pump or an elevator, monitored by Vigilia.
 */
@Getter
public class Device extends AbstractDomainAggregateRoot<Device> {

    private Long id;
    private Long buildingId;
    private DeviceCode code;
    private String name;
    private DeviceType type;
    private String location;
    private LocalDate installationDate;
    private DeviceStatus status;

    private Device() {}

    public Device(RegisterDeviceCommand command) {
        this.buildingId = command.buildingId();
        this.code = command.code();
        this.name = command.name();
        this.type = command.type();
        this.location = command.location();
        this.installationDate = command.installationDate();
        this.status = DeviceStatus.OPERATIONAL;
    }

    public static Device reconstitute(Long id, Long buildingId, DeviceCode code, String name,
                                      DeviceType type, String location, LocalDate installationDate,
                                      DeviceStatus status) {
        var device = new Device();
        device.id = id;
        device.buildingId = buildingId;
        device.code = code;
        device.name = name;
        device.type = type;
        device.location = location;
        device.installationDate = installationDate;
        device.status = status;
        return device;
    }

    public void update(ModifyDeviceCommand command) {
        this.name = command.name();
        this.type = command.type();
        this.location = command.location();
        this.installationDate = command.installationDate();
        this.status = command.status();
    }

    public boolean isDecommissioned() {
        return this.status == DeviceStatus.DECOMMISSIONED;
    }
}
