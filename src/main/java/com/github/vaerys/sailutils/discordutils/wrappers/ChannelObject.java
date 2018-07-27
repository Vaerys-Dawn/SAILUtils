package com.github.vaerys.sailutils.discordutils.wrappers;

import discord4j.core.object.entity.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;

public class ChannelObject {
    private final MessageChannel channel;
    private final EventObject event;

    public ChannelObject(MessageChannel channel, EventObject event) {
        this.channel = channel;
        this.event = event;
    }

    public MessageChannel get() {
        return channel;
    }

    public EventObject getEvent() {
        return event;
    }

    public void sendMessage(String content) {
        channel.createMessage(new MessageCreateSpec().setContent(content)).subscribe();
    }

    public void sendMessage(MessageCreateSpec message) {
        channel.createMessage(message).subscribe();
    }
}
