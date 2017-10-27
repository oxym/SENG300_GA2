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
	
	@Before
	public void setup() throws Exception{
		
		List<String> popCanNames = Arrays.asList("Coke", "Sprite", "Crush", "Ale", "Pepsi", "Diet");
		List<Integer> popCanCosts = Arrays.asList(250,250,250,250,250,250);
		
		int[] coinKinds = new int[] {5, 10, 25, 100, 200};
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
}
