package ca.ucalgary.seng300.a2;

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
