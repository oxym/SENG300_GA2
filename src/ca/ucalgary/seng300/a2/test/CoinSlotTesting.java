package ca.ucalgary.seng300.a2.test;
import static org.junit.Assert.*;
import org.junit.*;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

/**
 **This JUnit 4 class consists of a unit test suite for the CoinSlot class 
 * in the org.lsmr.vending.hardware package, as well as stub inner classes.
 * 
 **Two inner "stub" classes, TestSink and TestCoinSlotListener, are defined.  
 * 
 **CoinChannel is considered a part of the tested "unit", since 
 * CoinSlot.connect() strictly depends on CoinChannel, which is declared Final.
 * >> Similarly, no stub is created for Coin because CoinSlot.addCoin() strictly
 * >> depends on Coin. It could be extended, but that doesn't seem helpful.
 * 
 **Testing Paradigm: 
 * >> The tests are intended to isolate the CoinSlot as much as possible,
 * >> so VendingMachine is not involved. This approach can be thought of like 
 * >> placing the CoinSlot on a workbench and probing it with test devices.  
 * 
 **Assumptions:
 * >* CoinSlot's parent, AbstractHardware, has already been tested so listener 
 * >> registration and device enable/disabling doesn't need to be tested. 
 * >* CoinSlot's constructor "works" and does not need to be tested; just a setter.
 * >* CoinChannel connection can be tested implicitly in other tests 
 * >* Coins "work", including rejection of non-positive values.
 * 
 **Class Organization:
 * >> Fields
 * >> Inner classes
 * >> Setup/tear-down
 * >> Convenience methods
 * >> Test cases
 * 
 * @author Thomas Coderre (ID: 10169277)
 */
public class CoinSlotTesting {
	CoinSlot testSlot;
	TestCoinSlotListener testListener;
	TestSink validSink;
	TestSink returnSink;
	CoinChannel validChannel;
	CoinChannel returnChannel;


//vvv=======================INNER CLASSES START=======================vvv
	/*
	 * Stub class to connect to CoinChannel. 
	 * Provides two state controls and a means of retrieving the last coin.
	 * 
	 * Control 1 (Disable): Use setDisabled(...). acceptCoin(...) will throw DisabledException.
	 * Control 2 (Set Full): Use setFull(...). acceptCoin(...) will throw CapacityExceededException.
	 * Control 3 (Spoof Empty): Use setSpoofFull(...). Will cause the Sink to pretend that it's empty. 
	 * Control 4 (Last Coin): Use ejectCoin(). Will remove the coin from the skin and return it.
	 * 
	 * Notes: A coin is stored in the sink even if an exception is thrown 
	 */
	private class TestSink implements AbstractCoinAcceptor{
	    private Coin receivedCoin = null;
	    private boolean full = false;
	    private boolean disabled = false;
	    private boolean spoofEmpty = false;

		//Note that the lastCoin is removed.
	    public Coin ejectCoin(){
	    	Coin coinHolder = receivedCoin;
	    	receivedCoin = null;
	    	return coinHolder;
	    }
	    
	    public void setDisabled(boolean isDisabled){disabled = isDisabled;}
	    
	    public void setFull(boolean isFull){full = isFull;}
	    
	    public void setSpoofEmpty(boolean isSpoofed){spoofEmpty = isSpoofed;}

		@Override 
		public void acceptCoin(Coin coin) throws CapacityExceededException, DisabledException {
			receivedCoin = coin;
	    	if (disabled){
	    		throw new DisabledException();
	    	}
	    	else if (full){
	    		throw new CapacityExceededException();
	    	}
		}

		@Override
		public boolean hasSpace() {
			if (spoofEmpty)
				return true;
			else
				return !full;
		}
	}
	
