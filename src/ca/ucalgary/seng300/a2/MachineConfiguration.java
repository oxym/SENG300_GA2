package ca.ucalgary.seng300.a2;

import java.util.Arrays;
import java.util.List;

/**
 * This class holds the standard vending machine parameters
 *
 */
public class MachineConfiguration {

	//indicator indices
	public static final int OUT_OF_ORDER = 0;
	public static final int EXACT_CHANGE = 1;
	public static final String[] indicatorLabelText = { "Out Of Order", "Exact Change" };

	public static final int CONFIG_PANEL_BUTTONS = 37;

	public final int[] coinKinds = new int[] { 5, 10, 25, 100, 200 };

	public final List<String> productNames = Arrays.asList("Coke", "Sprite", "Crush", "Ale", "Pepsi", "Diet");
	public final List<Integer> productCosts = Arrays.asList(250, 250, 250, 250, 250, 250);

	public final int selectionButtonCount = productNames.size();
	public final int coinRackCapacity = 15;
	public final int productRackCapacity = 10;
	public final int receptacleCapacity = 200;
	public final int deliveryChuteCapacity = 200;
	public final int coinReturnCapacity = 200;
}
