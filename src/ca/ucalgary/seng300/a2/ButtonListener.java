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
		int sIndex = mgr.getSelectionButtonIndex(button);

		String productName = mgr.getProductName(sIndex);
		mgr.log("Button for: " + productName + ", button: " + sIndex + " pressed.");
		
		if (sIndex == -1){
			int cIndex = mgr.getConfigButtonIndex(button); 
			if (cIndex != -1){ 
				mgr.getConfigPanelHandler().pressKey(cIndex);
				//TODO Handle config panel input
				//NOTE: The "Enter" button has index 38, even though there
				//is no such index in the ConfigurationPanel's buttton array
			}
		}
		else{
			try{
				//Assumes a 1-to-1, strictly ordered mapping between popIndex and and butttonindex
				mgr.getProductHandler().buy(sIndex);
			} catch(InsufficientFundsException e){
				mgr.display(e.toString(), 5);
			} catch(DisabledException e){
				mgr.display("Vending machine disabled", 5);
			} catch (EmptyException e){
				mgr.display(productName + " is out of stock.", 5);
			} catch (CapacityExceededException e){
				mgr.display("Delivery chute full", 5);
			}
		}
	}
//^^^=======================BUTTON LISTENER METHODS END=======================^^^	
}
