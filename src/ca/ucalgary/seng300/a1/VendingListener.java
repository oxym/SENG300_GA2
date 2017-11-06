package ca.ucalgary.seng300.a2;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

/**
 * This class is registered by VendingManager with hardware classes to listen for hardware
 * events and perform first-pass checks and error-handling for them. Most "heavy-lifting" 
 * is completed within VendingManager.
 * 
 * ACCESS: Only listener methods are public access. 
 * 
 * HANDLED EVENTS: 	SelectionButtonListener: pressed() 
 *   				CoinSlotListener: ValidCoinInserted()
 * 
 * TODO: Divide this class into separate listener classes. Be sure to consult team first.
 * TEMPORARILY HANDLED EVENTS TYPES:
 * 	LockListener, DeliveryChute, PopCanRack, CoinRack, 
 *  CoinReceptacle, IndicatorLight
 *
 */
public class VendingListener implements CoinSlotListener, SelectionButtonListener,
										LockListener, DeliveryChuteListener,
										CoinRackListener, PopCanRackListener,
										CoinReceptacleListener, IndicatorLightListener
										{
	private static VendingListener listener;
	private static VendingManager mgr;
	
	private VendingListener (){}
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 */
	static void initialize(VendingManager manager){		
		if (manager != null){
			mgr = manager;
			listener = new VendingListener();
		}
	}
	
	/**
	 * Provides access to the singleton instance for package-internal classes.
	 * @return The singleton VendingListener instance  
	 */
	static VendingListener getInstance(){
		return listener;
	}

	
//vvv=======================TEMPLATE LISTENER METHODS START=======================vvv
	// AbstractHardwareListener (en-/dis-)able events are currently unused
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
//^^^=======================TEMPLATE LISTENER METHODS END=======================^^^

//vvv=======================BUTTON LISTENER METHODS START=======================vvv
	/**
	 * Responds to "pressed" notifications from registered SelectionButtons. 
	 * If no matching button is found in the VendingMachine, nothing is done.
	 * Uses the buy() method in VendingManager to process the purchase.
	 * All exceptions thrown by buy() are caught here (InsufficientFunds, Disabled, Empty, etc.) 
	 */
	@Override
	public void pressed(SelectionButton button) {
		int bIndex = mgr.getButtonIndex(button);
		
		String popName = mgr.getPopKindName(bIndex);
		mgr.log("Button for: " + popName + ", button: " + bIndex + " pressed.");
		
		if (bIndex == -1){
			//Then it's not a pop selection button. 
			//This may be where we handle "change return" button presses
		}
		else{
			try{
				//Assumes a 1-to-1, strictly ordered mapping between popIndex and and butttonindex
				mgr.buy(bIndex); 
			} catch(InsufficientFundsException e){
				mgr.display(e.toString(), 5);
			} catch(DisabledException e){
				mgr.display("Vending machine disabled", 5);
			} catch (EmptyException e){
				
				mgr.display(popName + " is out of stock.", 5);
			} catch (CapacityExceededException e){
				mgr.display("Delivery chute full", 5);
			}
		}		
	}
//^^^=======================BUTTON LISTENER METHODS END=======================^^^

//vvv=======================COIN SLOT LISTENER METHODS START=======================vvv
	//TODO: Document
	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
		mgr.log("Coin with value: " + coin.getValue() + " rejected by coin slot.");
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
	
//vvv=======================LOCK LISTENER METHODS START=======================vvv
	/**
	 * Handles the "locked" event from the registered Lock.
	 * Causes the vm to turn on safety-mode. 
	 */
	@Override
	public void locked(Lock lock) {
		mgr.disableSafety();	
	}
	
	/**
	 * Handles the "unlocked" event from the registered Lock.
	 * Causes the vm to turn off safety-mode.
	 */
	@Override
	public void unlocked(Lock lock) {
		mgr.enableSafety();		
	}
//^^^=======================LOCK LISTENER METHODS END=======================^^^
	
//vvv=======================DELIVERY CHUTE LISTENER METHODS START=======================vvv
	//TODO Document
	@Override
	public void itemDelivered(DeliveryChute chute) {
		//TODO Decide if an item entering the delivery chute should be logged
	}

	/*
	 * Handles the "notifyDoorOpened" event from the registered deliveryChute
	 * 
	 */
	@Override
	public void doorOpened(DeliveryChute chute) {
		mgr.log("Delivery chute door opened");
	}
	
	/*
	 * Handles the "notifyDoorClosed" event from the registered deliveryChute
	 * If there is room in the chute then keep the safety off
	 * 
	 */
	public void doorClosed(DeliveryChute chute) {
		mgr.log("Delivery chute door closed");
		if (chute.getCapacity() > chute.size())
			mgr.disableSafety();
	}
	
	/*
	 * Handles the "notifyChuteFull" event from the registered deliveryChute
	 * In the case that the chute is full, enable the safety of the machine
	 * 
	 */
	@Override
	public void chuteFull(DeliveryChute chute) {
		mgr.log("Delivery chute full");
		mgr.enableSafety();
	}
//^^^=======================DELIVERY CHUTE LISTENER METHODS END=======================^^^

//vvv=======================POP CAN RACK LISTENER METHODS START=======================vvv
	//TODO Decide whether these events should be logged or handled
	
	@Override
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {}
	@Override
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {}

	/*
	 * Disables the machines safety during the loading of the machine
	 * 
	 * @param curent pop can rack of interest
	 * @param curent pop can of interest 
	 */
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {
		mgr.disableSafety();
		//TODO Decide if we should log the added pop. 
		// Should only happen during loading, so maybe we'll just log it there.
	}
	
	/*
	 * Logs that the given pop has been removed from the rack
	 * 
	 *@param curent pop can rack of interest
	 *@param curent pop can of interest  
	 */
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		String popName = mgr.getPopCanRackName(popCanRack);
		mgr.log(popCan.getName() + " removed from " + popName + " rack.");	
	}
	
