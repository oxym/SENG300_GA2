package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.Display;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A driver for the display that will handle the display of messages, including
 * timing and scheduling messages and updating the display
 */
public class DisplayDriver {

	private static boolean TESTING = true;
	private static String MSG_DEFAULT = "Hi there!";
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
	}

	/**
	 * Cancels the currently executing timer and tasks, and instantiates a new timer
	 */
	private void cancelCycle(){
	    timer.cancel();
	    timer = new Timer();
	}

	/**
	 * Displays a new message on the display
	 *
	 * @param message
	 *            The message to display
	 */
	public void newMessage(String message) {
		cancelCycle();
		display.display(message);
		if (TESTING)
			System.out.println(message);
	}

	/**
	 * Displays a new message for some arbitrary duration on the display
	 *
	 * @param message The message to display
	 * @param duration The duration of the message to display
	 */
	public void newMessage(String message, int duration) {
		cancelCycle();
		TaskDisplayMessage newMessageTask = new TaskDisplayMessage(message, display);
		timer.schedule(newMessageTask, duration * 1000);
		if (TESTING)
			System.out.println(message);
	}

	/**
	 * Displays the default message The default message is displayed for set time
	 * and is then blank for the remainder of the cycle time
	 */
	public void defaultMessage() {
		cancelCycle();
		defaultTask = new TaskDisplayMessage(MSG_DEFAULT, display);
		TaskDisplayClear clearTask = new TaskDisplayClear(display);
		timer.scheduleAtFixedRate(defaultTask, 0, greetingCycleTime * 1000);
		timer.scheduleAtFixedRate(clearTask, greetingTime * 1000, greetingCycleTime * 1000);
	}

	/**
	 * Clears the display
	 *
	 */
	public void clearMessage() {
		cancelCycle();
		display.display("");
		if (TESTING)
			System.out.println("<clear display>");
	}

	/**
	 * Clears the display and stops displaying the default message
	 */
	public void displayOff() {
		cancelCycle();
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
	private class TaskDisplayMessage extends TimerTask {
		private String message = "";
		private Display display;

		/**
		 * @param message The greeting message to display
		 * @param display The display where the message is displayed
		 * @param greetingTime The number of seconds that the message is displayed before clearing
		 */
		TaskDisplayMessage(String message, Display display) {
			this.message = message;
			this.display = display;
		}

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			display.display(message);
			if (TESTING) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
				String a = LocalDateTime.now().format(format);
				System.out.println(message + ": " + a);
			}
		}
	}

	/**
	 * Inner Class for the a timer task that clears the display
	 *
	 */
	private class TaskDisplayClear extends TimerTask {
		private String message = "";
		private Display display;

		/**
		 * @param display The display where the message is displayed
		 */
		TaskDisplayClear(Display display) {
			this.display = display;
		}

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			display.display(""); // clear display
			if (TESTING) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
				String a = LocalDateTime.now().format(format);
				System.out.println("<clear display>: " + a);
			}
		}
	}

}