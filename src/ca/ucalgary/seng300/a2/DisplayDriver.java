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

	private VendingManager mgr;
	private Display display;

	/**
	 * @param display
	 *            the display object that is driven/controlled by this class
	 */
	public DisplayDriver(Display display) {
		this.display = display;
		mgr = VendingManager.getInstance();
		timer = new Timer();
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
		if (mgr != null)
			VendingManager.getInstance().log("Displayed message: " + message);
		if (TESTING)
			System.out.println(message);
	}

	/**
	 * Displays a new message for some arbitrary duration on the display.
	 * After the given duration, either the then-current credit will be shown,
	 * or the flashing greeting screen will be shown.
	 *
	 * @param message
	 *            The message to display
	 * @param duration
	 *            The duration to display the message for
	 */
	public void newMessage(String message, int duration) {
		newMessage(message);

		if (mgr != null)
			VendingManager.getInstance().log("Waiting for: " + duration + " seconds.");
		if (TESTING) {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
			String time = LocalDateTime.now().format(format);
			System.out.println("Waiting for: " + duration + " at: " + time);
		}


		int delay = duration * 1000;
		if (mgr != null && VendingManager.getInstance().getCredit() > 0 ){
			DisplayMessageTask messageTask = getMessageTask("$CREDIT$");
			timer.schedule(messageTask, delay);
		}
		else { //Restore greeting message
			DisplayMessageTask messageTask = getMessageTask(MSG_DEFAULT);
			timer.scheduleAtFixedRate(messageTask, delay, greetingCycleTime * 1000);
			timer.scheduleAtFixedRate(getClearTask(), delay + greetingDuration * 1000, greetingCycleTime * 1000);
		}
	}

	/**
	 * Creates a new message task
	 *
	 * @param message The message to display
	 * @return message task that displays the message
	 */
	DisplayMessageTask getMessageTask(String message){
		return new DisplayMessageTask(message, display);
	}

	/**
	 * Creates a new task that clears the display
	 *
	 * @return task to clear the display
	 */
	DisplayMessageTask getClearTask(){
		return new DisplayMessageTask("", display);
	}
	/**
	 * Displays the default message The default message is displayed for set time
	 * and is then blank for the remainder of the cycle time
	 */
	public void greetingMessage() {
		cancelCycle();
		DisplayMessageTask messageTask = getMessageTask(MSG_DEFAULT);
		timer.scheduleAtFixedRate(messageTask, 0, greetingCycleTime * 1000);
		timer.scheduleAtFixedRate(getClearTask(), greetingDuration * 1000, greetingCycleTime * 1000);
	}

	/**
	 * Clears the display indefinitely.
	 * @deprecated This method should not be used. The display should never be perma-cleared.
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
	 * @param duration
	 */
	public void setGreetingCycleTime(int duration, int cycleTime) {
		greetingDuration = duration;
		greetingCycleTime = cycleTime;
	}

	/**
	 * Gets the greeting message which is displayed when the vending machine is
	 * idle and has no credit stored.
	 * @return The default (greeting) message.
	 */
	public static String getGreeetingMessage(){
		return MSG_DEFAULT;
	}

	/**
	 * Inner Class for the display message task
	 *
	 */
	private class DisplayMessageTask extends TimerTask {
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
		DisplayMessageTask(String message, Display display) {
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
			if (message.equals("$CREDIT$")){
				display.display(VendingManager.getInstance().getCreditMessage());
			}
			else{
				display.display(message);
			}
			if (TESTING) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
				String time = LocalDateTime.now().format(format);
				String logEntry = message.equals("") ? "<clear display>: " : message + ": ";
				System.out.println(logEntry + time);
			}
		}
	}
}