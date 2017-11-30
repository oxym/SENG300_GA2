package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a2.gui.GuiInterfaceIndicators;

/**
 * Event-handling listener class for IndicatorLight.
 */
public class LightListener extends VendingListener implements IndicatorLightListener {

	protected static LightListener listener;
	protected static VendingManager mgr;

	private GuiInterfaceIndicators guiIndicators;
	private boolean guiIndicatorsPresent = false;

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

	// vvv=======================INDICATOR LIGHT LISTENER METHODS
	// START=======================vvv

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
			if (guiIndicatorsPresent)
				guiIndicators.indicatorOn(MachineConfiguration.EXACT_CHANGE);
		} else if (light == mgr.getOutOfOrderLight()) {
			message = "Out of order (safety) light turned on.";
			if (guiIndicatorsPresent)
				guiIndicators.indicatorOn(MachineConfiguration.OUT_OF_ORDER);
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
			if (guiIndicatorsPresent)
				guiIndicators.indicatorOff(MachineConfiguration.EXACT_CHANGE);
		} else if (light == mgr.getOutOfOrderLight()) {
			message = "Out of order (safety) light turned off.";
			if (guiIndicatorsPresent)
				guiIndicators.indicatorOff(MachineConfiguration.OUT_OF_ORDER);
		} else
			message = "Unknown light turned off.";
		mgr.log(message);
	}

	/**
	 * Attaches a gui Indicator Lights
	 *
	 * @param guiIndicators
	 *            guiIndicator object
	 */
	public void attachGuiIndicators(GuiInterfaceIndicators guiIndicators) {
		this.guiIndicators = guiIndicators;
		guiIndicatorsPresent = true;
	}
	// ^^^=======================INDICATOR LIGHT LISTENER METHODS
	// END=======================^^^
}
