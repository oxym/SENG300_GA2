package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.Display;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A driver for the display that will handle the display of messages, including
 * timing and scheduling messages and updating the display
 */
public class DisplayDriver {

	private final static boolean TESTING = true;
	private final static String MSG_DEFAULT = "Hi there!";
	private int greetingCycleTime = 15; // in seconds
	private int greetingTime = 5;  // in seconds
	private Timer timer;
	private TimerTask defaultTask;

	private Display display;

	/**
	 * @param display
	 *            the display object that is driven/controlled by this class
	 */
	public DisplayDriver(Display display) {
		this.display = display;
		timer = new Timer();
		defaultTask = new TaskDisplayGreeting(MSG_DEFAULT, display, greetingCycleTime);
	}

	/**
	 * Displays a new message on the display
	 *
	 * @param message
	 *            The message to display
	 */
	public void newMessage(String message) {
		defaultTask.cancel();
		timer.purge();
		display.display(message);
		if (TESTING)
			System.out.println(message);
	}

	/**
	 * Displays the default message The default message is displayed for set time
	 * and is then blank for the remainder of the cycle time
	 */
	public void defaultMessage() {
		defaultTask.cancel();
		timer.purge();
		defaultTask = new TaskDisplayGreeting(MSG_DEFAULT, display, greetingTime);
		timer.scheduleAtFixedRate(defaultTask, 0, greetingCycleTime * 1000);// 15 second interval
	}

	/**
	 * Clears the display
	 *
	 */
	public void clearMessage() {
		defaultTask.cancel();
		timer.purge();
		display.display("");
		if (TESTING)
			System.out.println("<clear display>");
	}

	/**
	 * Clears the display and stops displaying the default message
	 */
	public void displayOff() {
		defaultTask.cancel();
		timer.purge();
		clearMessage();
	}

	/**
	 * Set the cycle timer on the
	 * @param seconds
	 */
	public void setGreetingCycleTime (int seconds) {
		greetingCycleTime = seconds;
	}

	/**
	 * Inner Class for the default message task
	 *
	 */
	private class TaskDisplayGreeting extends TimerTask {
		private String message = "";
		private Display display;

		/**
		 * @param message The greeting message to display
		 * @param display The display where the message is displayed
		 * @param greetingTime The number of seconds that the message is displayed before clearing
		 */
		TaskDisplayGreeting(String message, Display display, int greetingTime) {
			this.message = message;
			this.display = display;
		}

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			display.display(message); // display greeting message for 5 seconds
			if (TESTING)
				System.out.println(message);
			try {
				Thread.sleep(greetingTime * 1000);
			} catch (InterruptedException e) {
				//should not get here
				e.printStackTrace();
			}

			display.display(""); // clear display for remainder of cycle
			if (TESTING)
				System.out.println("<clear display>");
		}
	}

}