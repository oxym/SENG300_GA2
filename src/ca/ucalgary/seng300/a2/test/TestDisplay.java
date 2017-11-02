package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.Deliverable;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.CoinSlot;
import org.lsmr.vending.hardware.DeliveryChute;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.EmptyException;
import org.lsmr.vending.hardware.SelectionButton;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a2.DispListener;
import ca.ucalgary.seng300.a2.InsufficientFundsException;
import ca.ucalgary.seng300.a2.VendingListener;
import ca.ucalgary.seng300.a2.VendingManager;

public class TestDisplay {

	private VendingMachine machine = null;
	private VendingManager manager = null;
	private int[] coinKinds = new int[] { 5, 10, 25, 100, 200 };

	// test listeners to ensure test accuracy due to multithreading
	private DispListener testDisplayListener;

	@Before
	public void setup() throws Exception {

		List<String> popCanNames = Arrays.asList("Coke", "Sprite", "Crush", "Ale", "Pepsi", "Diet");
		List<Integer> popCanCosts = Arrays.asList(250, 250, 250, 250, 250, 250);

		int selectionButtonCount = 6;
		int coinRackCapacity = 15;
		int popCanRackCapacity = 10;
		int receptacleCapacity = 200;
		machine = new VendingMachine(coinKinds, selectionButtonCount, coinRackCapacity, popCanRackCapacity,
				receptacleCapacity);
		machine.configure(popCanNames, popCanCosts);

		VendingManager.initialize(machine);
		manager = VendingManager.getInstance();

		// Instantiate a testlistener to receive messages with this test class
		testDisplayListener = new DispListener();
		machine.getDisplay().register(testDisplayListener);
	}


	////////////////////////////////////////////////////////////////////////
	// Test Display
	////////////////////////////////////////////////////////////////////////

	// NOTE: Because of threading, these tests may need to be run in a separate or
	// multiple separate
	// classes to test the desired effect and ensure correct behaviour as junit runs
	// multiple
	// tests concurrently, which produces out of sync messages on the display.

	/**
	 * Tests default message display
	 *
	 * Note: This test should be disabled if shorter test times are desired Note:
	 * Due to this running threaded, if some processes take up a larger amount of
	 * time, it is possible this test might fail, as it is dependent on delays and
	 * timing
	 * @throws DisabledException
	 */
	@Test
	public void testDefaultGreeting() throws DisabledException {

		//ensure the default greeting is displaying correctly.
		try {
			Thread.sleep(2000); // initial delay
			for (int i = 0; i < 3; i++) { // Loops 3 times
				assertEquals("Hi there!", testDisplayListener.getLastMessage());
				Thread.sleep(5000);
				assertEquals("", testDisplayListener.getLastMessage());
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//ensure that default greeting is still not scheduled after coin is added, or display is updated.
		assertEquals(0, manager.getCredit()); // credit should be 0 for default message to work

		try {
			Thread.sleep(8000); // let the default message be displayed for 8 seconds
			userAddCoin(100);
			Thread.sleep(20000);
			assertEquals("Credit: $  1.00", testDisplayListener.getLastMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

//	/**
//	 * Tests messageUpdates and stops displaying default when event happens
//	 *
//	 * Note: This test should be disabled if shorter test times are desired
//	 *
//	 * @throws DisabledException
//	 */
//	@Test
//	public void testUpdateDisplay() throws DisabledException {
//		assertEquals(0, manager.getCredit()); // credit should be 0 for default message to work
//
//		try {
//			Thread.sleep(8000); // let the default message be displayed for 8 seconds
//			userAddCoin(100);
//			Thread.sleep(20000);
//			assertEquals("Credit: $  1.00", testDisplayListener.getLastMessage());
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

//	/**
//	 * Tests the display is correct when coin is inserted
//	 *
//	 * Note: This test should be disabled if shorter test times are desired
//	 *
//	 * @throws DisabledException
//	 */
//	@Test
//	public void testCoinInsertedDisplay() throws DisabledException {
//
//		assertEquals(0, manager.getCredit()); // default credit should be 0
//		int amountInserted = 0;
//
//		for (int coinValue : coinKinds) {
//			userAddCoin(coinValue);
//			amountInserted += coinValue;
//			assertEquals(amountInserted, manager.getCredit());// confirm that amount inserted is correct
//			int dollars = manager.getCredit() / 100;
//			int cents = manager.getCredit() % 100;
//			String testMessage = String.format("Credit: $%3d.%02d", dollars, cents);
//			assertEquals(testMessage, testDisplayListener.getLastMessage());
//		}
//	}

	////////////////////////////////////////////////////////////////////////
	// User Actions
	////////////////////////////////////////////////////////////////////////

	public void userAddCoin(int value) throws DisabledException {
		machine.getCoinSlot().addCoin(new Coin(value));
	}

	public void userPressButton(int index) throws DisabledException {
		machine.getSelectionButton(index).press();
	}

}