	/*
	 * Stub class to register with CoinSlot.
	 * Used to verify valid / rejection, enabled / disabled notifications.
	 * 
	 * Use 1 (Verify last coin): Use ejectCoin(). Coin is removed from listener and returned.
	 * Use 2 (Check if last accepted): Use wasCoinAccepted(). 
	 * Use 3 (Verify coin notification sent): Use ejectCoin(). Will be null if none  
	 * Use 4 (Verify enable notification sent): Use isSlotDisabled(). Should check start before expected change.
	 * 
	 * Notes: This listener remembers stores only the last coin and acceptance state.
	 */
	private class TestCoinSlotListener implements CoinSlotListener{
		private Coin lastCoin = null;
		private boolean lastCoinAccepted = false;
		private boolean disabled = false;
		
		//Note that the lastCoin is removed.
		public Coin ejectCoin(){
			Coin coinHolder = lastCoin;
			lastCoin = null;
			return coinHolder;
		}
		
		public boolean wasCoinAccepted(){
			return lastCoinAccepted;
		}
		
		public boolean isSlotDisabled(){
			return disabled;
		}
		
		@Override
		public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {			
			disabled = false;
		}

		@Override
		public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
			disabled = true;	
		}

		@Override
		public void validCoinInserted(CoinSlot slot, Coin coin) {
			lastCoin = coin;
			lastCoinAccepted = true; 
		}

		@Override
		public void coinRejected(CoinSlot slot, Coin coin) {
			lastCoin = coin;
			lastCoinAccepted = false; 
		}
	}
//^^^=======================INNER CLASSES END=======================^^^

	
//vvv=========================SETUP & TEAR-DOWN STARTS=======================vvv
	/*
	 * Run before each test
	 * Each test will use a standard set of valid coin values (probably).
	 * If a test case requires special values, just re-instantiate and overwrite 
	 * default CoinSlot within the test case. 
	 */
	@Before public void setupTest(){
		int[] validValues = {5,10,25,100,200};
		setupWorkBench(validValues); //Functionalized to avoid code redundancy.
	}
	
	/*
	 * Run after each test.
	 * Resets all testing devices.
	 */
	@After public void teardownTest(){
		testSlot = null;
		testListener = null;
		validSink = null;
		returnSink = null;
		validChannel = null;
		returnChannel = null;
	}
//^^^=========================SETUP & TEAR-DOWN ENDS=======================^^^

	
//vvv=========================CONVENIENCE METHODS START=======================vvv
	/**
	 * Sets up the testing "workbench", i.e. each test object.
	 * Called before each test by setupTest() and whenever a test needs a CoinSlot
	 * to accept non-standard coin values.
	 * @param validCoins Integer array containing the CoinSlot's valid coin values. 
	 */
	private void setupWorkBench(int[] validCoins){
		testSlot = new CoinSlot(validCoins);
		
		validSink = new TestSink();
		validChannel = new CoinChannel(validSink);
		returnSink = new TestSink();
		returnChannel = new CoinChannel(returnSink);
		testSlot.connect(validChannel, returnChannel);
		
		testListener = new TestCoinSlotListener();
		testSlot.register(testListener);
	}

	/*
	 * Returns an array of Coins.
	 */
	private Coin[] makeCoinArray(int[] values){
		int coinCount = values.length;
		Coin[] testCoins = new Coin[coinCount];
		for (int i = 0; i < coinCount; i++){
			testCoins[i] = new Coin(values[i]);
		}
		return testCoins;
	}
