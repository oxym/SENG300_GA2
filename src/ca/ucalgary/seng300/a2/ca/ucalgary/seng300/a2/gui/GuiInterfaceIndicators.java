package ca.ucalgary.seng300.a2.gui;

/**
 * Provides a common interface for indicator buttons
 *
 */
public interface GuiInterfaceIndicators {

	/**
	 * A method to turn an indicator light on
	 * @param index Index of the indicator light
	 */
	void indicatorOn(int index);

	/**
	 * A method to turn an indicator light off
	 * @param index Index of the indicator light
	 */
	void indicatorOff(int index);


}
