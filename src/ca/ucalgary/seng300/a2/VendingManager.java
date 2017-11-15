package ca.ucalgary.seng300.a2;

import java.util.Arrays;

import org.lsmr.vending.hardware.*;
import java.io.FileNotFoundException;


/**
 * VendingManager is the primary access-point for the logic controlling the
 * VendingMachine hardware.
 *
 * USAGE: Pass VendingMachine to static method initialize(), which returns the singleton instance
 * Later, you can use getInstance() to get the existing instance. Listeners are registered automatically.
 *
 * DESIGN: All listeners except DispListiner are designed as singletons.
 * Currently, the only public-access methods are for initialization. This was done to enforce
 * strong encapsulation of the logic classes in an attempt to prevent misuse.
 *
 *
 * LAYOUT:
 * 	SETUP: Sets up the VendingManager and related classes
 * 	HARDWARE ACCESSORS: Methods to get hardware instances or information about them
 * 	HARDWARE ACTIONS: Methods to interact with the hardware in some way
 * 	LOGIC INTERNALS: Methods that do not modify the hardware directly
 *
 * @author Thomas Coderre (10169277)
 * @author Jason De Boer (30034428)
 * @author Khesualdo Condori (30004958)
 * @author Michaela Olsakova (30002591)
 * @author Paul Dan (30011349)
 * @author Dan Dunareanu (30002346)
 *
 */
public class VendingManager {
	private static boolean debug = false;

	private static VendingManager mgr;
	private static VendingMachine vm;
	private static DisplayDriver displayDriver;

	private static DispListener displayListener;
	private static PopListener popListener;
	private static ButtonListener buttonListener;
	private static CoinListener coinListener;
	private static LightListener lightListener;
	private static MachineLockListener lockListener;

	private static Logger eventLog;
	private static String eventLogName = "VendingLog.txt";

	private int credit = 0;
	private static String currency = "CAD";

	private static Lock lock;


//vvv=======================SETUP START=======================vvv
	/**
	 * Singleton constructor. Initializes and stores the singleton instances
	 * of hardware listeners. Registers the listeners with the appropriate hardware.
	 * Sets up the event log and display driver.
	 */
	private VendingManager(){
		eventLog = new Logger(eventLogName);

		//setup lock
		lock = new Lock();

		displayListener = new DispListener(this);
		buttonListener = ButtonListener.initialize(this);
		popListener = PopListener.initialize(this);
		lightListener = LightListener.initialize(this);
		coinListener = CoinListener.initialize(this);
		lockListener = MachineLockListener.initialize(this);

		registerListeners();

		displayDriver = new DisplayDriver(getDisplay());
		displayDriver.greetingMessage();

		if (isOutOfOrder()) enableSafety();
	}

	/**
	 * Replaces the existing singleton instances (if any) for the entire
	 * the Vending logic package.
	 * @param host The VendingMachine which the VendingManager is intended to manage.
	 */
	public static VendingManager initialize(VendingMachine host){
		vm = host;
		mgr = new VendingManager();
		return getInstance();
	}

	/**
	 * Provides public access to the VendingManager singleton.
	 * @return The singleton VendingManager instance
	 */
	public static VendingManager getInstance(){
		return mgr;
	}

	/**
	 * Registers the previously instantiated listeners with the appropriate hardware.
	 */
	private void registerListeners(){
		getDisplay().register(displayListener);

		registerPopCanRackListener(popListener);
		getDeliveryChute().register(popListener);

		getCoinSlot().register(coinListener);
		getCoinReceptacle().register(coinListener);
		registerCoinRackListener(coinListener);
		getCoinReturn().register(coinListener);

		getOutOfOrderLight().register(lightListener);
		getExactChangeLight().register(lightListener);

		getLock().register(lockListener);
		
		registerButtonListener(buttonListener);		
	}

	/**
	 * Iterates through all selection buttons in the VendingMachine and
	 * registers a single listener with each.
	 * @param listener The listener that will handle SelectionButtonListener events.
	 */
	private void registerButtonListener(PushButtonListener listener){
		int buttonCount = getNumberOfSelectionButtons();
		for (int i = 0; i< buttonCount; i++){
			getSelectionButton(i).register(listener);;
		}
	}

	/**
	 * Iterates through all coin racks in the VendingMachine and
	 * registers a single listener with each.
	 * @param listener The listener that will handle CoinRackListener events.
	 */
	private void registerCoinRackListener(CoinRackListener listener){
		int rackCount = getNumberOfCoinRacks();
		for (int i = 0; i< rackCount; i++){
			getCoinRack(i).register(listener);;
		}
	}

