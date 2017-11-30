package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.CoinRack;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.EmptyException;

public class CreditHandler {
	private int credit = 0;
	private static String currency = "CAD";
	private VendingManager mgr;
	
	protected CreditHandler(VendingManager manager){
		mgr = manager;
		credit = 0;
	}

	/**
	 * Gets the credit available for purchases, in cents.
	 * Public access for testing and external access.
	 * It is assumed to not be a security vulnerability.
	 * @return The stored credit, in cents.
	 */
	public int getCredit(){
		return credit;
	}
	
	public String getCurrency(){
		return currency;
	}
	
	/**
	 * Adds value to the tracked credit.
	 * @param added The credit to add, in cents.
	 */
	void addCredit(int added){
		credit += added;
		mgr.log("Credit added:" + added);
	}

	/**
	 * Subtracts value to the tracked credit.
	 * @param subtracted The credit to add, in cents.
	 */
	void subtractCredit(int subtracted){
		credit -= subtracted;
		mgr.log("Credit removed:" + subtracted);
	}
	
	/**
	 * A method for returning change.
	 * May not return exact change.
	 * Dispenses the largest denominations first.
	 * NOTE: This method will only return change in the case where cash is used,
	 * 		since that is the only way that the credit will exceed the cost.
	 *
	 * @throws DisabledException Some necessary hardware is disabled
	 * @throws EmptyException CoinSlot is empty and a coin removal was attempted
	 * @throws CapacityExceededException DeliveryChute is full
	 */
	void returnChange() throws CapacityExceededException, EmptyException, DisabledException{
		int[] rackValues = mgr.getDescendingCoinRackValues();
		int coinVal = 0;
		CoinRack rack;
		for (int i=0; i < rackValues.length; i++){
			coinVal = rackValues[i];
			rack = mgr.getCoinRackForCoinKind(coinVal);

			while (getCredit() >= coinVal && rack.size() != 0){
				rack.releaseCoin();
				subtractCredit(coinVal);
			}

			if (getCredit() == 0){
				break;
			}
		}
	}

	/**
	 * Checks if valid change can be returned, but does not return anything.
	 * Similar to returnChange, but sets the indicator light instead
	 * @param cost The cost (in cents) of a hypothetical purchase
	 * @return Whether exact change could be provided for an item of the given cost
	 */
	boolean canReturnExactChange(int cost){
		boolean exact = true;

		int excess = getCredit() - cost; // i.e. credit after the possible purchase

		int rackCount = mgr.getNumberOfCoinRacks();
		int[] rackValues = mgr.getDescendingCoinRackValues();

		//Populate CoinRack count array
		int[] rackAmounts = new int[mgr.getNumberOfCoinRacks()];
		for (int i=0; i < rackCount; i++){
			rackAmounts[i] = mgr.getCoinRackForCoinKind(rackValues[i]).size();
			if (mgr.isDebug()) System.out.println("CoinRack (value: " + rackValues[i]
										+ ") has " + rackAmounts[i] + " coins.");
		}
		//Try to reduce the excess credit to 0
		for (int i=0; i < rackCount; i++){
			while (excess >= rackValues[i] && rackAmounts[i] != 0){
				excess -= rackValues[i];
				rackAmounts[i]--;
			}
			if (excess == 0){
				if (mgr.isDebug()) System.out.println("Correct Change");
				break;
			}
		}

		//If credit remains, inexact change would need to be be provided
		if (excess > 0){
			exact = false;
			if (mgr.isDebug()) System.out.println("Wrong change");
		}

		return exact;
	}
	
	/**
	 * Checks that exact change could be provided for each possible purchase,
	 * given the current credit.
	 * @return True if exact change can be provided for each purchase
	 */
	public boolean checkExactChangeState(){
		boolean exact = true;
		int rackCount = mgr.getNumberOfProductRacks();

		int popCost;
		for (int i = 0; i < rackCount; i++){
			popCost = mgr.getProductCost(i);
			exact = canReturnExactChange(popCost);
			if (!exact)
				break;
		}

		return exact;
	}
	
	/**
	 * Returns a formatted string to display credit.
	 * @return The formatted credit string.
	 */
	public String getCreditMessage(){
		String message;

		//Prettify the message for known currencies.
		if (getCurrency().equals("CAD") || getCurrency().equals("USD")){
			int dollars = getCredit() / 100;
			int cents = getCredit() % 100;
			message = String.format("Credit: $%3d.%02d", dollars, cents);
		}
		else{
			message = "Credit: " + getCredit();
		}

		return message;
	}

}
