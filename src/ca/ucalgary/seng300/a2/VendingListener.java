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
 * Assignment 1:
 * @author Raymond Tran (30028473)
 * @author Thomas Coderre (10169277)
 * @author Thobthai Chulpongsatorn (30005238)
 * 
 * Assignment 2:
 * See authorship in VendingManager.
 *
 */
public class VendingListener implements CoinSlotListener, SelectionButtonListener,
										LockListener, DeliveryChuteListener {
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

	// Currently unused listener events
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}

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

//vvv=======================COINSLOT LISTENER METHODS START=======================vvv
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
 
		if (mgr.checkExactChangeState())
			mgr.getExactChangeLight().deactivate();
		else
			mgr.getExactChangeLight().activate();
	}
//^^^=======================COINSlOT LISTENER METHODS END=======================^^^
	
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

	//TODO Document
	@Override
	public void doorOpened(DeliveryChute chute) {
		mgr.log("Delivery chute door opened");
	}
	
	//TODO Document
	@Override
	public void doorClosed(DeliveryChute chute) {
		mgr.log("Delivery chute door closed");
		
	}
	
	//TODO Document
	@Override
	public void chuteFull(DeliveryChute chute) {
		mgr.log("Delivery chute full");		
	}
//^^^=======================DELIVERY CHUTE LISTENER METHODS END=======================^^^
}
