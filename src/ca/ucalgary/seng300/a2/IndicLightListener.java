package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

/**
 * This class is registered by VendingManager with hardware classes to listen for indicator light events
 * 
 * ACCESS: Only listener methods are public access. 
 * 
 * HANDLED EVENTS: 	enabled, disabled, light activatd, light deactivated
 */

public class IndicLightListener implements IndicatorLightListener{

	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activated(IndicatorLight light) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivated(IndicatorLight light) {
		// TODO Auto-generated method stub
		
	}

}