	/**
	 * Iterates through all pop can racks in the VendingMachine and
	 * registers a single listener with each.
	 * @param listener The listener that will handle PopCanRackListener events.
	 */
	private void registerPopCanRackListener(PopCanRackListener listener){
		int rackCount = getNumberOfPopCanRacks();
		for (int i = 0; i< rackCount; i++){
			getPopCanRack(i).register(listener);;
		}
	}

//^^^=======================SETUP END=======================^^^

	// Accessors used throughout the vending logic classes to get hardware references.
	// Indirect access to the VM is used to simplify the removal of the
	// VM class from the build.
//vvv=======================ACCESSORS START=======================vvv
	boolean isSafetyEnabled(){
		return vm.isSafetyEnabled();
	}
	IndicatorLight getExactChangeLight(){
		return vm.getExactChangeLight();
	}
	IndicatorLight getOutOfOrderLight(){
		return vm.getOutOfOrderLight();
	}
	int getNumberOfSelectionButtons(){
		return vm.getNumberOfSelectionButtons();
	}
	PushButton getSelectionButton(int index){
		return vm.getSelectionButton(index);
	}
	CoinSlot getCoinSlot(){
		return vm.getCoinSlot();
	}
	CoinReceptacle getCoinReceptacle(){
		return vm.getCoinReceptacle();
	}
	CoinReturn getCoinReturn(){
		return vm.getCoinReturn();
	}
	DeliveryChute getDeliveryChute(){
		return vm.getDeliveryChute();
	}
	int getNumberOfCoinRacks(){
		return vm.getNumberOfCoinRacks();
	}
	CoinRack getCoinRack(int index){
		return vm.getCoinRack(index);
	}
	CoinRack getCoinRackForCoinKind(int value){
		return vm.getCoinRackForCoinKind(value);
	}
	Integer getCoinKindForCoinRack(int index){
		return vm.getCoinKindForCoinRack(index);
	}
	int getNumberOfPopCanRacks(){
		return vm.getNumberOfPopCanRacks();
	}
	String getPopKindName(int index){
		return vm.getPopKindName(index);
	}
	int getPopKindCost(int index){
		return vm.getPopKindCost(index);
	}
	PopCanRack getPopCanRack(int index){
		return vm.getPopCanRack(index);
	}
	Display getDisplay(){
		return vm.getDisplay();
	}
	Lock getLock() {
		return lock;
	}

	/**
	 * Returns the index of the given SelectionButton,
	 * which implies the index of the associated PopRack.
	 * @param button The button of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getButtonIndex(PushButton button){
		int buttonCount = getNumberOfSelectionButtons();
		for (int i = 0; i < buttonCount; i++){
			if (getSelectionButton(i) == button){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the given PopCanRack.
	 * @param popRack The PopCanRack of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getPopCanRackIndex(PopCanRack popRack){
		int rackCount = getNumberOfPopCanRacks();
		for (int i = 0; i < rackCount; i++){
			if (getPopCanRack(i) == popRack){
				return i;
			}
		}
		return -1;
	}

	/**
	  * Returns the name pop in the given PopCanRack.
	 * @param popRack The PopCanRack to check the name for.
	 * @return The name of the pop.
	 */
	String getPopCanRackName(PopCanRack popRack){
		return getPopKindName(getPopCanRackIndex(popRack));
	}

	/**
	 * Returns the index of the given CoinRack.
	 * @param coinRack The CoinRack of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getCoinRackIndex(CoinRack coinRack){
		int rackCount = getNumberOfCoinRacks();
		for (int i = 0; i < rackCount; i++){
			if (getCoinRack(i) == coinRack){
				return i;
			}
		}
		return -1;
	}

	/**
	  * Returns the coin value in the given CoinRack.
	 * @param coinRack The CoinRack to check the value for.
	 * @return The value (in cents) of the stored coins.
	 */
	int getCoinRackValue(CoinRack coinRack){
		return getCoinKindForCoinRack(getCoinRackIndex(coinRack));
	}

	/**
	 * Gets the credit available for purchases, in cents.
	 * Public access for testing and external access.
	 * It is assumed to not be a security vulnerability.
	 * @return The stored credit, in cents.
	 */
	public int getCredit(){
		return credit;
	}
//^^^=======================ACCESSORS END=======================^^^


//vvv=======================HARDWARE LOGIC START=======================vvv
	/**
	 * Used by calling code to to enable the safety.
	 * If the safety is not already enabled, it will always relay the message
	 * to the hardware.
	 */
	void enableSafety(){
		if (!isSafetyEnabled())
			log("Safety enabled");
			vm.enableSafety();
	}

