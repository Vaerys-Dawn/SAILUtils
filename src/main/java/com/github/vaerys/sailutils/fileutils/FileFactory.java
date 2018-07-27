package com.github.vaerys.sailutils.fileutils;


import com.github.vaerys.sailutils.enums.FilePath;
import com.github.vaerys.sailutils.handlers.FileHandler;
import com.github.vaerys.sailutils.utils.ErrorUtils;

import java.io.IOException;

public class FileFactory {


    public static <T extends JsonFile> T create(long guildID, String newPath, Class object) {
        T t;
        String dirGuild = FilePath.DIR_STORAGE + (guildID + "/");
        FileHandler.createDirectory(dirGuild);
        String path = dirGuild + newPath;
        if (!FileHandler.exists(path)) {
            t = (T) getObject(object);
            FileHandler.writeToJson(path, t);
        } else {
            t = (T) FileHandler.readFromJson(path, object);
        }
        if (t == null) {
            ErrorUtils.sendStack(new IOException("File is corrupt: " + path));
        }
        t.setPath(guildID + "/" + newPath);
        return t;
    }

    private static void checkClass(Class object) {
        if (object.isInstance(JsonFile.class)){
            throw new IllegalArgumentException(String.format("%s is not subClass of %s", object.getName(), JsonFile.class.getName()));
        }
    }

    public static <T extends JsonFile> T create(String newPath, Class<?> object) {
        T t;
        String path = FilePath.DIR_STORAGE + "/" + newPath;
        if (!FileHandler.exists(path)) {
            t = (T) getObject(object);
            FileHandler.writeToJson(path, t);
        } else {
            t = (T) FileHandler.readFromJson(path, object);
        }
        if (t == null) {
            ErrorUtils.sendStack(new IOException("File is corrupt: " + path));
        }
        t.setPath(newPath);
        return t;
    }

    public static Object getObject(Class<?> object) {
        try {
            return object.getConstructor().newInstance();
        } catch (Exception e) {
            ErrorUtils.sendStack(e);
        }
        return null;
    }
}
