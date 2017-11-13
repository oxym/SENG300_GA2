package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

public class MachineLockListener extends VendingListener implements LockListener{

	protected static MachineLockListener listener;
	protected static VendingManager mgr;
	
	protected MachineLockListener(){}

	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 */
	static MachineLockListener initialize(VendingManager manager){
		mgr = manager;
		listener = new MachineLockListener();
		return getInstance();
	}

	/**
	 * Provides access to the singleton instance for package-internal classes.
	 * @return The singleton VendingListener instance
	 */
	static MachineLockListener getInstance(){
		return listener;
	}
	
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
}
