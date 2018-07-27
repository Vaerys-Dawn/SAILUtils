package com.github.vaerys.sailutils.commandutils;

import com.github.vaerys.sailutils.discordutils.wrappers.EventObject;
import discord4j.core.spec.MessageCreateSpec;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand {

    public final List<String> names;

    public abstract String[] names();

    public AbstractCommand() {
        this.names = Arrays.asList(names());
        names.forEach(n -> {
            if (n.matches("(?i)/w")) throw new IllegalArgumentException("Name values must be alphanumeric.");
        });
    }

    /***
     * Standard command handler.
     *
     * @param event data derived from the messageCreateEvent
     * @return the payload to be sent.
     */
    public abstract void handleCommand(EventObject event, MessageCreateSpec message);


    /***
     * tests to see if the command is being run.
     *
     * @param event the event data
     * @return if event message starts with command;
     */
    public boolean isCommand(EventObject event) {
        String prefix = event.guild.getPrefix();
        String commandCall = event.getFirstWord();
        for (String s : names) {
            if (String.format("%s%s", prefix, s).equalsIgnoreCase(commandCall)) return true;
        }
        return false;
    }

    public String getArguments(EventObject event) {
        return StringUtils.replaceOnce(event.message.content, String.format("%s ", event.getFirstWord()), "");
    }
}
