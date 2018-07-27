package com.github.vaerys.sailutils.discordutils.wrappers;

import discord4j.core.DiscordClient;

public class ClientObject {
    private final DiscordClient client;
//    private final UserObject botUser;
    public final EventObject event;

    public ClientObject(DiscordClient client, EventObject event) {
        this.client = client;
//        this.botUser = new UserObject(client.getSelf(), event);
        this.event = event;
    }

    public DiscordClient get() {
        return client;
    }

//    public UserObject getBotUser() {
//        return botUser;
//    }

    public EventObject getEvent() {
        return event;
    }
}
