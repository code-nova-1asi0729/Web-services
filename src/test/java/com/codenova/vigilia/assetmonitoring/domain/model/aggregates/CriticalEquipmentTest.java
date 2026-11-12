package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CriticalEquipmentTest {

    private CriticalEquipment newEquipment() {
        return new CriticalEquipment(new RegisterEquipmentCommand(1L, new EquipmentCode("LP-WP-01"),
                "Bomba hidroneumática 1", EquipmentType.WATER_PUMP, "Sótano 1", LocalDate.of(2021, 3, 15)));
    }

    @Test
    void isRegisteredAsOperational() {
        var equipment = newEquipment();
        assertEquals(EquipmentStatus.OPERATIONAL, equipment.getStatus());
        assertEquals("LP-WP-01", equipment.getCode().value());
        assertFalse(equipment.isDecommissioned());
    }

    @Test
    void canBeModified() {
        var equipment = newEquipment();
        equipment.update(new ModifyEquipmentCommand(1L, "Bomba hidroneumática principal", EquipmentType.WATER_PUMP,
                "Sótano 2", LocalDate.of(2021, 3, 15), EquipmentStatus.REQUIRES_FOLLOW_UP));
        assertEquals("Sótano 2", equipment.getLocation());
        assertEquals(EquipmentStatus.REQUIRES_FOLLOW_UP, equipment.getStatus());
    }

    @Test
    void isDecommissionedWhenItsStatusChangesToDecommissioned() {
        var equipment = newEquipment();
        equipment.update(new ModifyEquipmentCommand(1L, "Bomba hidroneumática 1", EquipmentType.WATER_PUMP,
                "Sótano 1", LocalDate.of(2021, 3, 15), EquipmentStatus.DECOMMISSIONED));
        assertTrue(equipment.isDecommissioned());
    }

    @Test
    void cannotBeRegisteredWithAFutureInstallationDate() {
        assertThrows(IllegalArgumentException.class, () -> new RegisterEquipmentCommand(1L,
                new EquipmentCode("LP-WP-02"), "Bomba 2", EquipmentType.WATER_PUMP, "Sótano 1",
                LocalDate.now().plusDays(1)));
    }
}