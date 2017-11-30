package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.EmptyException;

public class ProductHandler {
	private VendingManager mgr;
	
	public ProductHandler(VendingManager manager){
		mgr = manager;
	}
	
	/**
	 * Handles a pop purchase. Checks if the pop rack has pop, confirms funds available,
	 *  dispenses the pop, reduces available funds and deposits the added coins into storage.
	 * @param popIndex The index of the selected pop rack.
	 * @throws InsufficientFundsException Thrown if credit < cost.
	 * @throws EmptyException Thrown if the selected pop rack is empty.
	 * @throws DisabledException Thrown if the pop rack or delivery chute is disabled.
	 * @throws CapacityExceededException Thrown if the delivery chute is full.
	 */
	void buy(int popIndex) throws InsufficientFundsException, EmptyException,
											DisabledException, CapacityExceededException {
		int cost = mgr.getPopKindCost(popIndex);

		if (mgr.getCreditHandler().getCredit() >= cost){
			mgr.getPopCanRack(popIndex).dispensePopCan(); //Will throw EmptyException if pop rack is empty
			mgr.getCreditHandler().subtractCredit(cost); //Will only be performed if the pop is successfully dispensed.
			if (mgr.isGUIEnabled()) {
				//TODO: update the gui delivery chute
			}

			//These coin-related actions may need to be nested in a conditional once additional
			//Payment methods are supported. It depends on whether change is returned automatically.
			mgr.getCoinReceptacle().storeCoins();
			mgr.returnChange();

			if (mgr.getCreditHandler().getCredit() > 0) {
				mgr.displayCredit();
			} else {
				mgr.display("Thank you for your purchase!", 3);
			}

		} else { //Not enough credit
			int diff = cost - mgr.getCreditHandler().getCredit();
			String popName = mgr.getPopKindName(popIndex);
			throw new InsufficientFundsException("Cannot buy " + popName + ". " + diff + " cents missing.");
		}
	}
}

