package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

/**
 * Event-handling listener class for PushButton.
 */
public class ButtonListener extends VendingListener implements PushButtonListener{

	private static ButtonListener listener;
	private static VendingManager mgr;
	
	private ButtonListener(){}

	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 */
	static ButtonListener initialize(VendingManager manager){
		mgr = manager;
		listener = new ButtonListener();
		return getInstance();
	}

	/**
	 * Provides access to the singleton instance for package-internal classes.
	 * @return The singleton VendingListener instance
	 */
	static ButtonListener getInstance(){
		return listener;
	}
	
//vvv=======================BUTTON LISTENER METHODS START=======================vvv
	/**
	 * Responds to "pressed" notifications from registered PushButtons.
	 * If no matching button is found in the VendingMachine, nothing is done.
	 * Uses the buy() method in VendingManager to process the purchase.
	 * All exceptions thrown by buy() are caught here (InsufficientFunds, Disabled, Empty, etc.)
	 */
	@Override
	public void pressed(PushButton button) {
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
				VendingManager.getProductHandler().buy(bIndex);
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
}
