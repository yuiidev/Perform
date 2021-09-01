package net.demozo.perform.command;

public interface Command {

    void handle(String value);
    String getDescription();
    String getName();

}
