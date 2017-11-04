package ca.ucalgary.seng300.a2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creates and maintains a file event log for user-initiated actions 
 * and relevant hardware events.
 * Uses JVM's internal clock to add a time stamp to each entry.
 *
 * ==== Usage ====
 *
 * Construct Logger object:
 *        Logger(String filename)
 *
 * Log a single message:
 *        log(String msg)
 *      
 * Log a series of messages:
 *        log(String[] msg)
 */
public class Logger {
    private boolean debug = true;
	
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static Date date = new Date();
	
    private String name;
    private PrintWriter writer;
    
    /**
     * Initializes a Logger instance for a given log file.
     * @throws  
     */
    Logger(String filename) throws IllegalArgumentException {
    	
    	if (!filename.equals("") && !filename.equals(null))
    		name = filename;
    	else
    		throw new IllegalArgumentException("Filename cannot be empty or null.");
    }
    
    /**
     * Adds a single message to the log file, as a single new line. 
     * Date and time are prepended to each message.
     * @param msg The text to be logged
     * @throws IllegalArgumentException If the message string is empty or null
     * @throws FileNotFoundException If the file cannot be created or is a directory.
     */
    public void log(String msg) throws IllegalArgumentException,
    								   FileNotFoundException {
    	initializeLog();
        write(msg);
        closeLog();
    }
    
    /**
     * Adds a series of messages to the log file, as a separate new lines. 
     * Date and time are prepended to each message.
     * @param msg The texts to be logged. Cannot elements cannot be empty or null.
     * @throws IllegalArgumentException If the message string is empty or null
     * @throws FileNotFoundException If the file cannot be created or is a directory.
     */
    public void log(String[] msgs) throws IllegalArgumentException,
    								   FileNotFoundException {
    	initializeLog();
    	for (String msg : msgs){
    		write(msg);
    	}
        closeLog();
    }
    
    /**
     * Opens byte output steam for the specified file.
     * @param filename name of the existing or new file (eg. log.txt)
     * @throws FileNotFoundException If the file cannot be created or is a directory.
     */
    private void initializeLog() throws FileNotFoundException {
        writer = new PrintWriter(new FileOutputStream(new File(name), true));
    }

    /**
     * Private method to handle log writing.
     * Assumes the byte stream is already open and will be closed
     * by calling code.
     * @throws IllegalArgumentException If the message string is empty or null
     */
    private void write(String msg) throws IllegalArgumentException,
	   								 FileNotFoundException {
    	 if (!msg.equals("") && !msg.equals(null)) {
             if (debug) System.out.println(String.format("%s - %s", dateFormat.format(date), msg));
             writer.append(String.format("%s - %s\n", dateFormat.format(date), msg));
             writer.flush();
         } else {
             throw new IllegalArgumentException("Message cannot be empty.");
         }
    }
    
    /**
     * Closes the opened byte output steam, if it exists
     */
    private void closeLog() {
    	if (writer != null)
    		writer.close();
    }
}
