package ca.ucalgary.seng300.a2;

/**
 *This class ensures that any GUI display that implements it will have the methods contained in this interface
 *
 */
public interface GuiInterfaceDisplay {

	/**
	 * The message to update the display with
	 *
	 * @param message The message to display
	 */
	void updateMessage(String message);

}
