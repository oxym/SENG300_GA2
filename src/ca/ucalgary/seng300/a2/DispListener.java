package ca.ucalgary.seng300.a2;

import java.util.ArrayList;

import org.lsmr.vending.*;
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
public class DispListener implements DisplayListener {

	private String status;
	private String messageLast = "";
	private String messageCurrent = "";
	private VendingManager vm;
	
	public DispListener() {
		status = "Initializing...";
		vm = VendingManager.getInstance();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.lsmr.vending.hardware.AbstractHardwareListener#enabled(org.lsmr.vending.
	 * hardware.AbstractHardware)
	 */
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		status = "enabled";

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.lsmr.vending.hardware.AbstractHardwareListener#disabled(org.lsmr.vending.
	 * hardware.AbstractHardware)
	 */
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		status = "disabled";

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
		if (vm != null) vm.log("Message displayed: " + newMessage);
	}

	/**
	 * Returns the status of the display
	 *
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Returns the last message displayed
	 *
	 * @return lastMessage
	 */
	public String getMessageLast() {
		return messageLast;
	}

	/**
	 * Returns the currently displaying message
	 *
	 * @return Current Message
	 */
	public String getMessageCurrent() {
		return messageCurrent;
	}
}
