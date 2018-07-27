package com.github.vaerys.sailutils.discordutils;

import com.github.vaerys.sailutils.commandutils.AbstractCommand;
import com.github.vaerys.sailutils.commandutils.MessageCommandHandler;
import com.github.vaerys.sailutils.discordutils.wrappers.EventObject;
import com.github.vaerys.sailutils.discordutils.wrappers.GuildAssist;
import com.github.vaerys.sailutils.enums.ExitCode;
import com.github.vaerys.sailutils.enums.FilePath;
import com.github.vaerys.sailutils.fileutils.JsonFile;
import com.github.vaerys.sailutils.handlers.FileHandler;
import com.github.vaerys.sailutils.utils.ErrorUtils;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class DiscordBot {

    final static Logger logger = LoggerFactory.getLogger(DiscordBot.class);

    private static List<JsonFile> files = new LinkedList<>();

    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static boolean savingFiles = false;
    private static final List<AbstractCommand> commandList = new ArrayList<>();
    private static Optional<Class<? extends GuildAssist>> guildAssistClass = Optional.empty();
    private static String defaultPrefix = "^";

    /***
     * Implementation for starting up your new discord bot.
     */
    public static void run() {
        DiscordBot discordBot = getBotClass();

        FileHandler.createDirectory(FilePath.DIR_STORAGE.toString());
        try {
            Client.createClient();
        } catch (IndexOutOfBoundsException e) {
            logger.error("Missing Token. Please Enter your token within \"" + FilePath.FILE_TOKEN + "\".");
            System.exit(ExitCode.STOP.get());
        } catch (ClientCreationException e) {
            logger.error("Invalid Token. Please Update your token within \"" + FilePath.FILE_TOKEN + "\".");
        }

        Instant timeNow = Instant.now();
        scheduledSave(timeNow);
        scheduledBackup(timeNow);
        initSaveOnExit();
        //register command handler
        Client.getClient().getEventDispatcher().on(MessageCreateEvent.class).subscribe(e -> MessageCommandHandler.handleCommand(e));
        discordBot.start(Client.getClient());
    }

    private static void initSaveOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info(">>> Running Shutdown Process <<<");
            if (savingFiles) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ErrorUtils.sendStack(e);
                }
            }
            saveAll();
        }));
    }

    /***
     * Saves all files within the files array every 5 mins.
     * @param timeNow the current time.
     */
    private static void scheduledSave(Instant timeNow) {
        ZonedDateTime time = timeNow.atZone(ZoneOffset.UTC);
        int diff = time.getMinute() % 5;
        if (diff == 0) diff = 5;
        else diff = 5 - diff;
        time = time.withSecond(0).withNano(0).plusMinutes(diff);
        long initialTime = time.toEpochSecond() - Instant.now().atZone(ZoneOffset.UTC).toEpochSecond();
        scheduler.scheduleAtFixedRate(() -> {
            saveAll();
        }, initialTime, TimeUnit.MINUTES.toSeconds(5), TimeUnit.SECONDS);
    }

    private static void saveAll() {
        logger.info("Saving files.");
        files.forEach(f -> f.save());
    }

    /***
     * Backs up all files within the files array every 24 hours.
     * @param timeNow the current time.
     */
    private static void scheduledBackup(Instant timeNow) {
        ZonedDateTime time = timeNow.atZone(ZoneOffset.UTC);
        time = time.withSecond(0).withNano(0).withMinute(1).withHour(0).plusDays(1);
        long initialTime = time.toEpochSecond() - timeNow.atZone(ZoneOffset.UTC).toEpochSecond();
        scheduler.scheduleAtFixedRate(() -> {
            logger.info("Backing up files.");
            files.forEach(f -> f.backUp());
        }, initialTime, TimeUnit.HOURS.toSeconds(24), TimeUnit.MINUTES);
    }

    public static DiscordBot getBotClass() {
        StackTraceElement[] cause = Thread.currentThread().getStackTrace();

        boolean foundThisMethod = false;
        String callingClassName = null;
        for (StackTraceElement element : cause) {
            // Skip entries until we get to the entry for this class
            String className = element.getClassName();
            String methodName = element.getMethodName();
            if (foundThisMethod) {
                callingClassName = className;
                break;
            } else if (DiscordBot.class.getName().equals(className) && "run".equals(methodName)) {
                foundThisMethod = true;
            }
        }

        if (callingClassName == null) {
            throw new RuntimeException("Error: unable to determine main class");
        }

        try {
            Class botClass = Class.forName(callingClassName, false,
                    Thread.currentThread().getContextClassLoader());
            if (DiscordBot.class.isAssignableFrom(botClass)) {
                return (DiscordBot) botClass.getConstructor().newInstance();
            } else {
                throw new RuntimeException("Error: " + botClass
                        + " is not a valid Subclass");
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void addFile(JsonFile file) {
        if (files.contains(file)) return;
        files.add(file);
    }

    public static void registerCommand(AbstractCommand command) {
        commandList.add(command);
    }

    public static List<AbstractCommand> getCommands() {
        return commandList;
    }

    public static GuildAssist getGuildAssist(Guild guild, EventObject event) {
        if (guildAssistClass.isPresent()) {
            try {
                return guildAssistClass.get().getConstructor(guild.getClass(), event.getClass()).newInstance(guild, event);
            } catch (Exception e){
                throw new IllegalArgumentException(e);
            }
        }
        return new GuildAssist(guild, event);
    }

    public static void setGuildAssistClass(Class<? extends GuildAssist> assistClass) {
        guildAssistClass = Optional.of(assistClass);
    }

    public static String getDefaultPrefix() {
        return defaultPrefix;
    }

    public abstract void start(DiscordClient client);

}
