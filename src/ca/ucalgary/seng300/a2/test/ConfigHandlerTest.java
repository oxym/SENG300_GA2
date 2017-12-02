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

		manager = VendingManager.initialize(machine, cfg.coinKinds);

		machine.disableSafety(); //needed due to singleton instance being passed to multiple tests

		configH = new ConfigPanelHandler(manager);
	}
	
	/*
	 * Tests for getButtonDisplayCode
	 */
	@Test
	public void invalidButton(){	
		String code = configH.getButtonDisplayCode(-1);
		System.out.println(code);
		assertEquals("", configH.getButtonDisplayCode(1));
	}
	
	@Test
	public void invalidButton2(){	
		String code = configH.getButtonDisplayCode(1);
		System.out.println(code);
		assertEquals("B", configH.getButtonDisplayCode(1));
	}
	
}