/*
 * logs that the given pop can rack is full
 * 
 * @param curent pop can rack of interest
 */
	public void popCansFull(PopCanRack popCanRack) {
		String popName = mgr.getPopCanRackName(popCanRack);
		mgr.log(popName + " rack full.");
	}
	
/*
 * logs that the given pop rack is empty. If all pop 
 * racks are empty then enable the safety of the machine.
 * 
 * @param curent pop can rack of interest 
 */
	public void popCansEmpty(PopCanRack popCanRack) {
		String popName = mgr.getPopCanRackName(popCanRack);
		mgr.log(popName + " rack empty.");
		if (mgr.checkAllProductsEmpty()){
			mgr.enableSafety();
		}
	}
//^^^=======================POP CAN RACK LISTENER METHODS END=======================^^^

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
	public void coinsFull(CoinRack rack) {
		int rackVal = mgr.getCoinRackValue(rack);
		mgr.log("Coin (" + rackVal + ") rack full.");
	}
	
	/*
	 * logs that the given coin value rack is empty
	 * 
	 * @param the given coin rack of interest
	 */	@Override
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
	  * @param any arbritrary coin value
	  */	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		int rackVal = mgr.getCoinRackValue(rack);
		mgr.log("Coin (" + coin.getValue() + ") removed from (" + rackVal + ") rack.");
	}
//^^^=======================COIN RACK LISTENER METHODS END=======================^^^

//vvv=======================COIN RECEPTACLE LISTENER METHODS START=======================vvv
	//TODO Decide whether these events should be logged or handled 
	@Override
	public void coinsLoaded(CoinReceptacle receptacle, Coin... coins) {}
	@Override
	public void coinsUnloaded(CoinReceptacle receptacle, Coin... coins) {}

	/*
	 * Log that a coin has been added to the storage bin. This should happen
	 * when there is no more room in the coin rack? 
	 * 
	 * @param Current coin receptacle
	 * @param any arbritrary coin value
	 */
	@Override
	public void coinAdded(CoinReceptacle receptacle, Coin coin) {
		if (receptacle == mgr.getStorageBin()){
			mgr.log("Coin (" + coin.getValue() + ") added to storage bin");
		}
		else{
			//TODO: Decide whether to log when the coin enters the "holding" coin receptacle.
		}
	}

	//TODO Document
	@Override
	public void coinsRemoved(CoinReceptacle receptacle) {
		mgr.disableSafety();
		String message= (receptacle == mgr.getStorageBin())
				? "Coins removed from storage bin."
				: "Coins removed from coin receptacle.";
		mgr.log(message);
	}
	
	//TODO Document
	@Override
	public void coinsFull(CoinReceptacle receptacle) {
		//NOTE: safety is being enabled when storage bin is full, even if
		// there is still room in some coin racks. MAY BE CHANGED.
		mgr.enableSafety();
		String message= (receptacle == mgr.getStorageBin())
				? "Storage bin full."
				: "Coin receptacle full.";
		mgr.log(message);
	}
//^^^=======================COIN RECEPTACLE LISTENER METHODS END=======================^^^

//vvv=======================INDICATOR LIGHT LISTENER METHODS START=======================vvv
	//TODO Document
	@Override
	public void activated(IndicatorLight light) {
		String message;
		if (light == mgr.getExactChangeLight())
			message = "Exact change light turned on.";
		else if (light == mgr.getOutOfOrderLight())
			message = "Out of order (safety) light turned on.";
		else
			message = "Unknown light turned on.";
		mgr.log(message);
	}
	
	//TODO Document
	@Override
	public void deactivated(IndicatorLight light) {
		String message;
		if (light == mgr.getExactChangeLight())
			message = "Exact change light turned off.";
		else if (light == mgr.getOutOfOrderLight())
			message = "Out of order (safety) light turned off.";
		else
			message = "Unknown light turned off.";
		mgr.log(message);		
	}
//^^^=======================INDICATOR LIGHT LISTENER METHODS END=======================^^^	

	
//vvv=======================TEMPLATE LISTENER METHODS START=======================vvv
//^^^=======================TEMPLATE LISTENER METHODS END=======================^^^	
}
