package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a2.gui.GuiInterfaceDisplay;

/**
 * This class is registered by VendingManager with hardware classes to listen
 * for display events.
 * The current and last message are stored and can be obtained by the public accessors.
 *
 * HANDLED EVENTS: messageChange
 *
 * NOTES: In the messageChange, the display that had the message changed is
 * ignored for now.
 *
 */
public class DispListener extends VendingListener implements DisplayListener {

	private String messageLast = "";
	private String messageCurrent = "";
	private VendingManager mgr;

	/**
	 * @param manager The vending machine manager
	 */
	public DispListener(VendingManager manager) {
		mgr = manager;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.lsmr.vending.hardware.DisplayListener#messageChange(org.lsmr.vending.
	 * hardware.Display, java.lang.String, java.lang.String)
	 */
	@Override
	public void messageChange(Display display, String oldMessage, String newMessage) {
		messageLast = oldMessage;
		messageCurrent = newMessage;
		
		if (mgr != null){
			//Update GUI
			if (display == mgr.getDisplay()){ //If it's the user display
				mgr.guiUpdateUserDisplay(newMessage);			
			} else if (display == mgr.getConfigurationPanel().getDisplay()){ //If it's the config display
				mgr.guiUpdateConfigDisplay(newMessage); 
			}
					
			String greeting = DisplayDriver.getGreeetingMessage();
			if (newMessage != (null) && !newMessage.equals("") && !newMessage.equals(greeting)){
					mgr.log("Message displayed: " + newMessage);
			}
		}
	}

	/**
	 * Returns the last message displayed
	 *
	 * @return lastMessage
	 */
	public String getLastMessage() {
		return messageLast;
	}

	/**
	 * Returns the currently displaying message
	 *
	 * @return Current Message
	 */
	public String getCurrentMessage() {
		return messageCurrent;
	}
}
