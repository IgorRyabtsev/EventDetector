package ya.eventdetector.util;

public class Utils {
    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public static final String UNABLE_TO_READ = "Unable to write to file ";
    public static final String UNABLE_TO_WRITE = "Unable to write to file ";
    public static final String DB_EXTENSION = ".db";
    private static final String USER_DIR = "user.dir";

    public static String getCurrentDir() {
        return System.getProperty(USER_DIR);
    }

}
