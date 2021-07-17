package ch.bbcag.mdriver;

import javafx.scene.control.Dialog;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Launcher {

    public static void main(String[] args) {
        exportDLL();
        setGlobalExceptionHandler();
        App.launch(App.class);
    }

    private static void setGlobalExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Something went wrong");
            dialog.setHeaderText(t.getName());
            dialog.setContentText(e.getMessage());
            dialog.show();
        });
    }

    private static void exportDLL() {
        String[] files = {
                "jinput-dx8_64.dll",
                "jinput-raw_64.dll"
        };

        Path tmpDir = null;
        try {
            tmpDir = Files.createTempDirectory("md-lib-");
            new File(String.valueOf(tmpDir)).deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String file : files) {
            File tmp = new File(String.valueOf(tmpDir), file);
            try {
                tmp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            tmp.deleteOnExit();
            try {
                InputStream from = Launcher.class.getResourceAsStream("/lib/" + file);
                OutputStream to = new FileOutputStream(tmp);
                if (from != null) {
                    IOUtils.copy(from, to);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.setProperty("net.java.games.input.librarypath", String.valueOf(Objects.requireNonNull(tmpDir).toAbsolutePath()));
    }
}