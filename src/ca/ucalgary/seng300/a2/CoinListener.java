package ca.ucalgary.seng300.a2;

import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.*;

/**
 * Event-handling listener class for CoinSlot, CoinReceptacle, CoinRack and CoinReturn.
 */
public class CoinListener extends VendingListener implements
							CoinSlotListener, CoinReceptacleListener,
							CoinRackListener, CoinReturnListener {

	protected static CoinListener listener;
	protected static VendingManager mgr;

	private CoinListener (){}

	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 */
	static CoinListener initialize(VendingManager manager){
		mgr = manager;
		listener = new CoinListener();
		return getInstance();
	}

	/**
	 * Provides access to the singleton instance for package-internal classes.
	 * @return The singleton VendingListener instance
	 */
	static CoinListener getInstance(){
		return listener;
	}

//vvv=======================COIN SLOT LISTENER METHODS START=======================vvv
	/**
	 * Logs that a coin was added to the CoinSlot, but was rejected
	 */
	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
		mgr.log("Coin (" + coin.getValue() + ") rejected by coin slot.");
	}

	/**
	 * Responds to "Valid coin inserted" notifications from the registered CoinSlot.
	 * Adds the value of the coin to the VendingManager's tracked credit.
	 * Toggles the "Exact Change" light according to whether exact change could be
	 * provide for each possible purchase.
	 */
	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		mgr.addCredit(coin.getValue());
		mgr.displayCredit();

		if (mgr.checkExactChangeState()){
			mgr.getExactChangeLight().deactivate();
			mgr.log("ExactChange light off");
		}
		else{
			mgr.getExactChangeLight().activate();
			mgr.log("ExactChange light on");
		}
	}
//^^^=======================COIN SlOT LISTENER METHODS END=======================^^^

//vvv=======================COIN RECEPTACLE LISTENER METHODS START=======================vvv
	//TODO Decide whether these events should be logged or handled
	@Override
	public void coinAdded(CoinReceptacle receptacle, Coin coin) {}
	@Override
	public void coinsLoaded(CoinReceptacle receptacle, Coin... coins) {}
	@Override
	public void coinsUnloaded(CoinReceptacle receptacle, Coin... coins) {}


	/* (non-Javadoc)
	 * @see org.lsmr.vending.hardware.CoinReceptacleListener#coinsRemoved(org.lsmr.vending.hardware.CoinReceptacle)
	 */
	@Override
	public void coinsRemoved(CoinReceptacle receptacle) {
		mgr.disableSafety(); //Safety may not be on and may not turn off
		mgr.log("Coins removed from coin receptacle.");
	}

	/* (non-Javadoc)
	 * @see org.lsmr.vending.hardware.CoinReceptacleListener#coinsFull(org.lsmr.vending.hardware.CoinReceptacle)
	 */
	@Override
	public void coinsFull(CoinReceptacle receptacle) {
		mgr.enableSafety();
		mgr.log("Coin receptacle full.");
	}
//^^^=======================COIN RECEPTACLE LISTENER METHODS END=======================^^^


//vvv=======================COIN RACK LISTENER METHODS START=======================vvv
	//TODO: Decide if these events should be logged or handled.
	@Override
	public void coinsLoaded(CoinRack rack, Coin... coins) {}
	@Override
	public void coinsUnloaded(CoinRack rack, Coin... coins) {}

	/*
	 * logs that the given coin value rack is full
	 *
	 * @param the given coin rack of interest
	 */
	@Override
	public void coinsFull(CoinRack rack) {
		int rackVal = mgr.getCoinRackValue(rack);
		mgr.log("Coin (" + rackVal + ") rack empty.");
	}

	/*
	 * logs that the given coin value rack is empty
	 *
	 * @param the given coin rack of interest
	 */
	@Override
	public void coinsEmpty(CoinRack rack) {
		int rackVal = mgr.getCoinRackValue(rack);
		mgr.log("Coin (" + rackVal + ") rack empty.");
	}

	/*
	  * logs that the given coin rack has had a coin value
	  * added to the rack
	  *
	  * @param the given coin rack of interest
	  * @param any arbritrary coin value
	  */
	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		int rackVal = mgr.getCoinRackValue(rack);
		mgr.log("Coin (" + coin.getValue() + ") added to (" + rackVal + ") rack.");
	}

	 /*
	  * logs that the given coin rack has had a coin value
	  * removed from the rack
	  *
	  * @param the given coin rack of interest
	  * @param any arbitrary coin value
	  */
	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		int rackVal = mgr.getCoinRackValue(rack);
		mgr.log("Coin (" + coin.getValue() + ") removed from (" + rackVal + ") rack.");
	}
//^^^=======================COIN RACK LISTENER METHODS END=======================^^^

//vvv=======================COIN RETURN LISTENER METHODS START=======================vvv

	/*
	 * Logs the coins that, one by one, has been returned to the user
	 *
	 * @param coinreturn object
	 * @param an array of the coins returned
	*/
	@Override
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins) {
		String coinStr = "";
		for (int i = 0; i < coins.length; i++){
			coinStr += coins[i].getValue();
			if (i != coins.length - 1){
				coinStr += " ";
			}
		}
		mgr.log("Coin(s) + (" + coinStr + ") delivered to coin return.");
	}

	@Override
	public void returnIsFull(CoinReturn coinReturn) {
		mgr.log("Coin return full");
		mgr.enableSafety();
	}

	//TODO Request coinsUnloaded() listener event from Mr. Client

//^^^=======================COIN RETURN LISTENER METHODS END=======================^^^
}