	/**
	 * Used by calling code to *attempt* to disable the safety.
	 * The calling code is assumed to be ignorant of system state, so
	 * there are many cases where this will be called but the message will
	 * not be relayed to the hardware.
	 */
	void disableSafety(){
		if (isSafetyEnabled() && !isOutOfOrder() && !lockListener.isLocked())
			log("Safety disabled");
			vm.disableSafety();
	}

	/**
	 * Handles a pop purchase. Checks if the pop rack has pop, confirms funds available,
	 *  dispenses the pop, reduces available funds and deposits the added coins into storage.
	 * @param popIndex The index of the selected pop rack.
	 * @throws InsufficientFundsException Thrown if credit < cost.
	 * @throws EmptyException Thrown if the selected pop rack is empty.
	 * @throws DisabledException Thrown if the pop rack or delivery chute is disabled.
	 * @throws CapacityExceededException Thrown if the delivery chute is full.
	 */
	void buy(int popIndex) throws InsufficientFundsException, EmptyException,
											DisabledException, CapacityExceededException {
		int cost = getPopKindCost(popIndex);

		if (getCredit() >= cost){
			getPopCanRack(popIndex).dispensePopCan(); //Will throw EmptyException if pop rack is empty
			credit -= cost; //Will only be performed if the pop is successfully dispensed.
			
			//These coin-related actions may need to be nested in a conditional once additional
			//Payment methods are supported. It depends on whether change is returned automatically.
			getCoinReceptacle().storeCoins();
			returnChange();

			if (credit > 0) {
				displayCredit();
			} else {
				display("Thank you for your purchase!", 3); 
			}
			
		} else { //Not enough credit
			int diff = cost - credit;
			String popName = getPopKindName(popIndex);
			throw new InsufficientFundsException("Cannot buy " + popName + ". " + diff + " cents missing.");
		}
	}

	/**
	 * A method for returning change.
	 * May not return exact change.
	 * Dispenses the largest denominations first.
	 *
	 * @throws DisabledException Some necessary hardware is disabled
	 * @throws EmptyException CoinSlot is empty and a coin removal was attempted
	 * @throws CapacityExceededException DeliveryChute is full
	 */
	void returnChange() throws CapacityExceededException, EmptyException, DisabledException{
		int[] rackValues = getDescendingRackValues();
		int coinVal = 0;
		CoinRack rack;
		for (int i=0; i < getNumberOfCoinRacks(); i++){
			coinVal = rackValues[i];
			rack = getCoinRackForCoinKind(coinVal);

			while (getCredit() >= coinVal && rack.size() != 0){
				rack.releaseCoin();
				subtractCredit(coinVal);
			}

			if (getCredit() == 0){
				break;
			}
		}
	}

	/**
	 * Convenience method to display a message from other logic classes.
	 * @param msg The message to be displayed.
	 */
	void display(String msg){
		displayDriver.newMessage(msg);
	}

	/**
	 * Convenience method to display a timed message from other logic classes.
	 * @param msg The message to be displayed.
	 * @param duration The duration of the message.
	 */
	void display(String msg, int duration){
		displayDriver.newMessage(msg, duration);
	}

	/**
	 * Displays the current credit on the hardware display
	 */
	void displayCredit() {
		display(getCreditMessage());
	}

//^^^======================HARDWARE LOGIC END=======================^^^

//vvv======================LOGIC INTERNALS START=======================vvv
	/**
	 * Adds value to the tracked credit.
	 * @param added The credit to add, in cents.
	 */
	void addCredit(int added){
		credit += added;
		log("Credit added:" + added);
	}

	/**
	 * Subtracts value to the tracked credit.
	 * @param subtracted The credit to add, in cents.
	 */
	void subtractCredit(int subtracted){
		credit -= subtracted;
		log("Credit removed:" + subtracted);
	}

	/**
	 * Returns a formatted string to display credit.
	 * @return The formatted credit string.
	 */
	public String getCreditMessage(){
		String message;

		//Prettify the message for known currencies.
		if (currency.equals("CAD") || currency.equals("USD")){
			int dollars = credit / 100;
			int cents = credit % 100;
			message = String.format("Credit: $%3d.%02d", dollars, cents);
		}
		else{
			message = "Credit: " + credit;
		}

		return message;
	}


