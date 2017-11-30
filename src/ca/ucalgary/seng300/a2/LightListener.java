package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a2.gui.GuiInterfaceIndicators;

/**
 * Event-handling listener class for IndicatorLight.
 */
public class LightListener extends VendingListener implements IndicatorLightListener {

	protected static LightListener listener;
	protected static VendingManager mgr;

	private LightListener() {}

	/**
	 * Forces the existing singleton instance to be replaced. Called by
	 * VendingManager during its instantiation.
	 */
	static LightListener initialize(VendingManager manager) {
		mgr = manager;
		listener = new LightListener();
		return getInstance();
	}

	/**
	 * Provides access to the singleton instance for package-internal classes.
	 *
	 * @return The singleton VendingListener instance
	 */
	static LightListener getInstance() {
		return listener;
	}

	// vvv=======================INDICATOR LIGHT LISTENER METHODS START=======================vvv

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.lsmr.vending.hardware.IndicatorLightListener#activated(org.lsmr.vending.
	 * hardware.IndicatorLight)
	 */
	@Override
	public void activated(IndicatorLight light) {
		String message;
		if (light == mgr.getExactChangeLight()) {
			message = "Exact change light turned on.";
			mgr.guiSetChangeLight(true);	
		} else if (light == mgr.getOutOfOrderLight()) {
			message = "Out of order (safety) light turned on.";
			mgr.guiSetOutOfOrderLight(true);
		} else
			message = "Unknown light turned on.";
		mgr.log(message);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.lsmr.vending.hardware.IndicatorLightListener#deactivated(org.lsmr.vending
	 * .hardware.IndicatorLight)
	 */
	@Override
	public void deactivated(IndicatorLight light) {
		String message;
		
		if (light == mgr.getExactChangeLight()) {
			message = "Exact change light turned off.";
			mgr.guiSetChangeLight(false);
			
		} else if (light == mgr.getOutOfOrderLight()) {
			message = "Out of order (safety) light turned off.";
			mgr.guiSetOutOfOrderLight(false);
		} else
			message = "Unknown light turned off.";
		mgr.log(message);
	}

	// ^^^=======================INDICATOR LIGHT LISTENER METHODS END=======================^^^
}
