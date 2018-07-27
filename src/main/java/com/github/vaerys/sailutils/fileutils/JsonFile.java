package com.github.vaerys.sailutils.fileutils;

import com.github.vaerys.sailutils.enums.FilePath;
import com.github.vaerys.sailutils.handlers.FileHandler;
import com.github.vaerys.sailutils.utils.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class JsonFile {
    final static Logger logger = LoggerFactory.getLogger(JsonFile.class);
    public transient String path;
    public transient String backupPath;

    public void save() {
        FileHandler.writeToJson(path, this);
    }

    public void backUp() {
        try {
            File backup1 = new File(backupPath + 1);
            File backup2 = new File(backupPath + 2);
            File backup3 = new File(backupPath + 3);
            File toBackup = new File(path);
            if (backup3.exists()) backup3.delete();
            if (backup2.exists()) backup2.renameTo(backup3);
            if (backup1.exists()) backup1.renameTo(backup2);
            if (toBackup.exists())
                Files.copy(Paths.get(toBackup.getPath()), backup1.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.trace(this.getClass().getName() + " - File Backed up.");
        } catch (IOException e) {
            ErrorUtils.sendStack(e);
        }
    }

    public void setPath(String newPath) {
        path = FilePath.DIR_STORAGE + newPath;
        backupPath = FilePath.DIR_BACKUP + newPath;
    }
}
