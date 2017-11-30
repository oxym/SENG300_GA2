package ca.ucalgary.seng300.a2;

import java.util.Arrays;
import java.util.List;

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
 * @author Xiangyu (Michael) Han ()
 * @author Keegan Barnett ()
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
	private static ConfigPanelHandler configHandler;


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

		VendingMachine machine = new VendingMachine(cfg.coinKinds, cfg.selectionButtonCount, cfg.coinRackCapacity, cfg.productRackCapacity,
				cfg.receptacleCapacity, cfg.deliveryChuteCapacity, cfg.coinReturnCapacity);
		machine.configure(cfg.productNames, cfg.productCosts);

		VendingManager.initialize(machine, cfg.coinKinds);
	}

	/**
	 * Singleton constructor. Initializes and stores the singleton instances
	 * of hardware listeners. Registers the listeners with the appropriate hardware.
	 * Sets up the event log and display driver.
	 */
	private VendingManager(){
		eventLog = new Logger(eventLogName);

		credHandler = new CreditHandler(this);
		prodHandler = new ProductHandler(this);
		configHandler = new ConfigPanelHandler(this);

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
		mgr.disableSafety();

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
		getConfigurationPanel().getDisplay().register(displayListener);

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
		int rackCount = getNumberOfProductRacks();
		for (int i = 0; i< rackCount; i++){
			getProductRack(i).register(listener);;
		}
	}

//^^^=======================SETUP END=======================^^^

