package ca.ucalgary.seng300.a2;

import java.util.Arrays;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a2.gui.GUIMain;
import ca.ucalgary.seng300.a2.gui.GuiInterfaceIndicators;

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
	private static boolean GUI_enabled = true;

	private static VendingManager mgr;
	private static VendingMachine vm;
	private static DisplayDriver displayDriver;

	private static DispListener displayListener;
	private static PopListener popListener;
	private static ButtonListener buttonListener;
	private static CoinListener coinListener;
	private static LightListener lightListener;
	private static MachineLockListener lockListener;
	private static CreditHandler credHandler;
	private static ProductHandler prodHandler;

	private static Logger eventLog;
	private static String eventLogName = "VendingLog.txt";


	private static GUIMain gui;


//vvv=======================SETUP START=======================vvv
	/**
	 * Main method used primarily to drive manual testing.
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		MachineConfiguration cfg = new MachineConfiguration();

		VendingMachine machine = new VendingMachine(cfg.coinKinds, cfg.selectionButtonCount, cfg.coinRackCapacity, cfg.popCanRackCapacity,
				cfg.receptacleCapacity, cfg.deliveryChuteCapacity, cfg.coinReturnCapacity);
		machine.configure(cfg.popCanNames, cfg.popCanCosts);

		VendingManager.initialize(machine, cfg.coinKinds);
	}
	
	/**
	 * Singleton constructor. Initializes and stores the singleton instances
	 * of hardware listeners. Registers the listeners with the appropriate hardware.
	 * Sets up the event log and display driver.
	 */
	private VendingManager(){
		eventLog = new Logger(eventLogName);

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
	public static VendingManager initialize(VendingMachine host, int[] coinValues){
		vm = host;
		mgr = new VendingManager();

		if(GUI_enabled)
			mgr.startGui();

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
		return vm.getLock();
	}
	ConfigurationPanel getConfigurationPanel(){
		return vm.getConfigurationPanel();
	}
	
	
	/*
	 * Gets the valid coin denominations.
	 * @return The coin values for each coin rack, with order preserved..
	 */
	int[] getValidCoinTypes(){
		int typeCount = getNumberOfCoinRacks();
		int[] types = new int[typeCount];
		for (int i = 0; i < typeCount; i++){
			types[i] = getCoinKindForCoinRack(i);
		}
		return types;
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
	
	//TODO DOCUMENT
	public static CreditHandler getCreditHandler(){
		return credHandler;
	}

	//TODO DOCUMENT	
	public static ProductHandler getProductHandler(){
		return prodHandler;
	}
	
	//TODO DOCUMENT
	boolean isGUIEnabled(){
		return GUI_enabled;
	}
//^^^=======================ACCESSORS END=======================^^^


//vvv=======================HARDWARE LOGIC START=======================vvv
	/**
	 * Used by calling code to to enable the safety.
	 * If the safety is not already enabled, it will always relay the message
	 * to the hardware.
	 */
	public void enableSafety(){
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
	public void disableSafety(){
		if (isSafetyEnabled() && !isOutOfOrder() && !getLock().isLocked())
			log("Safety disabled");
			vm.disableSafety();
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

			while (getCreditHandler().getCredit() >= coinVal && rack.size() != 0){
				rack.releaseCoin();
				getCreditHandler().subtractCredit(coinVal);
			}

			if (getCreditHandler().getCredit() == 0){
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
		display(getCreditHandler().getCreditMessage());
	}

//^^^======================HARDWARE LOGIC END=======================^^^

//vvv======================LOGIC INTERNALS START=======================vvv
	

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

		int credit = getCreditHandler().getCredit();
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
			checkAllProductsEmpty() ||
			getLock().isLocked()
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
	
//^^^======================LOGIC INTERNALS END=======================^^^

//vvv======================GUI ACCESS START=======================vvv
	/**
	 * Loads and initializes the GUI for the vending machine simulation.
	 */
	private void startGui() {
		gui = new GUIMain(vm, mgr, getValidCoinTypes());
		gui.init();
	}
	
	/**
	 * Updates the user display in the GUI
	 * @param message Message to display in GUI
	 */
	void guiUpdateUserDisplay(String message){
		if (mgr.isGUIEnabled()){
			gui.getSidePanel().getDisplayPanel().updateMessage(message);
		}

	}
	
	/**
	 * Updates the config panel display in the GUI
	 * @param message Message to display in GUI
	 */
	void guiUpdateConfigDisplay(String message){
		if (mgr.isGUIEnabled()){
			//TODO Add config panel message update call
//			gui.getConfigPanel().getDisplayPanel().updateMessage(message);
		}
	}
	
	/**
	 * Updates the GUI "exact change" light state
	 * @param state The on/off state of the light
	 */
	void guiSetChangeLight(boolean state){
		if (mgr.isGUIEnabled()){
			//TODO modify gui exact change light state
//			gui.getSidePanel().getDisplayPanel().indicatorOff(MachineConfiguration.EXACT_CHANGE);
		}
	}
	
	/**
	 * Updates the GUI "out of order" light state
	 * @param state The on/off state of the light 
	 */
	void guiSetOutOfOrderLight(boolean state){
		if (mgr.isGUIEnabled()){
			//TODO modify gui exact change light state
//			gui.getSidePanel().getDisplayPanel().indicatorOff(MachineConfiguration.OUT_OF_ORDER);
		}
	}
	
	/**
	 * Notifies the GUI delivery chute that an item has been added
	 */
	void guiAddItemToChute(){
		if (mgr.isGUIEnabled()){
			gui.getDeliveryChutePanel().addItem();
		}
	}
	
	/**
	 * Notifies the GUI delivery chute that an item has been removed
	 */
	void guiRemoveItemFromChute(){
		if (mgr.isGUIEnabled()){
			gui.getDeliveryChutePanel().removeItems();			
		}
	}
//^^^======================GUI ACCESS END=======================^^^
}
