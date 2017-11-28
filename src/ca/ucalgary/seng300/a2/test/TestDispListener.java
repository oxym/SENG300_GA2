package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.hardware.Display;
import ca.ucalgary.seng300.a2.DispListener;

public class TestDispListener {

	private DispListener testDisplayListener;
	private Display dummyDisplay;

	@Before
	public void setup() throws Exception {

		// Instantiate a testlistener to receive messages with this test class
		testDisplayListener = new DispListener(null);
		dummyDisplay = new Display();

	}

	////////////////////////////////////////////////////////////////////////
	// Test Display
	////////////////////////////////////////////////////////////////////////

	/**
	 * Tests message log
	 *
	 */
	@Test
	public void testMessageLog() {

		String messageOld = "";
		String messageCurrent= "";

		for (int i = 0; i< 10; i++) {
			String message = "Test " + i;
			testDisplayListener.messageChange(dummyDisplay, messageOld, message);
			assertEquals(message, testDisplayListener.getCurrentMessage());
			assertEquals(messageOld, testDisplayListener.getLastMessage());

		}
	}

	/**
	 * Tests message log empty
	 *
	 */
	@Test
	public void testMessageLogEmpty() {
			assertEquals("", testDisplayListener.getLastMessage());
	}
}
