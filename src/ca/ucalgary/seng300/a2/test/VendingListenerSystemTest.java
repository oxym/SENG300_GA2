package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.Arrays;
import java.util.List;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a2.*;

/**
 * A test suite designed to verify the implementation of the hardware logic classes 
 * VendingManager and VendingListener built for SENG 300's Group Assignment 1.
 * 
 * NOTE: Due to the encapsulation of the Vending(Manager/Listener) classes and their strict
 * dependence on one another and VendingMachine to access the hardware classes (e.g. CoinSlot), testing
 * is done at the "system" level, without using stubs for VendingMachine or other hardware.
 *   >> This decision is supported by Mr. Client's claim that the provided hardware classes 
 *   (org.lsm.vending) have been thoroughly tested and can be assumed to function as intended. 
 * 
 * This test suite makes some assumptions based on the requirements of the assignment, and
 * thus this suite may become deprecated in the future if particular values change. These include:
 * 		- The number of pop types and names. (6 types of pop, irrelevant names.)
 * 		- Prices and amounts of each pop. (Assumed global price of 250 cents. 10 pops per type.)
 * 		- The currency accepted. (Canadian coins of 5, 10, 25, 100, 200 cent values.)
 * 
 * @author Raymond Tran (30028473)
 * @author Thomas Coderre (10169277)
 * @author Thobthai Chulpongsatorn (30005238)
 *
 */
public class VendingListenerSystemTest {
	
	// Initialized outside of setup() so they are accessible.
	// Primary hardware, software accessed in tests.
	private VendingMachine machine = null;
	private VendingManager manager = null;
	private CoinSlot slot = null;
	private DeliveryChute chute = null;
	
	// Six selection buttons, each named after current pop test names.
	private SelectionButton cokeButton = null;
	private SelectionButton spriteButton = null;
	private SelectionButton crushButton = null;
	private SelectionButton aleButton = null;
	private SelectionButton pepsiButton = null;
	private SelectionButton dietButton = null;
	
	// Accepted Canadian currency. 
	private Coin nickel = new Coin(5);
	private Coin dime = new Coin(10);
	private Coin quarter = new Coin(25);
	private Coin loonie = new Coin(100);
	private Coin toonie = new Coin(200);
	
	
	// Configures the value of their button, pop types, and prices.
	@Before
	public void setup() throws Exception{
		
		// Pop names and their associated values. 6 pops, all valued at 250 cents.
		List<String> popCanNames = Arrays.asList("Coke", "Sprite", "Crush", "Ale", "Pepsi", "Diet");
		List<Integer> popCanCosts = Arrays.asList(250, 250, 250, 250, 250, 250);
		
		// Initialize accepted currency. Non-changing.
		int[] coinKinds = {5, 10, 25, 100,200};
		
		// Set-up the Vending Machine
		// - Accepts Canadian Currency
		// - Has 6 selection buttons. (6 types of pop)
		// - Coin Rack capacity of 15 in Mr.Client's updated response to competing group.
		// - Pop Rack capacity of 10 to hold 10 of each types of pop.
		// - Receptacle capacity of 200 in Mr. Client's updated response to competing group.
		machine = new VendingMachine(coinKinds, 6, 15, 10, 200);
		machine.configure(popCanNames, popCanCosts);
		
		VendingManager.initialize(machine);
		manager = VendingManager.getInstance();
		
		slot = machine.getCoinSlot();
		cokeButton = machine.getSelectionButton(0);
		spriteButton = machine.getSelectionButton(1);
		crushButton = machine.getSelectionButton(2);
		aleButton = machine.getSelectionButton(3);
		pepsiButton = machine.getSelectionButton(4);
		dietButton = machine.getSelectionButton(5);
		chute = machine.getDeliveryChute(); 
	}
	
	// Checks to see if anything is dispensed when an insufficient amount of credit is added.
	// This one actually did something!
	@Test
	public void invalidCreditInStock() {
		
		machine.loadPopCans(5,5,5,5,5,5);
		
		try{
			slot.addCoin(toonie);
		}
		catch (DisabledException e) {
			fail("CoinSlot disabled");
		}
		
		cokeButton.press();
		dietButton.press();
		
		Deliverable[] dispensed = chute.removeItems();
		assertEquals(0, dispensed.length);
	}

	// Adds a sufficient amount of credit and attempts to purchase two products.
	// Should only dispense one product, referred to as Coke.
	// Inadvertently ensures that valid coins don't also come out.
	@Test
	public void validCreditInStock() {
		
		machine.loadPopCans(5,5,5,5,5,5);
		
		try{
			slot.addCoin(toonie);
			slot.addCoin(toonie);
		}
		catch (DisabledException e) {
			fail("CoinSlot disabled");
		}
		
		cokeButton.press();
		dietButton.press();
		
		Deliverable[] dispensed = chute.removeItems();
		assertEquals(1, dispensed.length);
		assertEquals("Coke", dispensed[0].toString());
	}

