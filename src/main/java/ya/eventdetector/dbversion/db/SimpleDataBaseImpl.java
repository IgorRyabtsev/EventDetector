package ya.eventdetector.dbversion.db;

import ya.eventdetector.dbversion.Event;
import ya.eventdetector.util.Utils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ya.eventdetector.util.Utils.UNABLE_TO_READ;
import static ya.eventdetector.util.Utils.UNABLE_TO_WRITE;

public class SimpleDataBaseImpl {

    private static Logger logger = Logger.getLogger(SimpleDataBaseImpl.class.getName());

    public static void writeEventsToDatabase(List<Event> events, String filename) {
        File writeDirectory = new File(Utils.getCurrentDir() + "/db");
        writeDirectory.mkdir();
        File fileToWrite = new File(writeDirectory, filename);

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileToWrite))) {
            objectOutputStream.writeObject(events);
        } catch (IOException e) {
            logger.log(Level.FINE, UNABLE_TO_WRITE + filename);
            logger.log(Level.FINE, Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(UNABLE_TO_WRITE);
        }
    }

    public static List<Event> readEventsFromDatabase(String filename) {
        File fileReadFrom = new File(Utils.getCurrentDir() + "/db/" + filename);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileReadFrom))) {
            return (CopyOnWriteArrayList<Event>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.FINE, UNABLE_TO_READ + filename);
            logger.log(Level.FINE, Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(UNABLE_TO_READ);
        }
    }

    public static void removeEvents(String filename) {
        File fileToWrite = new File(Utils.getCurrentDir() + "/db/" + filename);
        if (!fileToWrite.delete()) {
            logger.log(Level.FINE, "Unable to delete file " + filename);
        }
    }
}
