package ch.bbcag.mdriver.record;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.common.DriveMessage;
import ch.bbcag.mdriver.common.Message;
import ch.bbcag.mdriver.common.Record;
import ch.bbcag.mdriver.common.ShootMessage;
import ch.bbcag.mdriver.connection.SendListener;
import com.google.gson.Gson;
import javafx.scene.control.Dialog;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class RecordManager implements SendListener {

    public static final File RECORDINGS_DIRECTORY = Paths.get(System.getProperty("user.home"), "Recordings").toFile();
    public static final String FILE_NAME = "record.json";
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ssSSS");

    private boolean isRecording;

    private Record record;

    public RecordManager() {
        if (!RECORDINGS_DIRECTORY.exists()) {
            if (!RECORDINGS_DIRECTORY.mkdirs()) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Ein Fehler ist aufgetreten");
                dialog.setContentText("Das Verzeichnis für die Aufnahmen konnte nicht erstellt werden.");
                dialog.show();
            }
        }
    }

    public static void deleteRecord(Record record) {
        String filename = getFileName(record);
        try {
            Files.deleteIfExists(Paths.get(RECORDINGS_DIRECTORY.getAbsolutePath(), filename).toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileName(Record record) {
        return DATE_FORMATTER.format(record.getSaveDate()) + ".zip";
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void beginRecording() {
        isRecording = true;
        record = new Record();
    }

    public Record endRecording() {
        record.endRecord();
        Record returnRecord = record;
        record = null;
        isRecording = false;
        return returnRecord;
    }

    public void saveRecording(Gson gson, Record record) {
        String jsonString = gson.toJson(record);
        String filename = getFileName(record);
        File file = new File(RECORDINGS_DIRECTORY, filename);

        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

            zos.putNextEntry(new ZipEntry(FILE_NAME));
            zos.write(jsonString.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRecording(Gson gson) {
        saveRecording(gson, endRecording());
    }

    public Record getRecord() {
        return record;
    }

    public List<Record> getRecords() {
        Gson gson = Constants.GSON;
        File[] files = RECORDINGS_DIRECTORY.listFiles();
        List<Record> records = new ArrayList<>();
        if (files == null) {
            return records;
        }
        for (File file : files) {
            try {
                if (!file.getName().endsWith(".zip")) {
                    continue;
                }
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
                ZipEntry zipEntry;
                do {
                    zipEntry = zis.getNextEntry();
                } while (zipEntry != null && !FILE_NAME.equals(zipEntry.getName()));
                if (zipEntry == null) {
                    continue;
                }
                int len;
                byte[] buffer = new byte[8192];
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while ((len = zis.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                String jsonString = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
                Record record = (Record) gson.fromJson(jsonString, Message.class);
                records.add(record);
                zis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Runtime.getRuntime().gc();
        return records;
    }

    @Override
    public void onSend(Message message) {
        if (isRecording && record != null) {
            if (message instanceof DriveMessage || message instanceof ShootMessage) {
                record.add(message);
            }
        }
    }
}