// Accessors used throughout the vending logic classes to get hardware references.
// Indirect access to the VM is used to simplify the removal of the
// VM class from the build.
//vvv=======================ACCESSORS START=======================vvv
	/**@see org.lsmr.vending.hardware.VendingMachine */
	boolean isSafetyEnabled(){
		return vm.isSafetyEnabled();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	IndicatorLight getExactChangeLight(){
		return vm.getExactChangeLight();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	IndicatorLight getOutOfOrderLight(){
		return vm.getOutOfOrderLight();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	int getNumberOfSelectionButtons(){
		return vm.getNumberOfSelectionButtons();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	PushButton getSelectionButton(int index){
		return vm.getSelectionButton(index);
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	CoinSlot getCoinSlot(){
		return vm.getCoinSlot();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	CoinReceptacle getCoinReceptacle(){
		return vm.getCoinReceptacle();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	CoinReturn getCoinReturn(){
		return vm.getCoinReturn();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	DeliveryChute getDeliveryChute(){
		return vm.getDeliveryChute();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	int getNumberOfCoinRacks(){
		return vm.getNumberOfCoinRacks();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	CoinRack getCoinRack(int index){
		return vm.getCoinRack(index);
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	CoinRack getCoinRackForCoinKind(int value){
		return vm.getCoinRackForCoinKind(value);
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	Integer getCoinKindForCoinRack(int index){
		return vm.getCoinKindForCoinRack(index);
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	int getNumberOfProductRacks(){
		return vm.getNumberOfPopCanRacks();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	String getProductName(int index){
		return vm.getPopKindName(index);
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	int getProductCost(int index){
		return vm.getPopKindCost(index);
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	PopCanRack getProductRack(int index){
		return vm.getPopCanRack(index);
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	Display getDisplay(){
		return vm.getDisplay();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	Lock getLock() {
		return vm.getLock();
	}
	/**@see org.lsmr.vending.hardware.VendingMachine */
	ConfigurationPanel getConfigurationPanel(){
		return vm.getConfigurationPanel();
	}
	boolean configureVendingMachine(List<String> popCanNames, List<Integer> popCanCosts){
		boolean success = false;
		//Must match current product configuration or we reject the request
		if (popCanNames.size() == mgr.getNumberOfProductRacks() &&
			popCanCosts.size() == popCanNames.size()){
			vm.configure(popCanNames, popCanCosts);
			success = true;
		}


		return success;
	}

	/*
	 * Gets the valid coin denominations.
	 * @return The coin values for each coin rack, with order preserved..
	 */
	int[] getCoinRackValues(){
		int typeCount = getNumberOfCoinRacks();
		int[] types = new int[typeCount];
		for (int i = 0; i < typeCount; i++){
			types[i] = getCoinKindForCoinRack(i);
		}
		return types;
	}

	/**
	 * Takes the coin values inside the machine and sorts them in
	 * descending order for the purpose of change return
	 * @return coins denominations in descending order as an array
	 */
	int[] getDescendingCoinRackValues() {
		int rackNumber = mgr.getNumberOfCoinRacks();
		int[] rackAmounts = new int[rackNumber];

		for (int i=0; i < rackNumber; i++){
			rackAmounts[i] = mgr.getCoinKindForCoinRack(i);
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
	 * Returns the index of the given SelectionButton,
	 * which implies the index of the associated PopRack.
	 * @param button The button of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getSelectionButtonIndex(PushButton button){
		int buttonCount = getNumberOfSelectionButtons();
		for (int i = 0; i < buttonCount; i++){
			if (getSelectionButton(i) == button){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the given SelectionButton in the config panel.
	 * @param button The button of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getConfigButtonIndex(PushButton button){
		int buttonCount = MachineConfiguration.CONFIG_PANEL_BUTTONS;
		ConfigurationPanel config = getConfigurationPanel();
		for (int i = 0; i < buttonCount; i++){
			if (config.getButton(i) == button){
				return i;
			}
		}
		if (button == config.getEnterButton()){
			//Then return a "pseudo-index" of configButtonMaxIndex+1
			//Is used internally to facilitate handling of the "enter" key
			return buttonCount;
		}
		return -1;
	}

	/**
	 * Returns the index of the given PopCanRack.
	 * @param popRack The PopCanRack of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getProductRackIndex(PopCanRack productRack){
		int rackCount = getNumberOfProductRacks();
		for (int i = 0; i < rackCount; i++){
			if (getProductRack(i) == productRack){
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
	String getProductRackName(PopCanRack productRack){
		return getProductName(getProductRackIndex(productRack));
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
//^^^=======================ACCESSORS END=======================^^^

//vvv====================FUNCTIONALITY HANDLERS START===================vvv
	//TODO DOCUMENT
	public CreditHandler getCreditHandler(){
		return credHandler;
	}

	//TODO DOCUMENT
	public ProductHandler getProductHandler(){
		return prodHandler;
	}

	public ConfigPanelHandler getConfigPanelHandler(){
		return configHandler;
	}

	//The below accessor methods are preserved in VendingManager
	//are intended to decouple other logic classes (e.g. listeners) from
	//the handler classes.

	/** @see CreditHandler */
	public int getCredit(){
		return mgr.getCreditHandler().getCredit();
	}
	/** @see CreditHandler */
	public String getCurrency(){
		return mgr.getCreditHandler().getCurrency();
	}
	/** @see CreditHandler */
	void addCredit(int added){
		mgr.getCreditHandler().addCredit(added);
	}
	/** @see CreditHandler */
	void subtractCredit(int subtracted){
		mgr.getCreditHandler().subtractCredit(subtracted);
	}
	/** @see CreditHandler */
	void returnChange() throws CapacityExceededException, EmptyException, DisabledException{
		mgr.getCreditHandler().returnChange();
	}
	/** @see CreditHandler */
	public boolean checkExactChangeState(){
		return mgr.getCreditHandler().checkExactChangeState();
	}
	/** @see CreditHandler */
	public String getCreditMessage(){
		return mgr.getCreditHandler().getCreditMessage();
	}

	/** @see ProductHandler */
	void buy(int productIndex) throws InsufficientFundsException, EmptyException,
	DisabledException, CapacityExceededException {
		getProductHandler().buy(productIndex);
	}
	/** @see ProductHandler */
	boolean checkAllProductsEmpty(){
		return getProductHandler().checkAllProductsEmpty();
	}
//^^^====================FUNCTIONALITY HANDLERS END===================^^^

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
	 * Returns the debugging state of the system.
	 * @return Whether the VendingManager is in debug mode.
	 */
	boolean isDebug(){
		return debug;
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
		gui = new GUIMain(vm, mgr, getCoinRackValues());
		gui.init();
	}

	//TODO DOCUMENT
	boolean isGUIEnabled(){
		return GUI_enabled;
	}

	/**
	 * Updates the user display in the GUI
	 * @param message Message to display in GUI
	 */
	void guiUpdateUserDisplay(String message){
		if (isGUIEnabled() && gui != null){
			gui.getSidePanel().getDisplayPanel().updateMessage(message);
		}

	}

	/**
	 * Updates the config panel display in the GUI
	 * @param message Message to display in GUI
	 */
	void guiUpdateConfigDisplay(String message){
		if (isGUIEnabled() && gui != null){
			//TODO Add config panel message update call
//			gui.getConfigPanel().getDisplayPanel().updateMessage(message);
		}
	}

	/**
	 * Updates the GUI "exact change" light state
	 * @param state The on/off state of the light
	 */
	void guiSetChangeLight(boolean state){
		if (isGUIEnabled() && gui != null){
			gui.getSidePanel().getDisplayPanel().indicatorSet(MachineConfiguration.EXACT_CHANGE, state);
		}
	}

	/**
	 * Updates the GUI "out of order" light state
	 * @param state The on/off state of the light
	 */
	void guiSetOutOfOrderLight(boolean state){
		if (isGUIEnabled() && gui != null){
			gui.getSidePanel().getDisplayPanel().indicatorSet(MachineConfiguration.OUT_OF_ORDER, state);
		}
	}

	/**
	 * Notifies the GUI delivery chute that an item has been added
	 */
	void guiAddItemToChute(){
		if (mgr.isGUIEnabled() && gui != null){
			gui.getDeliveryChutePanel().addItem();
		}
	}

	/**
	 * Notifies the GUI delivery chute that an item has been removed
	 */
	void guiRemoveItemFromChute(){
		if (mgr.isGUIEnabled() && gui != null){
			gui.getDeliveryChutePanel().removeItems();
		}
	}
//^^^======================GUI ACCESS END=======================^^^
}
