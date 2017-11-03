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

	private final static boolean TESTING = true;
	private final static String MSG_DEFAULT = "Hi there!";
	private int greetingCycleTime = 15; // in seconds
	private int greetingDuration = 5; // in seconds
	private Timer timer;
	private TimerTask messageTask;
	private TimerTask clearTask;

	private Display display;

	/**
	 * @param display
	 *            the display object that is driven/controlled by this class
	 */
	public DisplayDriver(Display display) {
		this.display = display;
		timer = new Timer();
		clearTask = new TaskDisplayClear(display);
	}

	/**
	 * Cancels the currently executing timer and tasks, and instantiates a new timer
	 */
	private void cancelCycle() {
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
	 * @param message
	 *            The message to display
	 * @param duration
	 *            The duration of the message to display
	 * @param display
	 *            greeting after time expired
	 */
	public void newMessage(String message, int duration, Boolean resumeGreeting) {
		cancelCycle();
		display.display(message);
		if (TESTING) {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
			String time = LocalDateTime.now().format(format);
			System.out.println(message + " " + time);
		}

		// clear the task after the specified time
		TimerTask newClearTask = new TaskDisplayClear(display);
		timer.schedule(newClearTask, duration * 1000);

		// resume the regular greeting message
		if (resumeGreeting) {
			messageTask = new TaskDisplayMessage(MSG_DEFAULT, display);
			int newStart = duration * 1000;
			timer.scheduleAtFixedRate(messageTask, newStart, greetingCycleTime * 1000);
			timer.scheduleAtFixedRate(clearTask, newStart + greetingDuration * 1000, greetingCycleTime * 1000);
		}
	}

	/**
	 * Displays the default message The default message is displayed for set time
	 * and is then blank for the remainder of the cycle time
	 */
	public void greetingMessage() {
		cancelCycle();
		messageTask = new TaskDisplayMessage(MSG_DEFAULT, display);
		timer.scheduleAtFixedRate(messageTask, 0, greetingCycleTime * 1000);
		timer.scheduleAtFixedRate(clearTask, greetingDuration * 1000, greetingCycleTime * 1000);
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
	 * Set the cycle timer on the
	 *
	 * @param seconds
	 */
	public void setGreetingCycleTime(int duration, int cycleTime) {
		greetingCycleTime = cycleTime;
		greetingDuration = cycleTime;
	}

	/**
	 * Inner Class for the display message task
	 *
	 */
	private class TaskDisplayMessage extends TimerTask {
		private String message = "";
		private Display display;

		/**
		 * @param message
		 *            The greeting message to display
		 * @param display
		 *            The display where the message is displayed
		 * @param greetingDuration
		 *            The number of seconds that the message is displayed before
		 *            clearing
		 */
		TaskDisplayMessage(String message, Display display) {
			this.message = message;
			this.display = display;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			display.display(message);
			if (TESTING) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
				String time = LocalDateTime.now().format(format);
				System.out.println(message + ": " + time);
			}
		}
	}

	/**
	 * Inner Class for the a timer task that clears the display
	 *
	 */
	private class TaskDisplayClear extends TimerTask {
		private Display display;

		/**
		 * @param display
		 *            The display where the message is displayed
		 */
		TaskDisplayClear(Display display) {
			this.display = display;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			display.display(""); // clear display
			if (TESTING) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
				String time = LocalDateTime.now().format(format);
				System.out.println("<clear display>: " + time);
			}
		}
	}

}