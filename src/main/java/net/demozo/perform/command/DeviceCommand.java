package net.demozo.perform.command;

import net.demozo.perform.MidiController;
import net.demozo.perform.Perform;
import net.demozo.perform.exception.DeviceNotFoundException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class DeviceCommand implements Command{
    @Override
    public void handle(String value) {
        Perform.LOGGER.info("Attempting connection to \"%s\"".formatted(value));
        var deviceInfos = Arrays.stream(MidiSystem.getMidiDeviceInfo()).filter(info -> info.getName().toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT))).toList();
        var validDevices = new ArrayList<MidiDevice>();

        try {
            for(var deviceInfo : deviceInfos) {
                var device = MidiSystem.getMidiDevice(deviceInfo);

                if(device.getMaxTransmitters() != 0) {
                    validDevices.add(device);
                } else {
                    Perform.LOGGER.debug("%s no max transmitters, but getTransmitters size is %s".formatted(deviceInfo.getName(), device.getTransmitters().size()));
                }
            }

            if(validDevices.size() == 0) {
                throw new DeviceNotFoundException(value);
            }

            var device = validDevices.get(0);
            var midiController = new MidiController();

            Perform.LOGGER.info("Established connection to \"%s\"".formatted(device.getDeviceInfo().getName()));
            midiController.listen(device);
        } catch (MidiUnavailableException e) {
            Perform.LOGGER.error(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getName() {
        return "device";
    }
}
