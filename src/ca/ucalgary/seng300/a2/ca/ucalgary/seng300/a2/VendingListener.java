package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

/**
 * Acts as the superclass for the various listener classes.
 * Handles the enabled and disabled methods common to all AbstractHardwareListeners.  
 *
 */
public abstract class VendingListener implements AbstractHardwareListener {
	private static boolean debug = false;

//vvv=======================ABSTRACT HARDWARE LISTENER METHODS START=======================vvv
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		if (debug)
			System.out.println("Enabled: " + hardware.getClass().getSimpleName());
	}
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		if (debug)
			System.out.println("Enabled: " + hardware.getClass().getSimpleName());
	}
//^^^=======================ABSTRACT HARDWARE LISTENER METHODS END=======================^^^
}