	/**
	 * Takes the coin values inside the machine and sorts them in
	 * descending order for the purpose of change return
	 * @return coins denominations in descending order as an array
	 */
	int[] getDescendingRackValues() {
		int rackNumber = getNumberOfCoinRacks();
		int[] rackAmounts = new int[rackNumber];

		for (int i=0; i < rackNumber; i++){
			rackAmounts[i] = getCoinKindForCoinRack(i);
		}

		Arrays.sort(rackAmounts);
		int[] descending = new int[rackNumber];
		//Reverse the array
		for (int i = rackNumber - 1; i >= 0; i--){
			descending[rackNumber - i - 1] = rackAmounts[i];
		}
		return descending;
	}

	/**
	 * Checks that exact change could be provided for each possible purchase,
	 * given the current credit.
	 * @return True if exact change can be provided for each purchase
	 */
	public boolean checkExactChangeState(){
		boolean exact = true;
		int rackCount = getNumberOfPopCanRacks();

		int popCost;
		for (int i = 0; i < rackCount; i++){
			popCost = getPopKindCost(i);
			exact = canReturnExactChange(popCost);
			if (!exact)
				break;
		}

		return exact;
	}

	/**
	 * Checks if valid change can be returned, but does not return anything.
	 * Similar to returnChange, but sets the indicator light instead
	 * @param cost The cost (in cents) of a hypothetical purchase
	 * @return Whether exact change could be provided for an item of the given cost
	 */
	boolean canReturnExactChange(int cost){
		boolean exact = true;

		int credit = getCredit();
		int excess = credit - cost; // i.e. credit after the possible purchase

		int rackCount = getNumberOfCoinRacks();
		int[] rackValues = getDescendingRackValues();

		//Populate CoinRack count array
		int[] rackAmounts = new int[getNumberOfCoinRacks()];
		for (int i=0; i < rackCount; i++){
			rackAmounts[i] = getCoinRackForCoinKind(rackValues[i]).size();
			if (debug) System.out.println("CoinRack (value: " + rackValues[i]
										+ ") has " + rackAmounts[i] + " coins.");
		}
		//Try to reduce the excess credit to 0
		for (int i=0; i < rackCount; i++){
			while (excess >= rackValues[i] && rackAmounts[i] != 0){
				excess -= rackValues[i];
				rackAmounts[i]--;
			}
			if (excess == 0){
				if (debug) System.out.println("Correct Change");
				break;
			}
		}

		//If credit remains, inexact change would need to be be provided
		if (excess > 0){
			exact = false;
			if (debug) System.out.println("Wrong change");
		}

		return exact;
	}

	/**
	 * Checks if all of the pop racks are empty.
	 * @return True if all are empty, else false
	 */
	boolean checkAllProductsEmpty(){
		boolean empty = true;

		int popCount = getNumberOfPopCanRacks();
		for (int i = 0; i < popCount; i++){
			if (getPopCanRack(i).size() != 0){
				empty = false;
				break;
			}
		}

		return empty;
	}

	/**
	 * Checks the machine state to determine whether it is out of order.
	 * Checks the status of the delivery chute, coin receptacle, and inventory.
	 */
	boolean isOutOfOrder() {
		boolean response = false;
		if (
			!getDeliveryChute().hasSpace() ||
			!getCoinReceptacle().hasSpace() ||
			!getCoinReturn().hasSpace() ||
			checkAllProductsEmpty()
		) {
			response = true;
		}
		return response;
	}

	/**
	 * Provides a simplified interface for the Logger.log() methods.
	 * See details in ca.ucalgary.seng300.a2.Logger.
	 *
	 * @param msg String of event to log. None can be null or empty.
	 */
	void log(String msg){
		try{
			eventLog.log(msg);
		}
		catch(IllegalArgumentException e){
			if (debug) System.out.println(e);
		}
		catch(FileNotFoundException e){
			if (debug) System.out.println(e);
		}
	}

	/**
	 * See log(String).
	 * @param msgs String array of events to log.
	 */
	void log(String[] msgs){
		try{
			eventLog.log(msgs);
		}
		catch(IllegalArgumentException e){
			if (debug) System.out.println(e);
		}
		catch(FileNotFoundException e){
			if (debug) System.out.println(e);
		}
	}

	//TODO Finish implementation once coin return button added.
//	/**
//	 * **Currently unused**. Provides a method of returning the coins that are stored
//	 * in the coin receptacle.
//	 * @throws CapacityExceededException
//	 * @throws DisabledException
//	 */
//	public void returnInsertedCoins() throws CapacityExceededException, DisabledException {
//		getCoinReceptacle().returnCoins();
//		//TODO Decide where it would be best to handle credit adjustment
//		// e.g. in listener method for CoinReturn coinDelivered()?
//
//
//	}
//^^^======================LOGIC INTERNALS END=======================^^^
}
