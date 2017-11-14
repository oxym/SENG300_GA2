package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.CoinChannel;
import org.lsmr.vending.hardware.CoinSlot;
import org.lsmr.vending.hardware.DeliveryChute;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.EmptyException;
import org.lsmr.vending.hardware.PushButton;
import org.lsmr.vending.hardware.SimulationException;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a2.DispListener;
import ca.ucalgary.seng300.a2.InsufficientFundsException;
import ca.ucalgary.seng300.a2.VendingListener;
import ca.ucalgary.seng300.a2.VendingManager;

public class VendingManagerSystemTest {

	private VendingMachine machine = null;
	private VendingManager manager = null;
	private int[] coinKinds = new int[] { 5, 10, 25, 100, 200 };

	// test listeners to ensure test accuracy due to multithreading
	private DispListener testDisplayListener;

	@Before
	public void setup() throws Exception {

		List<String> popCanNames = Arrays.asList("Coke", "Sprite", "Crush", "Ale", "Pepsi", "Diet");
		List<Integer> popCanCosts = Arrays.asList(250, 250, 250, 250, 250, 250);

		int selectionButtonCount = popCanNames.size();
		int coinRackCapacity = 15;
		int popCanRackCapacity = 10;
		int receptacleCapacity = 200;
		int deliveryChuteCapacity = 200;
		int coinReturnCapacity = 200;

		machine = new VendingMachine(coinKinds, selectionButtonCount, coinRackCapacity, popCanRackCapacity,
				receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
		machine.configure(popCanNames, popCanCosts);

		manager = VendingManager.initialize(machine);

		machine.disableSafety(); //needed due to singleton instance being passed to multiple tests
								 //that appear to clone the current state of the machine at the time of instantiation

		// Register an additional display listener to receive messages during testing
		// Note: display listener is unit tested in TestDispListener.java
		testDisplayListener = new DispListener(manager);
		machine.getDisplay().register(testDisplayListener);
	}

	/**
	 * Tests that the logic can dispense the correct pop after too much change is
	 * added and the button is pressed. Also confirms that nothing else is dispensed
	 * and the credit is reduced appropriately.
	 */
	@Test
	public void testCreditAndPop() {

		machine.loadPopCans(10, 10, 10, 10, 10, 10);
		machine.loadCoins(10, 10, 10, 10, 10);

		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}
		machine.getSelectionButton(1).press();

		PopCan[] delivered = machine.getDeliveryChute().removeItems();

		String expected = machine.getPopKindName(1);
		String dispensed = delivered[0].toString();

		assertEquals(1, delivered.length);
		assertEquals(expected, dispensed);
		assertEquals(0, manager.getCredit());
	}

	//TODO Document
	/**
	 *
	 * @throws CapacityExceededException
	 * @throws EmptyException
	 * @throws DisabledException
	 */
	@Test
	public void testCoinReturn() throws CapacityExceededException, EmptyException, DisabledException{
		machine.loadCoins(2, 1, 2, 2, 2);
		Coin coin = new Coin(100);
		try {
			machine.getCoinSlot().addCoin(coin);
		} catch (DisabledException e) {
		}
		manager.checkExactChangeState();
	}

	/**
	 * Tests that the logic is able to handle the case where the selected pop is not
	 * available but there were sufficient funds added. Ensures that the credit is
	 * not reduced in this case.
	 */
	@Test
	public void testCreditAndNoPop() {

		machine.loadPopCans(0, 0, 0, 0, 0, 0);
		machine.loadCoins(10, 10, 10, 10, 10);

		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}

		machine.getSelectionButton(1).press();

		PopCan[] delivered = machine.getDeliveryChute().removeItems();