//^^^=========================CONVENIENCE METHODS END=======================^^^
	
	
//vvv=========================TEST CASES START=======================vvv	
	/*
	 * Tests coins with the "default" valid values, under "normal" conditions.
	 * Also acts as a sanity check for the testListener and testSinks.
	 * 
	 * Notes: 	ejectCoin() removes the last coin from the sink / listener.
	 * 			Would be better if it was a parameterized tests but I couldn't get them working.    
	 */
	@Test public void testValidCoins(){
		int[] testValues = {5,10,25,100,200};  
		Coin[] testCoins = makeCoinArray(testValues);

		for (Coin testCoin : testCoins){
			try{
				testSlot.addCoin(testCoin);
				assertEquals(testListener.ejectCoin(), testCoin);
				assertTrue(testListener.wasCoinAccepted());
				assertEquals(validSink.ejectCoin(), testCoin);
				assertNotEquals(returnSink.ejectCoin(),testCoin);
			}
			catch(Exception e){
				fail(e.toString());
			}
		}
	}
	
	/*
	 * Tests invalid coin values under "normal" conditions.
	 * We expect the coin to be added to rejected and 
	 * added to the return sink. 
	 * Note: Non-positive coin values are excluded because
	 * such values are rejected by the Coin constructor. 
	 */
	@Test public void testInvalidCoins(){
		int[] testValues = {1, 1000}; 
		Coin[] testCoins = makeCoinArray(testValues);
		
		for (Coin testCoin : testCoins){
			try{
				testSlot.addCoin(testCoin);
				assertEquals(testListener.ejectCoin(), testCoin);
				assertFalse(testListener.wasCoinAccepted());
				assertNotEquals(validSink.ejectCoin(), testCoin);
				assertEquals(returnSink.ejectCoin(), testCoin);
			}
			catch(Exception e){
				fail(e.toString());
			}
		}
	}

	/*
	 * If a "null" Coin is added, we expect a NullPointerException
	 * We're not sure where the "coin" should end up - i.e. listener / sinks.
	 */
	@Test public void testNullCoin(){
		try{
			testSlot.addCoin(null);
			fail("Should have thrown exception");
		}
		catch(NullPointerException e){
			//Assertions would go here if we knew what to expect
		}
		catch(Exception e){
			fail("Wrong exception thrown: " + e.toString());
		}
	}
	
	/*
	 * Add a coin to a disabled slot.
	 * We expect a DisabledException to be thrown by the CoinSlot
	 * and for the Coin to not be delivered to the sinks or listener.
	 * The listener should be notified for the disabling 
	 */
	@Test public void testDisabledSlot(){
		testSlot.disable();
		Coin testCoin = new Coin(5); //Valid test coin
		try{
			testSlot.addCoin(testCoin);
			fail("Coin accepted when slot disabled"); //Only run if no exception is thrown
		}
		catch(DisabledException e){
			assertTrue(testListener.isSlotDisabled());
			assertNull(testListener.ejectCoin());
			assertNull(validSink.ejectCoin());
			assertNull(returnSink.ejectCoin());

		}
		catch(Exception e){
			fail("Wrong exception thrown when adding valid coin to disabled slot: "
					+ e.toString());
		}		
	}
	
	/*
	 * A set of test cases to probe the behaviour when a sink
	 * is disabled but *not* the coinSlot. 
	 * We expect the CoinSlot to rethrow the DisabledException, but the coin should be 
	 * received (i.e. "stuck") at the sink.
	 * 
	 * Note: The case of both sinks being disabled is not implemented because
	 * its behaviour implements too much with existing tests (e.g. for full sinks).
	 */
	@Test public void testValidSinkDisabled(){
		validSink.setDisabled(true);
		Coin testCoin = new Coin(5); //Valid coin
		try{
			testSlot.addCoin(testCoin); 
			fail("Should have thrown exception");
		}
		catch(DisabledException e){
			assertEquals(testListener.ejectCoin(), testCoin);
			assertTrue(testListener.wasCoinAccepted());
			assertEquals(validSink.ejectCoin(), testCoin);
			assertNull(returnSink.ejectCoin());
		}
		catch(Exception e){
			fail("Wrong exception thrown: " + e.toString());
		}
	}
	@Test public void testReturnSinkDisabled(){
		returnSink.setDisabled(true);
		Coin testCoin = new Coin(1); //Invalid coin
		try{
			testSlot.addCoin(testCoin);
			fail("Should have thrown exception");
		}
		catch(DisabledException e){
			assertEquals(testListener.ejectCoin(), testCoin);
			assertFalse(testListener.wasCoinAccepted());
			assertNull(validSink.ejectCoin());
			assertEquals(returnSink.ejectCoin(),testCoin);
		}
		catch(Exception e){
			fail("Wrong exception thrown: " + e.toString());
		}
	}
	
	/*
	 * Tests the case where the validSink says it's not full, but is.
	 * Should trigger the SimulationException that "Should never happen".
	 * This test is of debatable usefulness; needed for 100% line coverage.
	 */
	@Test public void testValidSinkSpoofedFull(){
		validSink.setSpoofEmpty(true);
		validSink.setFull(true);
		
		Coin testCoin = new Coin(5); //Valid coin
		try{
			testSlot.addCoin(testCoin);
			fail("Should have thrown exception");
		}
		catch(SimulationException e){}
		catch(Exception e){
			fail("Wrong exception thrown: " + e.toString());
		}
	}
	
	/*
	 * Tests the case where the returnSink says it's not full, but is.
	 * Should trigger the SimulationException that "Should never happen"
	 * This test is of debatable usefulness; needed for 100% line coverage.
	 */
	@Test public void testReturnSinkSpoofedFull(){
		returnSink.setSpoofEmpty(true);
		returnSink.setFull(true);
		
		
		Coin testCoin = new Coin(1); //Invalid coin
		try{
			testSlot.addCoin(testCoin);
			fail("Should have thrown exception");
		}
		catch(SimulationException e){}
		catch(Exception e){
			fail("Wrong exception thrown: " + e.toString());
		}
	}
	
	/*
	 * If a valid coin is given and the valid sink is full, we expect
	 * the coin to simply be returned. 
	 */
	@Test public void testValidSinkFull(){
		validSink.setFull(true);
		Coin testCoin = new Coin(5); //Valid coin
		try{
			testSlot.addCoin(testCoin);
			assertEquals(testListener.ejectCoin(), testCoin);
			assertFalse(testListener.wasCoinAccepted());
			assertNull(validSink.ejectCoin());
			assertEquals(returnSink.ejectCoin(),testCoin);
		}
		catch(Exception e){
			fail("Exception thrown when adding valid coin and valid sink is full: "
					+ e.toString());
		}
	}
	/*
	 * If an invalid coin is given but the returnSink is full, we expect a SimulationError
	 * and for the coin to be stuck in the slot, not delivered to a sink. No listener notification. 
	 */
	@Test public void testReturnSinkFull(){
		returnSink.setFull(true);
		Coin testCoin = new Coin(1); //Invalid coin
		try{
			testSlot.addCoin(testCoin);
			fail("Invalid coin taken when returnSink full"); //Only run if no exception is thrown
		}
		catch (SimulationException e){
			assertNull(testListener.ejectCoin());
			assertNull(validSink.ejectCoin());
			assertNull(returnSink.ejectCoin());
		}
		catch(Exception e){
			fail("Wrong exception thrown when adding invalid coin and invalid sink is full: "
					+ e.toString());
		}
	}
	/*
	 * If both sinks are full, we expect a SimulationException.
	 * We also expect the coin to not be passed to the CoinChannels or listeners.
	 */
	@Test public void testBothSinksFull(){
		validSink.setFull(true);
		returnSink.setFull(true);
		
		Coin testCoin = new Coin(5);
		try{
			testSlot.addCoin(testCoin);
			fail("Coin accepted when both sinks full"); //Only run if no exception is thrown
		}
		catch (SimulationException e){
			assertNull(testListener.ejectCoin());
			assertNull(validSink.ejectCoin());
			assertNull(returnSink.ejectCoin());
		}
		catch(Exception e){
			fail("Wrong exception thrown when adding valid coin when both sinks are full: "
					+ e.toString());
		}		
	}
//^^^=========================TEST CASES END=======================^^^
}