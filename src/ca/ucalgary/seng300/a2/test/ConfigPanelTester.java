package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a2.MachineConfiguration;
import ca.ucalgary.seng300.a2.VendingManager;

public class ConfigPanelTester {
	private VendingMachine vmachine;
	private VendingManager vmanager;
	private MachineConfiguration cfg;
	
	@Before
	public void setup() throws Exception{
		cfg = new MachineConfiguration();
		vmachine = new VendingMachine(cfg.coinKinds, cfg.selectionButtonCount, cfg.coinRackCapacity, cfg.productRackCapacity,
				cfg.receptacleCapacity, cfg.deliveryChuteCapacity, cfg.coinReturnCapacity);
		vmanager = VendingManager.initialize(vmachine, cfg.coinKinds);
	}

	@Test
	public void reloadTest() throws InterruptedException {
		TimeUnit.SECONDS.sleep(20);
		fail("Not yet implemented");
	}

}
