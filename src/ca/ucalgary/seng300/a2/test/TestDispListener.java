package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.Display;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a2.DispListener;
import ca.ucalgary.seng300.a2.VendingManager;

public class TestDispListener {

	private DispListener testDisplayListener;
	private Display dummyDisplay;

	@Before
	public void setup() throws Exception {

		// Instantiate a testlistener to receive messages with this test class
		testDisplayListener = new DispListener();
		dummyDisplay = new Display();

	}

	////////////////////////////////////////////////////////////////////////
	// Test Display
	////////////////////////////////////////////////////////////////////////

	/**
	 * Tests enabled status
	 *
	 * Note: This test should be disabled if shorter test times are desired
	 */
	@Test
	public void testEnable() {
		testDisplayListener.enabled(dummyDisplay);
		assertEquals("enabled", testDisplayListener.getStatus());
	}

	/**
	 * Tests Disabled Status
	 */
	@Test
	public void testDisable() {
		testDisplayListener.disabled(dummyDisplay);
		assertEquals("disabled", testDisplayListener.getStatus());
	}

	/**
	 * Tests message log
	 *
	 */
	@Test
	public void testMessageLog() {

		String messageOld = "";

		for (int i = 0; i< 10; i++) {
			String message = "Test " + i;
			testDisplayListener.messageChange(dummyDisplay, messageOld, message);
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
