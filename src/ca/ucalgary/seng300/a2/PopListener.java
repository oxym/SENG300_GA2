package ca.ucalgary.seng300.a2;

import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.*;

public class PopListener extends VendingListener implements PopCanRackListener, DeliveryChuteListener {

	protected static PopListener listener;
	protected static VendingManager mgr;
	
	protected PopListener(){}

	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 */
	static PopListener initialize(VendingManager manager){
		mgr = manager;
		listener = new PopListener();
		return getInstance();
	}

	/**
	 * Provides access to the singleton instance for package-internal classes.
	 * @return The singleton VendingListener instance
	 */
	static PopListener getInstance(){
		return listener;
	}
	
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
	@Override
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
	@Override
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		String popName = mgr.getPopCanRackName(popCanRack);
		mgr.log(popCan.getName() + " removed from " + popName + " rack.");
	}

	/*
	 * logs that the given pop can rack is full
	 * 
	 * @param curent pop can rack of interest
	 */
	@Override
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
	@Override
	public void popCansEmpty(PopCanRack popCanRack) {
		String popName = mgr.getPopCanRackName(popCanRack);
		mgr.log(popName + " rack empty.");
		if (mgr.checkAllProductsEmpty()){
			mgr.enableSafety();
		}
	}
//^^^=======================POP CAN RACK LISTENER METHODS END=======================^^^

//vvv=======================DELIVERY CHUTE LISTENER METHODS START=======================vvv
	//TODO Document
	@Override
	public void itemDelivered(DeliveryChute chute) {
		mgr.log("PopCan delivered to the Delivery Chute");
	}

	/*
	 * Handles the "notifyDoorOpened" event from the registered deliveryChute
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
	@Override
	public void doorClosed(DeliveryChute chute) {
		mgr.log("Delivery chute door closed");
		if (chute.hasSpace())
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
}
