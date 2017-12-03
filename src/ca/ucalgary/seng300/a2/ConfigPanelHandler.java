package ca.ucalgary.seng300.a2;

import java.util.ArrayList;

import org.lsmr.vending.hardware.ConfigurationPanel;

public class ConfigPanelHandler {
	private VendingManager mgr;
	private ConfigurationPanel config;
	private DisplayDriver dispDriver;
	
	private static final String[] keyCodes =new String[] {	// Indices
			"A","B","C","D","E","F","G","H","I","J", 	// 0 : 9
			"K","L","M","N","O","P","Q","R","S","T", 	//10 : 19
			"U","V","W","X","Y","Z", 				 	//20 : 25
			"0","1","2","3","4","5","6","7","8","9",	//26 : 35
			"Shift","Enter"};							//36 : 37
	
	private boolean shifted = false;
	private static final int[] BOUND_LETTER = new int[] {0,25}; //Inclusive
	private static final int[] BOUND_NUM = new int[] {26,35}; //Inclusive
	private static final int[] BOUND_SPECIAL = new int[] {36,37}; //Inclusive
	private static final int INDEX_SHIFT = 36;
	private static final int INDEX_ENTER = 37;
	
	private static final int KEY_COUNT = keyCodes.length; 
	
	private static final String
		MENU_MAIN= "Press 1 to reprice a product",
		MENU_REPRICE_SELECT = "Enter the index of the product to reprice",
		MENU_REPRICE_SETPRICE = "Enter a new product price",
		MENU_REPRICE_COMPLETE = "Product repriced.";
	
	
	private String dispMessage = ""; 
	
	private String state = "idle";
	private int product = -1; 
	private String buffer = ""; 
	

	//TODO DOCUMENT
	public ConfigPanelHandler(VendingManager manager){
		mgr = manager;
		config = mgr.getConfigurationPanel();
		dispDriver = new DisplayDriver(config.getDisplay());
		showMainMenu("");
	}
	
	public String[] getKeyCodes(){
		return keyCodes;
	}
	
	/**
	 * Returns the display code for the given button index.
	 * Returns an empty string if the given index is out of range.
	 * @param index The index of the configuration panel button
	 * @return Display code, e.g. "A", "Shift". Can be empty but not null.
	 */
	public String getButtonDisplayCode(int index){
		String code = ""; 
		if (index >= 0 && index < KEY_COUNT){
			code = keyCodes[index];
		};
		return code;
	}
	
	public void pressKey(int key){
		if (isLetter(key) || isNum(key)){
			String symbol = getButtonDisplayCode(key);
			if (!isShifted()){
				symbol = symbol.toLowerCase(); 
			}
			
			buffer += getButtonDisplayCode(key);
			display(buffer);
		}
		else if (isShift(key)){
			toggleShift();
		}
		else if (isEnter(key)){
			pressEnter();
		}
		else{
			//TODO Decide if bad key presses need to be handled?
		}
		
	}
	
	/**
	 * Gets the index of the button whose value - or name - 
	 * matches the given string. Matching is case insensitive.
	 * @param keyName The name of the button to find
	 * @return The index of the button, or -1 if not found
	 */
	public int getKeyIndex(String keyName){
		int kIndex = -1;
		if (keyName != null)
			for (int i = 0; i < KEY_COUNT; i++){
				if (keyCodes[i].equalsIgnoreCase(keyName)){
					kIndex = i;
					break;
				}
			}
		return kIndex;
	}
	
	/**
	 * Retrieves the text that was last shown on the configuration panel display.
	 * @return The last displayed message
	 */
	public String getDisplayMessage(){
		return dispMessage;
	}
	
	//TODO DOCUMENT
	public boolean isLetter(int key){
		return key >= BOUND_LETTER[0] && key <= BOUND_LETTER[1];
	}
	
	//TODO DOCUMENT
	public boolean isNum(int key){
		return key >= BOUND_NUM[0] && key <= BOUND_NUM[1];
	}
	
