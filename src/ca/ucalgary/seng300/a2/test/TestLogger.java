package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.FileNotFoundException;

import ca.ucalgary.seng300.a2.Logger;

public class TestLogger {

	@Test(expected = NullPointerException.class)
	public void testConstructorNull() throws IllegalArgumentException, NullPointerException {
		new Logger(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorEmpty() throws IllegalArgumentException, NullPointerException {
		new Logger("");
	}
	
	@Test
	public void testConstructorName() throws IllegalArgumentException, NullPointerException {
		new Logger("temp.txt");
	}
	
	@Test
	public void testLogMsg() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		logger.log("Hello World!");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLogEmpty() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		logger.log("");
	}

	@Test(expected = NullPointerException.class)
	public void testLogNull() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String msg = null;
		logger.log(msg);
	}
	
	@Test
	public void testArrayLogMsg() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", "e", "l", "l", "o"};
		logger.log(msgs);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testArrayLogEmpty() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", "", "l", "l", "o"};
		logger.log(msgs);
	}
	
	@Test(expected = NullPointerException.class)
	public void testArrayLogNull() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", "e", null, "l", "o"};
		logger.log(msgs);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testArrayLogEmptyNull() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", "", null, "l", "o"};
		logger.log(msgs);
	}
	
	@Test(expected = NullPointerException.class)
	public void testArrayLogNullEmpty() throws IllegalArgumentException, FileNotFoundException, NullPointerException {
		Logger logger = new Logger("temp.txt");
		String[] msgs = {"H", null, "l", "l", "o"};
		logger.log(msgs);
	}
}
