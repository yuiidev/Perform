package net.demozo.perform.exception;

public class DeviceNotFoundException extends PrintInsteadException {
    public DeviceNotFoundException(String deviceName) {
        super("Device with name \"%s\" not found".formatted(deviceName));
    }
}
