package com.github.vaerys.sailutils.discordutils.wrappers;

import com.github.vaerys.sailutils.discordutils.Client;
import com.github.vaerys.sailutils.discordutils.DiscordBot;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class EventObject{
    public MessageObject message;
    public ChannelObject channel;
    public GuildAssist guild;
    public UserObject user;
    public ClientObject client;


    private EventObject(Message message, MessageChannel channel, User user, Optional<Guild> guild) {
        this.message = new MessageObject(message, this);
        this.channel = new ChannelObject(channel, this);
        this.user = new UserObject(user, this);
        this.guild = guild.isPresent() ? DiscordBot.getGuildAssist(guild.get(), this) : new PrivateGuildAssist(this);
        this.client = new ClientObject(Client.getClient(), this);
    }

    public static Mono<EventObject> of(MessageCreateEvent event) {
        Message message = event.getMessage();
        return message.getChannel().flatMap(channel -> message.getAuthor()
                .flatMap(user -> message.getGuild()
                        .map(guild -> new EventObject(message, channel, user, Optional.of(guild)))
                        .switchIfEmpty(Mono.just(new EventObject(message, channel, user, Optional.empty())))));
    }


    public EventObject(EventObject event) {
        this.message = event.message;
        this.channel = event.channel;
        this.user = event.user;
        this.guild = event.guild;
        this.client = event.client;
    }

    public String getFirstWord() {
        if (message.content.isEmpty()) return "";
        return message.content.split(" +")[0];
    }

}
