package com.github.vaerys.sailutils.commandutils;

import com.github.vaerys.sailutils.discordutils.DiscordBot;
import com.github.vaerys.sailutils.discordutils.wrappers.EventObject;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;

import java.util.List;

public class MessageCommandHandler {

    public static void handleCommand(MessageCreateEvent e) {
        Mono<EventObject> event = EventObject.of(e);
        List<AbstractCommand> commands = DiscordBot.getCommands();
        event.subscribe(eventObject -> {
            if (eventObject.user.get().isBot()) return;
            for (AbstractCommand command : commands) {
                MessageCreateSpec message = new MessageCreateSpec();
                if (command.isCommand(eventObject)) {
                    command.handleCommand(eventObject, message);
                    eventObject.channel.sendMessage(message);
                    break;
                }
            }
        });
    }
}
