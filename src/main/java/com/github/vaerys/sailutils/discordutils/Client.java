package com.github.vaerys.sailutils.discordutils;

import com.github.vaerys.sailutils.databaseutils.DataHandler;
import com.github.vaerys.sailutils.enums.FilePath;
import com.github.vaerys.sailutils.handlers.FileHandler;
import discord4j.core.ClientBuilder;
import discord4j.core.DiscordClient;
import discord4j.core.object.presence.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    final static Logger logger = LoggerFactory.getLogger(Client.class);
    private static DiscordClient client = null;
    private static String clientToken;
    private static Presence initialPresence = Presence.online();

    public static DiscordClient createClient(String token) throws ClientCreationException {
        ClientBuilder clientBuilder = new ClientBuilder(token);
        clientToken = token;
        clientBuilder.setInitialPresence(initialPresence);
        client = clientBuilder.build();
        if (client == null) throw new ClientCreationException("Invalid Token");
        return client;
    }

    public static DiscordClient getClient() {
        return client;
    }

    public static String getClientToken() {
        return clientToken;
    }

    public static DiscordClient createClient() throws ClientCreationException {
        FileHandler.createDirectory(FilePath.DIR_STORAGE.toString());
        return createClient(FileHandler.readFromFile(FilePath.FILE_TOKEN.toString()).get(0));
    }

    public static void setInitialPresence(Presence presence){
        initialPresence = presence;
    }
}
