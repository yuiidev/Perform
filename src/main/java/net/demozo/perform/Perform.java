package net.demozo.perform;

import net.demozo.perform.command.Command;
import net.demozo.perform.command.DeviceCommand;
import net.demozo.perform.command.ListDevicesCommand;
import net.demozo.perform.exception.CommandNotFoundException;
import net.demozo.perform.exception.PrintInsteadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Perform {
    public static final Logger LOGGER;
    private final List<Command> commandList;

    static {
        LOGGER = LoggerFactory.getLogger(Perform.class);
    }

    public static void main(String[] args) {
        LOGGER.info("Perform - by Ren Lucodi (Louisoix)");
        var perform = new Perform();

        try {
            perform.run(args);
        } catch (PrintInsteadException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public Perform() {
        commandList = new ArrayList<>();
        commandList.add(new ListDevicesCommand());
        commandList.add(new DeviceCommand());
    }

    private void run(String[] args) {
        for(var arg : args) {
            var split = arg.split("=");
            var name = split[0].substring(2);
            var value = split.length >= 2 ? split[1] : null;

            var commandToHandle = commandList.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElseThrow(() -> new CommandNotFoundException(name));
            commandToHandle.handle(value);
        }
    }

    public List<Command> getCommandList() {
        return commandList;
    }
}
