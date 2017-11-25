package ca.ucalgary.seng300.a2.test;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a2.MachineConfiguration;
import ca.ucalgary.seng300.a2.VendingManager;

/**
 * A quick gui test for functionality of what is working
 *
 */
public class TestQuickGuiTest {

	public VendingMachine machine = null;
	private VendingManager manager = null;
	private MachineConfiguration cfg;

	@Before
	public void setup() throws Exception {

		cfg = new MachineConfiguration();

		machine = new VendingMachine(cfg.coinKinds, cfg.selectionButtonCount, cfg.coinRackCapacity, cfg.popCanRackCapacity,
				cfg.receptacleCapacity, cfg.deliveryChuteCapacity, cfg.coinReturnCapacity);
		machine.configure(cfg.popCanNames, cfg.popCanCosts);

		manager = VendingManager.initialize(machine, cfg.coinKinds);

		machine.disableSafety(); //needed due to singleton instance being passed to multiple tests
								 //that appear to clone the current state of the machine at the time of instantiation

	}

	@Test
	public void quickTest() throws InterruptedException, DisabledException {

		int selfTestDelay = 500;
		int actionDelay = 1200;
		//pause before doing actions to allow seutp time and to see if display is working correctly
		//out of order light should be lit because nothing is in machine
		Thread.sleep(3000);

		//test safety/out of order light
		machine.enableSafety();
		Thread.sleep(selfTestDelay);
		machine.disableSafety();
		Thread.sleep(selfTestDelay);

		//lights
		machine.getExactChangeLight().activate();
		Thread.sleep(selfTestDelay);
		machine.getExactChangeLight().deactivate();
		Thread.sleep(selfTestDelay);

		//load machine after self test
		machine.loadPopCans(8, 8, 8, 8, 8, 8);
		machine.loadCoins(0, 0, 1, 0, 0);

		//add 3 dollars display should update
		Coin coin = new Coin(100);
		for (int i = 0; i < 3; i++) { // Adds three dollars to the machine
			try {
				machine.getCoinSlot().addCoin(coin);
				Thread.sleep(actionDelay); //pause between insertions
			} catch (DisabledException e) {
			}
		}

		Thread.sleep(actionDelay); //pause between action
		//simulate buy pop
		userPressButton(1);

//TODO: Note: simulation fails when change return overflows
		for(int i = 0; i < 6; i++) {
			buyPop(0);
			Thread.sleep(actionDelay);
		}

		Thread.sleep(120000);//pause so more user actions can be tested
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
