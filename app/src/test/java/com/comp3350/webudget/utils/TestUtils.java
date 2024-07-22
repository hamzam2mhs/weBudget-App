package com.comp3350.webudget.utils;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

import com.comp3350.webudget.application.Main;

public class TestUtils {
    private static final File DB_SRC = new File("src/main/assets/db/myDB.script");

    public static File copyDB() throws IOException {
        final File target = File.createTempFile("temp-db", ".script");
        Files.copy(DB_SRC, target);
        Main.setDBPathName(target.getAbsolutePath().replace(".script", ""));
        System.out.println(target.getAbsolutePath());
        return target;
    }
}
