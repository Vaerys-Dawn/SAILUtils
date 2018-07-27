package com.github.vaerys.sailutils.discordutils.wrappers;

import com.github.vaerys.sailutils.discordutils.DiscordBot;
import discord4j.core.object.entity.Guild;

// TODO: 20/05/2018 change this to a guild assist system that is abstract

public class GuildAssist {
    private final Guild guild;
    private final EventObject event;
    private String prefix;

    public GuildAssist(Guild guild, EventObject event) {
        this.guild = guild;
        this.event = event;
        this.prefix = DiscordBot.getDefaultPrefix();
    }

    public Guild get() {
        return guild;
    }

    public EventObject getEvent() {
        return event;
    }

    public String getPrefix() {
        return prefix;
        // TODO: 20/05/2018 do something about this
    }
}
