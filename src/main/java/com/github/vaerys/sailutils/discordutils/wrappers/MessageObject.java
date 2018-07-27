package com.github.vaerys.sailutils.discordutils.wrappers;

import discord4j.core.object.entity.Message;

public class MessageObject {
    private final Message message;
    private final EventObject event;
    public String content = "";

    public MessageObject(Message message, EventObject event) {
        this.message = message;
        this.event = event;
        if (message.getContent().isPresent()) content = message.getContent().get();
    }

    public Message get() {
        return message;
    }

    public EventObject getEvent() {
        return event;
    }
}
