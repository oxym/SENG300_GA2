package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

/**
 * Event-handling listener class for Lock.
 */
public class MachineLockListener extends VendingListener implements LockListener{

	private boolean isLocked;

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
		isLocked = true;
	}

	/**
	 * Handles the "unlocked" event from the registered Lock.
	 * Causes the vm to turn off safety-mode.
	 */
	@Override
	public void unlocked(Lock lock) {
		mgr.enableSafety();
		isLocked = false;
	}

	/**
	 * Returns the status of the lock
	 *
	 * @return status if hardware lock is locked
	 */
	public boolean isLocked() {
		return isLocked;
	}
//^^^=======================LOCK LISTENER METHODS END=======================^^^
}
