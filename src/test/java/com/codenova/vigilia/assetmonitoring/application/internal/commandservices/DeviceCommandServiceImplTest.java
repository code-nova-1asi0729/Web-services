package com.codenova.vigilia.assetmonitoring.application.internal.commandservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyDeviceCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceType;
import com.codenova.vigilia.assetmonitoring.domain.repositories.DeviceRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DeviceCommandServiceImplTest {

    private static class FakeDeviceRepository implements DeviceRepository {

        private final Device device;

        FakeDeviceRepository(Device device) {
            this.device = device;
        }

        @Override
        public List<Device> findAll() {
            return List.of(device);
        }

        @Override
        public List<Device> findAllByBuildingId(Long buildingId) {
            return List.of(device);
        }

        @Override
        public Optional<Device> findById(Long id) {
            return Optional.of(device);
        }

        @Override
        public boolean existsByBuildingIdAndCode(Long buildingId, DeviceCode code) {
            return false;
        }

        @Override
        public Device save(Device device) {
            return device;
        }
    }

    @Test
    void cannotModifyADecommissionedDevice() {
        var device = Device.reconstitute(1L, 1L, new DeviceCode("LP-WP-01"), "Bomba hidroneumática 1",
                DeviceType.WATER_PUMP, "Sótano 1", LocalDate.of(2021, 3, 15), DeviceStatus.DECOMMISSIONED);
        var service = new DeviceCommandServiceImpl(new FakeDeviceRepository(device), null);
        var command = new ModifyDeviceCommand(1L, "Bomba hidroneumática principal", DeviceType.WATER_PUMP,
                "Sótano 2", LocalDate.of(2021, 3, 15), DeviceStatus.OPERATIONAL);

        var result = service.handle(command);

        assertFalse(result.isSuccess());
        assertEquals("device.error.alreadyDecommissioned", result.fold(saved -> "", failure -> failure.messageKey()));
        assertEquals("Sótano 1", device.getLocation());
    }
}