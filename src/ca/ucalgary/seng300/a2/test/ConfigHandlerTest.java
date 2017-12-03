package ca.ucalgary.seng300.a2.test;
import static org.junit.Assert.*;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a2.ConfigPanelHandler;
import ca.ucalgary.seng300.a2.DispListener;
import ca.ucalgary.seng300.a2.MachineConfiguration;
import ca.ucalgary.seng300.a2.VendingManager;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ConfigHandlerTest {
	private VendingMachine machine = null;
	private VendingManager manager = null;
	private MachineConfiguration cfg;
	private ConfigPanelHandler configH;

	@Before
	public void setup() throws Exception {

		cfg = new MachineConfiguration();

		machine = new VendingMachine(cfg.coinKinds, cfg.selectionButtonCount, cfg.coinRackCapacity, cfg.productRackCapacity,
				cfg.receptacleCapacity, cfg.deliveryChuteCapacity, cfg.coinReturnCapacity);
		machine.configure(cfg.productNames, cfg.productCosts);

		manager = VendingManager.initialize(machine);

		machine.disableSafety(); //needed due to singleton instance being passed to multiple tests

		configH = new ConfigPanelHandler(manager);
	}
	
	/**
	 * Tests for getButtonDisplayCode
	 */
	@Test
	public void invalidIndex_OOB(){	
		String code = configH.getButtonDisplayCode(38);
		assertEquals("", code);
	}
	/**
	 * Tests for getButtonDisplayCode
	 */
	@Test
	public void invalidIndex_Negative(){	
		String code = configH.getButtonDisplayCode(-1);
		assertEquals("", code);
	}
	
	@Test
	public void lowerEdgeCase(){	
		String code = configH.getButtonDisplayCode(0);
		assertEquals("A", code);
	}
	
	@Test
	public void upperEdgeCase(){	
		String code = configH.getButtonDisplayCode(37);
		assertEquals("Enter", code);
	}
	
	/**
	 * Character type tests.
	 * All of the ones below are passing but theyre not showing up as code coverage in the ConfigPanelHandler
	 * EDIT [Thomas]: Running them on my machine shows full coverage of isNum() and 3/4 branch coverage of isLetter()
	 */
	@Test
	public void isLetterTrueLower(){
		assertEquals(true ,configH.isLetter(0));
	}
	
	@Test
	public void isLetterTrueUpper(){
		assertEquals(true ,configH.isLetter(25));
	}
	
	@Test
	public void isLetterFalse(){
		assertEquals(false ,configH.isLetter(26));
	}

	@Test
	public void isNumLower(){
		assertEquals(true ,configH.isNum(26));
	}
	
	@Test
	public void isNumUpper(){
		assertEquals(true ,configH.isNum(35));
	}
	
	@Test
	public void isNumFalseUpper(){
		assertEquals(false ,configH.isNum(36));
	}
	
	@Test
	public void isNumFalseLower(){
		assertEquals(false ,configH.isNum(25));
	}
}