	//TODO DOCUMENT
	public boolean isSpecial(int key){
		return key >= BOUND_SPECIAL[0] && key <= BOUND_SPECIAL[1];
	}
	
	//TODO DOCUMENT
	public boolean isShift(int key){
		return key == INDEX_SHIFT;
	}
	
	//TODO DOCUMENT
	public boolean isEnter(int key){
		return key == INDEX_ENTER;
	}
	
	//TODO DOCUMENT
	public void toggleShift(){
		shifted = !shifted;
	};
	
	public boolean isShifted(){
		return shifted;
	}

	//TODO DOCUMENT
	public void display(String message){
		dispMessage = message;
		dispDriver.newMessage(message);
	}
	
	//TODO DOCUMENT
	private void showMainMenu(String extraMsg){
		state = "idle";
		
		extraMsg = (extraMsg != null && !extraMsg.equals("")) 
				? extraMsg  + " "
				: "";
		display(extraMsg + MENU_MAIN);
		
		clearTextBuffer();
		clearProductBuffer();
	}

	//TODO DOCUMENT
	private void showProductSelectMenu(String extraMsg){
		state = "product_select";
		
		extraMsg = (extraMsg != null && !extraMsg.equals("")) 
				? extraMsg + " "
				: "";
		display(extraMsg + MENU_REPRICE_SELECT);
		
		clearTextBuffer();
	}

	//TODO DOCUMENT
	private void showProductPriceMenu(String extraMsg){
		state = "price_enter";
		
		extraMsg = (extraMsg != null && !extraMsg.equals("")) 
				? extraMsg  + " "
				: "";
		display(extraMsg + MENU_REPRICE_SETPRICE);
	
		clearTextBuffer();
	}
	
	//TODO DOCUMENT
	private void pressEnter(){
		int productCount = mgr.getNumberOfProductRacks();
		switch(state){
		case "idle":
			if (buffer.equals("1")){
				showProductSelectMenu("");
			} else{
				showMainMenu("Error.");
			}
			break;
		
		case "product_select":
			int tempProduct = -1;
			try{ //Check the input is a valid number
				tempProduct = (int) Integer.parseInt(buffer);
			} catch (NumberFormatException e){}
			
			if (tempProduct < 0 || tempProduct >= productCount){
				showProductSelectMenu("Invalid product selection.");
			} else {
				product = tempProduct;
				showProductPriceMenu("");
			}
			break;
		
		case "price_enter":
			int price = -1;
			try{ //Check the input is a valid number
				price = (int) Integer.parseInt(buffer);
			} catch (NumberFormatException e){}
			
			if (price < 0){ //If it's not a valid number
				showProductPriceMenu("Invalid price.");
			} else { 
				boolean success = updateProductCost(product, price);
				if (success) {
					showMainMenu(MENU_REPRICE_COMPLETE);
				} else { // This should ever happen
					showProductPriceMenu("Unknown pricing error.");
				}
			}		
			break;
		}
	}
	
	/**
	 * Clears the buffer that tracks which product has been selected
	 */
	private void clearProductBuffer(){
		product = -1;
	}
	
	/**
	 * Clears the buffer that tracks entered text
	 */
	private void clearTextBuffer(){
		buffer = "";
	}
	
	/**
	 * Updates the VendingMachine configuration to adjust a product cost.  
	 * @param index The product index to change
	 * @param cost The new, non-zero cost of the product
	 * @return True if the configuration is successful; else false
	 */
	private boolean updateProductCost(int index, int cost){
		ArrayList<String> productNames = new ArrayList<String>();
		ArrayList<Integer> productCosts = new ArrayList<Integer>();

		int prodCount = mgr.getNumberOfProductRacks();
		String prodName;
		int prodCost;
		for (int i = 0; i < prodCount; i++){
			prodName = mgr.getProductName(i);
			prodCost = (i == index && cost != -1) 
					? cost : mgr.getProductCost(i);
			
			productNames.add(prodName);
			productCosts.add(prodCost);
		}
		
		return mgr.configureVendingMachine(productNames, productCosts);
	}
}
