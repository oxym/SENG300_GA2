package ca.ucalgary.seng300.a2;

import java.util.Arrays;

import org.lsmr.vending.hardware.*;
import java.io.FileNotFoundException;


/**
 * VendingManager is the primary access-point for the logic controlling the
 * VendingMachine hardware. It is associated with VendingListener, which listens
 * for event notifications from the hardware classes.
 *
 * USAGE: Pass VendingMachine to static method initialize(), then use getInstance()
 * to get the singleton VendingManager object. Listeners are registered automatically.
 *
 * DESIGN: All logic classes are designed as singletons. Currently, the only public-access methods are for initialization
 * and to get a VendingManager instance. All other functionality is restricted
 * to package access.
 *
 * TESTING: Due to the near-total encapsulation, VendingManager and VendingListener
 * must be tested along with a VendingMachine. Although a "stub" VendingMachine
 * *could* be created, doing so would be extremely inefficient.
 * We have been instructed that the VendingMachine and other hardware classes
 * are known-good, so integration testing will be sufficient.
 *
 * @author Thomas Coderre (10169277)
 * @author Jason De Boer (30034428)
 * @author Khesualdo Condori (30004958)
 * @author Michaela Olsakova (30002591)
 * @author
 * @author
 *
 */
public class VendingManager {
	private static boolean debug = true;

	private static VendingManager mgr;
	private static VendingListener listener;
	private static VendingMachine vm;
	private static DispListener displayListener;
	private static DisplayDriver displayDriver;

	private static Logger eventLog;
	private static String eventLogName = "VendingLog.txt";
	private int credit = 0;

	private static String currency = "CAD";



	/**
	 * Singleton constructor. Initializes and stores the singleton instance
	 * of VendingListener.
	 */
	private VendingManager(){
		VendingListener.initialize(this);
		listener = VendingListener.getInstance();
		displayListener = new DispListener();
		eventLog = new Logger(eventLogName);
	}

	/**
	 * Replaces the existing singleton instances (if any) for the entire
	 * the Vending logic package. Registers the VendingListener(s) with the
	 * appropriate hardware.
	 * @param host The VendingMachine which the VendingManager is intended to manage.
	 */
	public static void initialize(VendingMachine host){
		mgr = new VendingManager();
		vm = host;
		mgr.registerListeners();
		displayDriver = new DisplayDriver(mgr.getDisplay());
		displayDriver.greetingMessage();
		mgr.checkOutOfOrder();
	}

	/**
	 * Provides public access to the VendingManager singleton.
	 * @return The singleton VendingManager instance
	 */
	public static VendingManager getInstance(){
		return mgr;
	}

	/**
	 * Registers the previously instantiated listener(s) with the appropriate hardware.
	 */
	private void registerListeners(){
		getDisplay().register(displayListener);

		getCoinSlot().register(listener);
		getDeliveryChute().register(listener);
		getCoinReceptacle().register(listener);
		getOutOfOrderLight().register(listener);
		getExactChangeLight().register(listener);

		//TODO implement Lock
		//getLock().register(listener)

		registerCoinRackListener(listener);
		registerPopCanRackListener(listener);
		registerButtonListener(listener);
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

	/**
	 * Checks the out of order status and sets machine appropriately
	 * checks the status of the delivery chute, coin receptacle, and inventory
	 * puts the machine in an out of order state as required
	 */
	private void checkOutOfOrder() {
		if (
			!getDeliveryChute().hasSpace() ||
			!getCoinReceptacle().hasSpace() ||
			//!getCoinReturn().hasSpace() || //TODO enable once null pointer fixed
			checkAllProductsEmpty()
		) {
			getOutOfOrderLight().activate();
			enableSafety();
		}
	}

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

	/**
	 * Used by calling code to to enable the safety.
	 * If the safety is not already enabled, it will always relay the message
	 * to the hardware.
	 */
	void enableSafety(){
		if (!mgr.isSafetyEnabled())
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
		//TODO Add more conditions; should not disable if something is still wrong
		if (mgr.isSafetyEnabled())
			log("Safety disabled");
			vm.disableSafety();
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
	 * @param poprack The PopCanRack of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getPopCanRackIndex(PopCanRack popRack){
		int rackCount = getNumberOfPopCanRacks();
		for (int i = 0; i < rackCount; i++){
			if (this.getPopCanRack(i) == popRack){
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
		return mgr.getPopKindName(mgr.getPopCanRackIndex(popRack));
	}

	/**
	 * Returns the index of the given CoinRack.
	 * @param coinRack The CoinRack of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getCoinRackIndex(CoinRack coinRack){
		int rackCount = getNumberOfCoinRacks();
		for (int i = 0; i < rackCount; i++){
			if (this.getCoinRack(i) == coinRack){
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
	 * @param added The credit to add, in cents.
	 */
	void subtractCredit(int subtracted){
		credit -= subtracted;
		log("Credit removed:" + subtracted);
	}
//^^^=======================ACCESSORS END=======================^^^


//vvv=======================VENDING LOGIC START=======================vvv
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
			returnChange();

			if (credit > 0) {
				displayCredit();
			} else {
				display("Transaction Complete", 3);
			}
			getCoinReceptacle().storeCoins();
		} else {
			int diff = cost - credit;
			String popName = getPopKindName(popIndex);
			throw new InsufficientFundsException("Cannot buy " + popName + ". " + diff + " cents missing.");
		}
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
	 * Displays the current credit on the hardware display
	 */
	void displayCredit() {
		displayDriver.newMessage(getCreditMessage());
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
	public void returnChange() throws CapacityExceededException, EmptyException, DisabledException{
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
	 * Takes the coin values inside the machine and sorts them in descending order for the purpose of change return
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
	 * Checks if all of the pop racks are empty.
	 * @return True if all are empty, else false
	 */
	boolean checkAllProductsEmpty(){
		boolean empty = true;

		int popCount = getNumberOfPopCanRacks();
		for (int i = 0; i < popCount; i++){
			if (this.getPopCanRack(i).size() != 0){
				empty = false;
				break;
			}
		}

		return empty;
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
			if (debug) System.out.println("CoinRack with value: " + rackValues[i]
										+ " has " + rackAmounts[i] + " coins.");
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


//^^^======================VENDING LOGIC END=======================^^^

//vvv======================LOGIC INTERNALS START=======================vvv

	/**
	 * Provides a simplified interface for the Logger.log() methods.
	 * See details in ca.ucalgary.seng300.a2.Logger.
	 *
	 * @param msgs String array of events to log. None can be null or empty.
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

//^^^======================LOGIC INTERNALS END=======================^^^
}