package ca.ucalgary.seng300.a2;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

/**
 * This class is registered by VendingManager with hardware classes to listen for hardware
 * events and perform first-pass checks and error-handling for them. Most "heavy-lifting"
 * is completed within VendingManager.
 *
 * ACCESS: Only listener methods are public access.
 *TODO Revamp class documentation
 */
public class VendingListener implements AbstractHardwareListener {
	private static boolean debug = false;

//vvv=======================ABSTRACT HARDWARE LISTENER METHODS START=======================vvv
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		if (debug)
			System.out.println("[debug] hardware was enabled");
	}
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		if (debug)
			System.out.println("[debug] hardware was disabled");
	}
//^^^=======================ABSTRACT HARDWARE LISTENER METHODS END=======================^^^

}
