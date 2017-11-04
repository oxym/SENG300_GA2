import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Allows to log actions of the user and the actions of the machine that are visible to the user.
 * Uses JVM's internal clock to stamp each log.
 *
 * ==== Usage ====
 *
 * Create byte output steam:
 *          initializeLogger(String filename)
 *
 * Log a message:
 *          log(String msg)
 *
 * Close the byte output steam:
 *          closeLog()
 */
public class Logger {

    private static PrintWriter writer;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static Date date = new Date();

    /**
     * Opens byte output steam for the specified file.
     * @param filename name of the existing or new file (eg. log.txt)
     * @throws Exception if error
     */
    public static void initializeLogger(String filename) throws Exception {

        if (!filename.equals(null)) {

            try {
                writer = new PrintWriter(new FileOutputStream(new File(filename), true));
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            throw new Exception("Could not initialize Logger");
        }
    }

    /**
     * Date and time stamps each msg, and appends it to the file
     * @param msg the text to be logged
     * @throws Exception if error
     */
    public static void log(String msg) throws Exception {

        if (!msg.equals(null)) {

            System.out.println(String.format("%s - %s", dateFormat.format(date), msg));
            writer.append(String.format("%s - %s\n", dateFormat.format(date), msg));
            writer.flush();
        } else {
            throw new Exception("No message found");
        }
    }

    /**
     * Closes the opened byte output steam, which was opened
     * with the initializeLogger(String filename) method.
     */
    public static void closeLog() {
        writer.close();
    }
}
