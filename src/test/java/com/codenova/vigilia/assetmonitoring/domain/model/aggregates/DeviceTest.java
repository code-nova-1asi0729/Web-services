package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyDeviceCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterDeviceCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    private Device newDevice() {
        return new Device(new RegisterDeviceCommand(1L, new DeviceCode("LP-WP-01"),
                "Bomba hidroneumática 1", DeviceType.WATER_PUMP, "Sótano 1", LocalDate.of(2021, 3, 15)));
    }

    @Test
    void isRegisteredAsOperational() {
        var device = newDevice();
        assertEquals(DeviceStatus.OPERATIONAL, device.getStatus());
        assertEquals("LP-WP-01", device.getCode().value());
        assertFalse(device.isDecommissioned());
    }

    @Test
    void canBeModified() {
        var device = newDevice();
        device.update(new ModifyDeviceCommand(1L, "Bomba hidroneumática principal", DeviceType.WATER_PUMP,
                "Sótano 2", LocalDate.of(2021, 3, 15), DeviceStatus.REQUIRES_FOLLOW_UP));
        assertEquals("Sótano 2", device.getLocation());
        assertEquals(DeviceStatus.REQUIRES_FOLLOW_UP, device.getStatus());
    }

    @Test
    void isDecommissionedWhenItsStatusChangesToDecommissioned() {
        var device = newDevice();
        device.update(new ModifyDeviceCommand(1L, "Bomba hidroneumática 1", DeviceType.WATER_PUMP,
                "Sótano 1", LocalDate.of(2021, 3, 15), DeviceStatus.DECOMMISSIONED));
        assertTrue(device.isDecommissioned());
    }

    @Test
    void cannotBeRegisteredWithAFutureInstallationDate() {
        assertThrows(IllegalArgumentException.class, () -> new RegisterDeviceCommand(1L,
                new DeviceCode("LP-WP-02"), "Bomba 2", DeviceType.WATER_PUMP, "Sótano 1",
                LocalDate.now().plusDays(1)));
    }
}