	// Adds a invalid coin, and attempts to collect it from the delivery chute.
	// NOTICE: This test may become deprecated in the future.
	// REASON: Involves coins returning from delivery chute.
	@Test
	public void returnBadCoin() {
		
		machine.loadPopCans(5,5,5,5,5,5);
		
		try{
			Coin limaGold = new Coin(5000);
			slot.addCoin(limaGold);
		}
		catch (DisabledException e) {
			fail("CoinSlot disabled");
		}
		
		cokeButton.press();
		
		Deliverable[] dispensed = chute.removeItems();
		assertEquals(1, dispensed.length);
		assertEquals("5000", dispensed[0].toString());
	}
	
	// Attempt to buy more pops than there actually is.
	// Expecting it to dispense 5 pops of one particular type.
	@Test
	public void buyOutOfStock() {
		
		machine.loadPopCans(5,5,5,5,5,5);
		
		// Add 8 200-value coins into the machine. (1600 currency)
		try{
			for(int i = 0; i < 8; i++) {
				slot.addCoin(toonie);
			}
		}
		catch (DisabledException e) {
			fail("CoinSlot disabled");
		}
		
		// Press the ale button 6 times. (1500 currency)
		for(int i = 0; i < 6; i++) {
			aleButton.press();
		}

		Deliverable[] dispensed = chute.removeItems();
		assertEquals(5, dispensed.length);
		assertEquals("Ale", dispensed[0].toString());
		assertEquals("Ale", dispensed[4].toString());
	}

	// Attempt to buy a pop (250 credit) with insufficient credit (245 credit)
	// Then, add sufficient credit (245 + 5 credit), and try to purchase two.
	// Only one item is expected to be delivered- the one pop that was affordable.
	// Inadvertently also checks to see if hardware and logic understands all
	// Canadian currency. (With the exception of toonies which have been tested extensively.)
	@Test
	public void clumsyPurchase() {
		
		machine.loadPopCans(5,5,5,5,5,5);
		
		// 100 + 100 + 25 + 10 + 5 + 5 = 245 credit.
		try{
			slot.addCoin(loonie);
			slot.addCoin(loonie);
			slot.addCoin(quarter);
			slot.addCoin(dime);
			slot.addCoin(nickel);
			slot.addCoin(nickel);
		}
		catch (DisabledException e) {
			fail("CoinSlot disabled");
		}
		
		spriteButton.press();
		
		try {
			slot.addCoin(nickel);
		}
		catch (DisabledException e) {
			fail("CoinSlot disabled");
		}
		
		crushButton.press();
		pepsiButton.press();

		Deliverable[] dispensed = chute.removeItems();
		assertEquals(1, dispensed.length);
		assertEquals("Crush", dispensed[0].toString());
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
	
		for (int i = 0; i < 3; i++){ //Adds three dollars to the machine
			try{
				slot.addCoin(loonie);
			} catch(DisabledException e){
				fail("CoinSlot disabled");
			}
		}
		machine.getSelectionButton(1).press();
		
		Deliverable[] delivered = chute.removeItems();
		
		assertEquals(1, delivered.length);
		
		
		String expected = machine.getPopKindName(1);	
		String dispensed = delivered[0].toString();
		
		assertEquals(dispensed, expected);
		assertEquals(manager.getCredit(), 50);
	}
	
	/**
	 * Tests that the logic is able to handle the case where the selected pop is not available
	 * but there were sufficient funds added. 
	 * Expects no pop and unchanged credit.
	 * 
	 */
	@Test
	public void testCreditAndNoPop() {
	
		machine.loadCoins(10,10,10,10,10);

		for (int i = 0; i < 3; i++){ //Adds three dollars to the machine
			try{
				slot.addCoin(loonie);
			} catch(DisabledException e){
				fail("CoinSlot disabled");
			}
		} 
	
		cokeButton.press();
				
		Deliverable[] delivered = chute.removeItems();
		
		assertEquals(delivered.length, 0);
		assertEquals(manager.getCredit(), 300);
	}
	
	/**
	 * Tests the case where the selected pop is available but no funds have been added.
	 * Expect no pop and 0 credit.  
	 */
	@Test
	public void testNoCreditAndPop() {
		
		machine.loadPopCans(10,10,10,10,10,10);
		
		cokeButton.press();
		
		Deliverable[] delivered = chute.removeItems();
		
		assertEquals(delivered.length, 0);
		assertEquals(manager.getCredit(), 0);
	}
	
}