		assertEquals(0, delivered.length);
		assertEquals(300, manager.getCredit());
	}

	/**
	 * Tests the case where the selected pop is available but insufficient funds
	 * have been added.
	 */
	@Test
	public void testLowCreditAndPop() {

		machine.loadPopCans(10, 10, 10, 10, 10, 10);
		machine.loadCoins(10, 10, 10, 10, 10);

		Coin coin = new Coin(100);
		for (int i = 0; i < 2; i++) { // Adds two dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}

		machine.getSelectionButton(1).press();

		PopCan[] delivered = machine.getDeliveryChute().removeItems();

		assertEquals(0, delivered.length);
		assertEquals(200, manager.getCredit());
	}

	/**
	 * Tests the case where the selected pop is available but no funds have been
	 * added.
	 */
	@Test
	public void testNoCreditAndPop() {

		machine.loadPopCans(10, 10, 10, 10, 10, 10);
		machine.loadCoins(10, 10, 10, 10, 10);

		machine.getSelectionButton(1).press();

		PopCan[] delivered = machine.getDeliveryChute().removeItems();

		assertEquals(0, delivered.length);
		assertEquals(0, manager.getCredit());
	}

	/**
	 * tests that safety is enabled when machine runs out of pop
	 */
	@Test
	public void testNoPopLeftEnableSafety() {

		machine.loadPopCans(1, 0, 0, 0, 0, 0);
		machine.loadCoins(10, 10, 10, 10, 10);

		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}

		machine.getSelectionButton(0).press();

		assertTrue(machine.isSafetyEnabled());
		assertTrue(machine.getOutOfOrderLight().isActive());
		assertEquals(machine.getDeliveryChute().size(), 1);

	}

	/**
	 * tests that safety does not enable if only one pop type is empty
	 */
	@Test
	public void testOutOfOnePopType() {

		machine.loadPopCans(0, 10, 10, 10, 10, 10);
		machine.loadCoins(10, 10, 10, 10, 10);

		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}

		machine.getSelectionButton(0).press();

		assertFalse(machine.isSafetyEnabled());
		assertFalse(machine.getOutOfOrderLight().isActive());

	}

	/**
	 * test that disabledException is thrown when a coin is inserted and safety is enabled
	 *
	 * @throws DisabledException
	 */
	@Test (expected = DisabledException.class)
	public void testAddCoinSafetyEnabled() throws DisabledException {

		machine.enableSafety();

		assertTrue(machine.getOutOfOrderLight().isActive());

		Coin coin = new Coin(100);
		machine.getCoinSlot().addCoin(coin);

	}

	/**
	 * tests that safety is enabled when coinReceptacle reaches capacity
	 * should throw a disabled exception when the final coin is added, since safety will be enabled
	 *
	 * @throws DisabledException
	 */
	@Test (expected = DisabledException.class)
	public void testValidCoinInsertCoinReceptaclesFull() throws DisabledException {
		Coin quarter = new Coin(25);
		for (int i = 0; i < machine.getCoinReceptacle().getCapacity(); i++)
		{
			machine.getCoinSlot().addCoin(quarter);
		}


		machine.getCoinSlot().addCoin(quarter);

	}

	/**
	 * tests that pop is not delivered to the chute when a button is pressed if machine is disabled
	 *
	 * @throws EmptyException
	 */
	@Test
	public void testPopRequestWhenDisabled() throws EmptyException {

		machine.loadPopCans(0, 10, 10, 10, 10, 10);
		machine.enableSafety();

		machine.getSelectionButton(0).press();
		assertEquals(machine.getDeliveryChute().size(), 0);
	}

	/*
	 * tests that if there is no change added to the machine
	 * exact change state returns false
	 *
	 */
	@Test
	public void testExactChangeForPurchase() {

		machine.loadCoins(0, 0, 0, 0, 0);

		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}

		assertEquals(false, manager.checkExactChangeState());
	}

	/*
	 * TODO figure out why its not returning false as it should be
	 *
	 *
	 * test low credit that can return exact change on a purchase
	 * then cannot return exact change on the next purchase
	 */
	@Test
	public void testLowChangeThenNotEnoughForExact() throws CapacityExceededException, EmptyException, DisabledException {
		machine.loadPopCans(10, 10, 10, 10, 10, 10);
		machine.loadCoins(0, 0, 2, 0, 0);

		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}

		assertEquals(true, manager.checkExactChangeState());


		machine.getSelectionButton(0).press();

		assertEquals(1, machine.getDeliveryChute().size());

		//insert 3 dollars more and check
		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}

		assertEquals(false, manager.checkExactChangeState());


	}



	////////////////////////////////////////////////////////////////////////
	// Test Display
	////////////////////////////////////////////////////////////////////////

	/**
	 * Tests the display is correct when coin is inserted
	 *
	 * @throws DisabledException
	 */
	@Test
	public void testCoinInsertedDisplay() throws DisabledException {

		assertEquals(0, manager.getCredit()); // default credit should be 0
		int amountInserted = 0;

		for (int coinValue : coinKinds) {
			userAddCoin(coinValue);
			amountInserted += coinValue;
			assertEquals(amountInserted, manager.getCredit());// confirm that amount inserted is correct
			int dollars = manager.getCredit() / 100;
			int cents = manager.getCredit() % 100;
			String testMessage = String.format("Credit: $%3d.%02d", dollars, cents);
			assertEquals(testMessage, testDisplayListener.getCurrentMessage());
		}
	}


//	@Test
//	public void testGetRefund() throws CapacityExceededException, DisabledException	{
//		Coin coin = new Coin(100);
//		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
//			try {
//				machine.getCoinSlot().addCoin(coin);
//			} catch (DisabledException e) {
//			}
//		}
//		int itemsInReceptacle = machine.getCoinReceptacle().size();
//		manager.refundButtonPressed();
//		int coinsDelivered = machine.getCoinReturn().size();
//		assertEquals(itemsInReceptacle, coinsDelivered);
//
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
