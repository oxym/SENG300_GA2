package ca.ucalgary.seng300.a2;

import java.util.Arrays;
import java.util.List;

/**
 * This class holds the standard vending machine parameters
 *
 */
public class MachineConfiguration {
	public final int[] coinKinds = new int[] { 5, 10, 25, 100, 200 };

	public final List<String> popCanNames = Arrays.asList("Coke", "Sprite", "Crush", "Ale", "Pepsi", "Diet");
	public final List<Integer> popCanCosts = Arrays.asList(250, 250, 250, 250, 250, 250);

	public final int selectionButtonCount = popCanNames.size();
	public final int coinRackCapacity = 15;
	public final int popCanRackCapacity = 10;
	public final int receptacleCapacity = 200;
	public final int deliveryChuteCapacity = 200;
	public final int coinReturnCapacity = 200;
}
