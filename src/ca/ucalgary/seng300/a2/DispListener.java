package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

/**
 * This class is registered by VendingManager with hardware classes to listen
 * for display events and return various messages and the status.
 *
 * ACCESS: Only listener methods are public access.
 *
 * HANDLED EVENTS: messageChange, enabled, disabled
 *
 *
 * NOTES: In the messageChange, the the display that had the message changed is
 * ignored for now.
 *
 */
public class DispListener extends VendingListener implements DisplayListener {

	private String messageLast = "";
	private String messageCurrent = "";
	private VendingManager mgr;
	
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
		String greeting = DisplayDriver.getGreeetingMessage();
		if (newMessage != (null) && !newMessage.equals("") && !newMessage.equals(greeting)) 
			if (mgr != null) mgr.log("Message displayed: " + newMessage);
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
