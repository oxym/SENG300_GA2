package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.FileNotFoundException;

import ca.ucalgary.seng300.a2.Logger;

public class TestLogger {
	
	/**
	 * Test Logger constructor with null for filename
	 * @throws IllegalArgumentException if error
	 * @throws NullPointerException if error
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNull() throws IllegalArgumentException, NullPointerException {
		new Logger(null);
	}
	
	/**
	 * Test Logger constructor with empty filename
	 * @throws IllegalArgumentException if error
	 * @throws NullPointerException if error
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorEmpty() throws IllegalArgumentException, NullPointerException {
		new Logger("");
	}
	
	/**
	 * Test Logger constructor with valid filename
	 * @throws IllegalArgumentException if error
	 * @throws NullPointerException if error
	 */
	@Test
	public void testConstructorName() throws IllegalArgumentException, NullPointerException {
		new Logger("temp.txt");
	}
	
	/**
	 * Test log(String msg) method with valid message
	 * @throws IllegalArgumentException if error
	 * @throws FileNotFoundException if error
	 * @throws NullPointerException if error
	 */
	@Test
	public void testLogMsg() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		logger.log("Hello World!");
	}
	
	/**
	 * Test log(String msg) method with an empty message
	 * @throws IllegalArgumentException if error
	 * @throws FileNotFoundException if error
	 * @throws NullPointerException if error
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLogEmpty() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		logger.log("");
	}

	/**
	 * Test log(String msg) method with a null message
	 * @throws IllegalArgumentException if error
	 * @throws FileNotFoundException if error
	 * @throws NullPointerException if error
	 */
	@Test(expected = NullPointerException.class)
	public void testLogNull() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String msg = null;
		logger.log(msg);
	}
	
	/**
	 * Test log(String[] msgs) method with a valid array of messages
	 * @throws IllegalArgumentException if error
	 * @throws FileNotFoundException if error
	 * @throws NullPointerException if error
	 */
	@Test
	public void testArrayLogMsg() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", "e", "l", "l", "o"};
		logger.log(msgs);
	}
	
	/**
	 * Test log(String[] msgs) method with an array of messages where one message is empty
	 * @throws IllegalArgumentException if error
	 * @throws FileNotFoundException if error
	 * @throws NullPointerException if error
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testArrayLogEmpty() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", "", "l", "l", "o"};
		logger.log(msgs);
	}
	
	/**
	 * Test log(String[] msgs) method with a valid array of messages where one message is null
	 * @throws IllegalArgumentException if error
	 * @throws FileNotFoundException if error
	 * @throws NullPointerException if error
	 */
	@Test(expected = NullPointerException.class)
	public void testArrayLogNull() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", "e", null, "l", "o"};
		logger.log(msgs);
	}
	
	/**
	 * Test log(String[] msgs) method with a valid array of messages 
	 * where first invalid message is the empty message
	 * @throws IllegalArgumentException if error
	 * @throws FileNotFoundException if error
	 * @throws NullPointerException if error
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testArrayLogEmptyNull() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", "", null, "l", "o"};
		logger.log(msgs);
	}
	
	/**
	 * Test log(String[] msgs) method with a valid array of messages 
	 * where first invalid message is null
	 * @throws IllegalArgumentException if error
	 * @throws FileNotFoundException if error
	 * @throws NullPointerException if error
	 */
	@Test(expected = NullPointerException.class)
	public void testArrayLogNullEmpty() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", null, "l", "l", "o"};
		logger.log(msgs);
	}
}
