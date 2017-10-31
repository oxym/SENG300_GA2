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

import ca.ucalgary.seng300.a2.InsufficientFundsException;
import ca.ucalgary.seng300.a2.VendingListener;
import ca.ucalgary.seng300.a2.VendingManager;

public class VendingManagerSystemTest {

	private VendingMachine machine = null;
	private VendingManager manager = null;
	private int[] coinKinds = new int[] {5, 10, 25, 100, 200};

	@Before
	public void setup() throws Exception{

		List<String> popCanNames = Arrays.asList("Coke", "Sprite", "Crush", "Ale", "Pepsi", "Diet");
		List<Integer> popCanCosts = Arrays.asList(250,250,250,250,250,250);

		int selectionButtonCount = 6;
		int coinRackCapacity = 15;
		int popCanRackCapacity = 10;
		int receptacleCapacity = 200;
		machine = new VendingMachine(coinKinds, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity);
		machine.configure(popCanNames, popCanCosts);

		VendingManager.initialize(machine);
		manager = VendingManager.getInstance();
	}

	/**
	 * Tests that the logic can dispense the correct pop after too much change is added
	 * and the button is pressed. Also confirms that nothing else is dispensed and the
	 * credit is reduced appropriately.
	 */
	@Test
	public void testCreditAndPop() {

		machine.loadPopCans(10,10,10,10,10,10);
		machine.loadCoins(10,10,10,10,10);

		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++){ //Adds three dollars to the machine
			try{
				machine.getCoinSlot().addCoin(coin);
			} catch(DisabledException e){}
		}
		machine.getSelectionButton(1).press();

		Deliverable[] delivered = machine.getDeliveryChute().removeItems();

		assertEquals(1, delivered.length);

		String expected = machine.getPopKindName(1);
		String dispensed = delivered[0].toString();

		assertEquals(dispensed, expected);
		assertEquals(manager.getCredit(), 50);
	}

	/**
	 * Tests that the logic is able to handle the case where the selected pop is not available
	 * but there were sufficient funds added. Ensures that the credit is not reduced in this case.
	 */
	@Test
	public void testCreditAndNoPop() {

		machine.loadPopCans(0,0,0,0,0,0);
		machine.loadCoins(10,10,10,10,10);

		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++){ //Adds three dollars to the machine
			try{
				machine.getCoinSlot().addCoin(coin);
			} catch(DisabledException e){}
		}

		machine.getSelectionButton(1).press();

		Deliverable[] delivered = machine.getDeliveryChute().removeItems();

		assertEquals(delivered.length, 0);
		assertEquals(manager.getCredit(), 300);
	}

	/**
	 * Tests the case where the selected pop is available but insufficient funds have been added.
	 */
	@Test
	public void testLowCreditAndPop() {

		machine.loadPopCans(10,10,10,10,10,10);
		machine.loadCoins(10,10,10,10,10);

		Coin coin = new Coin(100);
		for (int i = 0; i < 2; i++){ //Adds two dollars to the machine
			try{
				machine.getCoinSlot().addCoin(coin);
			} catch(DisabledException e){}
		}

		machine.getSelectionButton(1).press();

		Deliverable[] delivered = machine.getDeliveryChute().removeItems();

		assertEquals(delivered.length, 0);
		assertEquals(manager.getCredit(), 200);
	}

	/**
	 * Tests the case where the selected pop is available but no funds have been added.
	 */
	@Test
	public void testNoCreditAndPop() {

		machine.loadPopCans(10,10,10,10,10,10);
		machine.loadCoins(10,10,10,10,10);

		machine.getSelectionButton(1).press();

		Deliverable[] delivered = machine.getDeliveryChute().removeItems();

		assertEquals(delivered.length, 0);
		assertEquals(manager.getCredit(), 0);
	}


	////////////////////////////////////////////////////////////////////////
	// Test Display
	////////////////////////////////////////////////////////////////////////

	//NOTE: Because of threading, these tests may need to be run in a separate or multiple separate
	// classes to test the desired effect and ensure correct behaviour as junit runs multiple
	// tests concurrently, which produces out of sync messages on the display.

	/**
	 * Tests default message display
	 *
	 * Note: This test should be disabled if shorter test times are desired
	 */
	@Test
	public void testDefaultDisplay() {
		//TODO: need to implement the display listener to retrieve messages to test against actual

		assertEquals(0, manager.getCredit()); //credit should be 0 for default message to work

        //Machine is idle for 48 seconds - the display should alternate between Hi there! and clear
		try {
            Thread.sleep(36000); //let the machine operate for 48 seconds look at alternating input
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

	}

	/**
	 * Tests messageUpdates and stops displaying default when event happens
	 *
	 * Note: This test should be disabled if shorter test times are desired
	 * @throws DisabledException
	 */
	@Test
	public void testUpdateDisplay() throws DisabledException {
		//TODO: need to implement the display listener to retrieve messages to test against actual
		System.out.println("Testing update display");
		assertEquals(0, manager.getCredit()); //credit should be 0 for default message to work

        //Machine is idle for 48 seconds - the display should alternate between Hi there! and clear
		try {
            Thread.sleep(8000); //let the machine operate for 48 seconds look at alternating input
            userAddCoin(100);
            Thread.sleep(20000);
            //TODO: confirm credit is still displayed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		System.out.println("Finished");
	}

	/**
	 * Tests the display is correct when coin is inserted
	 *
	 * Note: This test should be disabled if shorter test times are desired
	 * @throws DisabledException
	 */
	@Test
	public void testCoinInsertedDisplay() throws DisabledException {
		//TODO: need to implement the display listener to retrieve messages to test against actual

		assertEquals(0, manager.getCredit()); //default credit should be 0
		int amountInserted = 0;

		for(int coinValue: coinKinds) {
			userAddCoin(coinValue);
			amountInserted += coinValue;
			assertEquals(amountInserted, manager.getCredit());//confirm that amount inserted is correct
			//TODO: check that display message is reporting correctly
		}

	}


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
