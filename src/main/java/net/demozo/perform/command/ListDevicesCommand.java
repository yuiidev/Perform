package net.demozo.perform.command;

import net.demozo.perform.Perform;

import javax.sound.midi.MidiSystem;

public class ListDevicesCommand implements Command {
    @Override
    public void handle(String value) {
        var info = MidiSystem.getMidiDeviceInfo();

        Perform.LOGGER.info("Available devices:");

        for(var deviceInfo : info) {
            Perform.LOGGER.info("\"%s\" - %s".formatted(deviceInfo.getName(), deviceInfo.getVendor()));
        }
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "list";
    }
}
