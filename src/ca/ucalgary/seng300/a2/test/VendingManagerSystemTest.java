package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;
import ca.ucalgary.seng300.a2.*;


/**
 * Test suite to system test the logic that manages the Vending Machine hardware.
 *
 * NOTE: The suite is regarded as a "system test" since it assumes the logic classes
 * are coupled to all of their "default" hardware classes, and to each other.
 * >> The emphasis on system / integration testing follows from the fact that
 * >> the operation of each logic class (e.g. the listeners / manager) depends heavily
 * >> on one another. Unit testing each exhaustively would be very time consuming and
 * >> not very informative.
 *
 */
public class VendingManagerSystemTest {

	private VendingMachine machine = null;
	private VendingManager manager = null;
	private MachineConfiguration cfg;

	// Stored as a field due to bugs with JUnit and multi-threading
	private DispListener testDisplayListener;

	@Before
	public void setup() throws Exception {

		cfg = new MachineConfiguration();

		VendingMachine machine = new VendingMachine(cfg.coinKinds, cfg.selectionButtonCount, cfg.coinRackCapacity, cfg.popCanRackCapacity,
				cfg.receptacleCapacity, cfg.deliveryChuteCapacity, cfg.coinReturnCapacity);
		machine.configure(cfg.popCanNames, cfg.popCanCosts);

		manager = VendingManager.initialize(machine, cfg.coinKinds);

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
	 * Test overflowing a coin rack
	 * overflow should end up in coin return
	 */
	@Test
	public void testOverflowCoinRack() {

		int coinRackCapacity = 10;

		machine = new VendingMachine(cfg.coinKinds, cfg.popCanNames.size(), cfg.coinRackCapacity, 1, 1, 1, 10);
		machine.configure(cfg.popCanNames, cfg.popCanCosts);

		Coin coin = new Coin(100);
		for (int i = 0; i < 2; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}


		assertEquals(machine.getCoinReturn().size(), 1);


	}

	/**
	 * Test that overflowing coin return enables the safety
	 * @throws DisabledException
	 *
	 */
	@Test
	public void testOverflowCoinReturn() throws DisabledException {

		int coinReturnCapacity = 1;

		machine = new VendingMachine(cfg.coinKinds, cfg.popCanNames.size(), 10, 5, 5, 5, coinReturnCapacity);
		machine.configure(cfg.popCanNames, cfg.popCanCosts);
		machine.loadPopCans(1, 1, 1, 1, 1, 1);
		machine.loadCoins(5, 5, 5, 5, 5);

		manager = VendingManager.initialize(machine, cfg.coinKinds);

		Coin coin = new Coin(1);
		machine.getCoinSlot().addCoin(coin);

		assertEquals(true,machine.getCoinSlot().isDisabled());
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

	/**coinKinds
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

	/**
	 * tests that if there is no change added to the machine
	 * exact change state returns false
	 *
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
		assertNotEquals(manager.checkExactChangeState(),("Thank you for your purchase!"));	}

/**
	 *
	 * Test that with a low amount of coins
	 * in the coin rack we can return exact change on a purchase
	 * then cannot return exact change on the next purchase
	 *
	 * @throws CapacityExceededException
	 * @throws EmptyException
	 * @throws DisabledException
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
	// Test Delivery Chute
	////////////////////////////////////////////////////////////////////////


	@Test
	public void testDeliveryChuteOverflow() throws DisabledException {

		machine = new VendingMachine(cfg.coinKinds, cfg.popCanNames.size(), 10, 10,	10, 1, 10);
		machine.configure(cfg.popCanNames, cfg.popCanCosts);

		manager = VendingManager.initialize(machine, cfg.coinKinds);

		machine.loadPopCans(10, 10, 10, 10, 10, 10);
		machine.disableSafety();

		assertFalse(machine.getOutOfOrderLight().isActive());		//check safety enabled

		System.out.println(machine.getDeliveryChute().size());
		buyPop(1);
		assertTrue(machine.getOutOfOrderLight().isActive());

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

		for (int coinValue : cfg.coinKinds) {
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

	public void buyPop(int index) throws DisabledException {
		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
			}
		}
		userPressButton(index);
	}
}
