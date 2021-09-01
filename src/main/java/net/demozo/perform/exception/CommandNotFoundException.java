package net.demozo.perform.exception;

public class CommandNotFoundException extends PrintInsteadException{
    public CommandNotFoundException(String commandName) {
        super("Could not find command with name \"%s\"".formatted(commandName));
    }
}
