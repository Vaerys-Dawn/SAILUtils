package com.github.vaerys.sailutils.discordutils.wrappers;

import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

public class UserObject {
    private final User user;
    private final EventObject event;

    public UserObject(User user, EventObject event) {
        this.user = user;
        this.event = event;
    }

    public UserObject(Mono<User> user, EventObject event) {
        this.user = user.block();
        this.event = event;
    }

    public User get() {
        return user;
    }

    public EventObject getEvent() {
        return event;
    }
}
