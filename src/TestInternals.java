import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.WiredRobot;
import edu.uwm.cs351.WiredRobot.FunctionalPart;
import edu.uwm.cs351.WiredRobot.Spy;
import edu.uwm.cs351.util.Cell;

public class TestInternals extends LockedTestCase {
	public static Comparator<FunctionalPart> nonDiscrimination = (p1,p2) -> 0;
	public static Comparator<FunctionalPart> byFunctionThenId = (p1,p2) -> {
		int c = p1.getFunction().compareTo(p2.getFunction());
		if (c != 0) return c;
		return p1.getId() - p2.getId();
	};
	
	protected WiredRobot.Spy spy;
	protected int reports;
	protected WiredRobot r;
    
    protected void assertReporting(boolean expected, Supplier<Boolean> test) {
            reports = 0;
            Consumer<String> savedReporter = spy.getReporter();
            try {
                    spy.setReporter((String message) -> {
                            ++reports;
                            if (message == null || message.trim().isEmpty()) {
                                    assertFalse("Uninformative report is not acceptable", true);
                            }
                            if (expected) {
                                    assertFalse("Reported error incorrectly: " + message, true);
                            }
                    });
                    assertEquals(expected, test.get().booleanValue());
                    if (!expected) {
                            assertEquals("Expected exactly one invariant error to be reported", 1, reports);
                    }
                    spy.setReporter(null);
            } finally {
                    spy.setReporter(savedReporter);
            }
    }
    
    protected void assertWellFormed(boolean expected, WiredRobot r) {
		assertReporting(expected, () -> spy.wellFormed(r));
	}
    
    @Override
    protected void setUp() {
		spy = new WiredRobot.Spy();
		testQuestions(false);
	}
    
    
    /// Locked tests
    
    @SuppressWarnings("unused")
	private void testQuestions(boolean unused) {
    	// Assume we have a tree of the form:
    	//        C
    	//     A      F
    	//       B   E  G
    	// Answer the following questions about calls to checkInRange:
    	// (where each call is written (r,lo,hi))
    	String answer1 = "(C,null,null)"; // How does wellFormed call checkInRange?
    	String answer2 = Ts(984819539); // What is the first recursive call for "(C,null,null)"?
    	String answer3 = Ts(1234571347); // What is the second recursive call for "(C,null,null)"?
    	// Now, let's look at the recursive calls from the call Ts(984819539)
    	String answer4 = Ts(1077119163); // what is the first recursive call?
    	String answer5 = Ts(1504416045); // what is the second recursive call?
    	// Finally let's look at the recursive calls from Ts(1504416045)
    	String answer6 = Ts(393682696); // what is the first recursive call?
    	String answer7 = Ts(920150819); // what is the second recursive call?
    }
    
    /// testAxx: tests of the compare helper method

    public void testA00() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, null);
    	assertEquals(-1, spy.compare(r, p1, p2));
    }
    
    public void testA01() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, null);
    	assertEquals(1, spy.compare(r, p2, p1));
    }
    
    public void testA02() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, null);
    	assertEquals(0, spy.compare(r, p1, p1));
    }
    
    public void testA03() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, null);
    	assertEquals(0, spy.compare(r, p2, p2));
    }
    
    public void testA04() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, null);
    	assertEquals(-1, spy.compare(r, p1, p2));
    }
    
    public void testA05() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, null);
    	assertEquals(1, spy.compare(r, p2, p1));
    }
    
    public void testA06() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, null);
    	assertEquals(0, spy.compare(r, p1, p1));
    }
    
    public void testA07() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, null);
    	assertEquals(0, spy.compare(r, p2, p2));
    }
    
    public void testA10() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, nonDiscrimination);
    	assertEquals(-1, spy.compare(r, p1, p2));
    }
    
    public void testA11() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, nonDiscrimination);
    	assertEquals(1, spy.compare(r, p2, p1));
    }
    
    public void testA12() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, nonDiscrimination);
    	assertEquals(0, spy.compare(r, p1, p1));
    }
    
    public void testA13() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, nonDiscrimination);
    	assertEquals(0, spy.compare(r, p2, p2));
    }
    
    public void testA14() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, nonDiscrimination);
    	assertEquals(-1, spy.compare(r, p1, p2));
    }
    
    public void testA15() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, nonDiscrimination);
    	assertEquals(1, spy.compare(r, p2, p1));
    }
    
    public void testA16() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, nonDiscrimination);
    	assertEquals(0, spy.compare(r, p1, p1));
    }
    
    public void testA17() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, nonDiscrimination);
    	assertEquals(0, spy.compare(r, p2, p2));
    }
    
    public void testA20() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertEquals(-1, spy.compare(r, p1, p2));
    }
    
    public void testA21() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertEquals(1, spy.compare(r, p2, p1));
    }
    
    public void testA22() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertEquals(0, spy.compare(r, p1, p1));
    }
    
    public void testA23() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertEquals(0, spy.compare(r, p2, p2));
    }
    
    public void testA24() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertTrue(spy.compare(r, p1, p2) < 0); // some negative value
    }
    
    public void testA25() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertTrue(spy.compare(r, p2, p1) > 0); // some positive value
    }
    
    public void testA26() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertTrue(spy.compare(r, p1, p2) < 0);
    }
    
    public void testA27() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertTrue(spy.compare(r, p2, p1) > 0);
    }
    
    public void testA28() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("head", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertTrue(spy.compare(r, p1, p2) > 0);
    }
    
    public void testA29() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("head", p1, null);
    	r = spy.create(p2, byFunctionThenId);
    	assertTrue(spy.compare(r, p2, p1) < 0);
    }
    
    
    /// testCxx: tests of the checkInRange helper method

    public void testC00() {
    	r = spy.create(null, null);
    	assertReporting(true, () -> spy.checkInRange(r, null, null, null));
    }
    
    public void testC01() {
    	r = spy.create(null, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, null, null, null));
    }
    
    public void testC02() {
    	r = spy.create(null, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, null, null, null));
    }
    
    public void testC03() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, null, null, null));
    }
    
    public void testC04() { // uncertain about whether testing for this is appropriate
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(null, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p4, null, null));
    }
    
    public void testC05() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	r = spy.create(null, null);
    	assertReporting(true, () -> spy.checkInRange(r, null, null, p1));
    }
    
    public void testC06() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	r = spy.create(null, null);
    	assertReporting(true, () -> spy.checkInRange(r, null, p1, null));
    }
    
    public void testC07() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	r = spy.create(null, null);
    	assertReporting(true, () -> spy.checkInRange(r, null, p1, p1));
    }
    
    public void testC10() {
    	Spy.Part p1 = spy.newPart(null, null, null);
    	r = spy.create(p1, null);
    	assertReporting(true, () -> spy.checkInRange(r, null, null, null));
    }
    
    public void testC11() {
    	Spy.Part p1 = spy.newPart(null, null, null);
    	r = spy.create(p1, null);
    	assertReporting(false, () -> spy.checkInRange(r, p1, null, null));
    }
    
    public void testC12() {
    	Spy.Part p1 = spy.newPart(null, null, null);
    	Spy.Part p2 = spy.newPart("head", p1, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	spy.setRight(p2, p3);
    	r = spy.create(p2, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC13() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart(null, p1, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	spy.setRight(p2, p3);
    	r = spy.create(p2, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC14() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", p1, null);
    	Spy.Part p3 = spy.newPart(null, null, null);
    	spy.setRight(p2, p3);
    	r = spy.create(p2, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC15() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", p1, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	spy.setRight(p2, p3);
    	spy.setFunction(p1, null);
    	r = spy.create(p2, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC20() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, null);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC21() {
    	Spy.Part lo = spy.newPart("arm", null, null);
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part hi = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, null);
    	assertReporting(true, () -> spy.checkInRange(r, p2, lo, hi));
    }
    
    public void testC22() {
    	Spy.Part lo = spy.newPart("arm", null, null);
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, null);
    	assertReporting(false, () -> spy.checkInRange(r, p2, lo, lo));
    }
    
    public void testC23() {
    	Spy.Part lo = spy.newPart("arm", null, null);
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, null);
    	assertReporting(false, () -> spy.checkInRange(r, p2, null, lo));
    }
    
    public void testC24() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part hi = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, null);
    	assertReporting(false, () -> spy.checkInRange(r, p2, hi, null));
    }
    
    public void testC25() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part hi = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, null);
    	assertReporting(false, () -> spy.checkInRange(r, p2, hi, hi));
    }
    
    public void testC26() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, byFunctionThenId);
    	assertReporting(false, () -> spy.checkInRange(r, p4, p3, null));
    }
    
    public void testC30() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("leg", p1, p2);
    	
    	r = spy.create(p3, null);
    	assertReporting(false, () -> spy.checkInRange(r, p3, null, null));
    }
    
    public void testC31() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("leg", p1, p2);
    	
    	r = spy.create(p3, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p3, null, null));
    }
    
    public void testC32() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("leg", p1, p2);
    	
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(false, () -> spy.checkInRange(r, p3, null, null));
    }
    
    public void testC33() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, null);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC34() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC35() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, byFunctionThenId);
    	assertReporting(false, () -> spy.checkInRange(r, p2, null, null));
    }
    
    // Larger tree tests
    public void testC40() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p4, null, null));
    }
    
    public void testC41() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p4, p1, p7));
    }
    
    public void testC42() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC43() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, p4));
    }
    
    public void testC44() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, p5));
    }
    
    public void testC45() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p2, p1, null));
    }
    
    public void testC46() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p6, null, null));
    }
    
    public void testC47() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	Spy.Part p6 = spy.newPart("arm", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p6, p2, null));
    }
    
    public void testC50() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p4, null, null));
    }
    
    public void testC51() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p4, p1, p7));
    }
    
    public void testC52() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC53() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, p4));
    }
    
    public void testC54() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, p5));
    }
    
    public void testC55() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p2, p1, null));
    }
    
    public void testC56() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p6, null, null));
    }
    
    public void testC57() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p6, p2, null));
    }
    
    public void testC60() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p2, p6);
    	spy.setRight(p4, p7);
    	spy.setRight(p2, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p3, null, null));
    }
    
    public void testC61() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p2, p6);
    	spy.setRight(p4, p7);
    	spy.setRight(p2, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p4, null, null));
    }
    
    public void testC62() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p2, p6);
    	spy.setRight(p4, p7);
    	spy.setRight(p2, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p1, null, null));
    }
    
    public void testC63() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p2, p6);
    	spy.setRight(p4, p7);
    	spy.setRight(p2, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p7, null, null));
    }
    
    public void testC64() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p2, p6);
    	spy.setRight(p4, p7);
    	spy.setRight(p2, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC65() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p2, p6);
    	spy.setRight(p4, p7);
    	spy.setRight(p2, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p6, null, null));
    }
    
    public void testC66() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p2, p6);
    	spy.setRight(p4, p7);
    	spy.setRight(p2, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p5, null, null));
    }
    
    public void testC67() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p2, p6);
    	spy.setRight(p4, p7);
    	spy.setRight(p2, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p2, p3, null));
    }
    
    public void testC68() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p2, p6);
    	spy.setRight(p4, p7);
    	spy.setRight(p2, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(false, () -> spy.checkInRange(r, p2, null, p7));
    }
    
    // bad trees
    public void testC70() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, p2);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p4, null, null));
    }
    
    public void testC71() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, p2);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(false, () -> spy.checkInRange(r, p3, null, null));
    }
    
    public void testC72() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, p2);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p1, null, null));
    }
    
    public void testC73() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, p2);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, null));
    }
    
    public void testC74() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, p2);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p6, null, null));
    }
    
    public void testC75() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, p2);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p6, p1, null));
    }
    
    public void testC76() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, p2);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p5, null, null));
    }
    
    public void testC77() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, p2);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	Spy.Part p6 = spy.newPart("head", p5, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p4, p6);
    	spy.setRight(p6, p7);
    	r = spy.create(p4, nonDiscrimination);
    	assertReporting(true, () -> spy.checkInRange(r, p7, null, null));
    }
    
    public void testC80() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, p2);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p5, p6);
    	spy.setRight(p3, p5);
    	spy.setRight(p4, p7);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(false, () -> spy.checkInRange(r, p3, null, null));
    }
    
    public void testC81() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, p2);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p5, p6);
    	spy.setRight(p3, p5);
    	spy.setRight(p4, p7);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p4, null, null));
    }
    
    public void testC82() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, p2);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p5, p6);
    	spy.setRight(p3, p5);
    	spy.setRight(p4, p7);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p1, null, null));
    }
    
    public void testC83() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, p2);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p5, p6);
    	spy.setRight(p3, p5);
    	spy.setRight(p4, p7);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p7, null, null));
    }
    
    public void testC84() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, p2);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p5, p6);
    	spy.setRight(p3, p5);
    	spy.setRight(p4, p7);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(false, () -> spy.checkInRange(r, p5, null, null));
    }
    
    public void testC85() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, p2);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p5, p6);
    	spy.setRight(p3, p5);
    	spy.setRight(p4, p7);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p6, null, null));
    }
    
    public void testC86() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	Spy.Part p5 = spy.newPart("leg", null, p2);
    	Spy.Part p6 = spy.newPart("head", null, null);
    	Spy.Part p7 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p3, p4);
    	spy.setLeft(p5, p6);
    	spy.setRight(p3, p5);
    	spy.setRight(p4, p7);
    	r = spy.create(p3, byFunctionThenId);
    	assertReporting(true, () -> spy.checkInRange(r, p2, null, null));
    }
    
    
    // testWxx: tests of wellFormed
    
    public void testW00() {
    	r = spy.create(null, null);
    	assertWellFormed(true, r);
    }
    
    public void testW01() {
    	r = spy.create(null, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW02() {
    	r = spy.create(null, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW10() {
    	Spy.Part p1 = spy.newPart(null, null, null);
    	r = spy.create(p1, null);
    	assertWellFormed(false, r);
    }
    
    public void testW20() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	r = spy.create(p1, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW21() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	r = spy.create(p2, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW22() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	
    	r = spy.create(p2, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW23() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, nonDiscrimination);
    	assertWellFormed(false, r);
    }
    
    public void testW24() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW25() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p2, p3);
    	r = spy.create(p1, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW26() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", p2, null);
    	
    	r = spy.create(p3, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW27() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p2, null);
    	
    	spy.setRight(p1, p3);
    	r = spy.create(p1, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW28() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, null);
    	
    	spy.setRight(p1, p2);
    	r = spy.create(p3, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW30() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p2, p3);
    	spy.setRight(p3, p4);
    	r = spy.create(p1, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW31() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p4, p3);
    	spy.setLeft(p3, p2);
    	spy.setLeft(p2, p1);
    	r = spy.create(p4, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW32() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p3, p4);
    	r = spy.create(p2, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW33() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", p2, null);
    	Spy.Part p4 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p3, p4);
    	r = spy.create(p3, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW34() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, null);
    	Spy.Part p4 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p1, p2);
    	spy.setRight(p3, p4);
    	r = spy.create(p3, nonDiscrimination);
    	assertWellFormed(false, r);
    }
    
    public void testW35() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	
    	spy.setRight(p2, p4);
    	r = spy.create(p2, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW36() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, null);
    	Spy.Part p4 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p3, p4);
    	spy.setRight(p1, p2);
    	r = spy.create(p3, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW37() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p4);
    	spy.setRight(p2, p3);
    	r = spy.create(p2, nonDiscrimination);
    	assertWellFormed(false, r);
    }
    
    public void testW38() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p2, null);
    	Spy.Part p4 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p3);
    	spy.setRight(p3, p4);
    	r = spy.create(p1, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW39() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p4, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW40() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	
    	spy.setRight(p1, p4);
    	spy.setRight(p2, p3);
    	r = spy.create(p1, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW41() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p2, null);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	
    	spy.setRight(p1, p4);
    	r = spy.create(p1, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW42() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, p2);
    	Spy.Part p4 = spy.newPart("arm", p3, null);
    	
    	spy.setRight(p1, p4);
    	r = spy.create(p1, nonDiscrimination);
    	assertWellFormed(false, r);
    }
    
    public void testW43() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p2, null);
    	Spy.Part p4 = spy.newPart("arm", p1, null);
    	
    	spy.setRight(p1, p3);
    	r = spy.create(p4, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW44() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", p1, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p4, nonDiscrimination);
    	assertWellFormed(false, r);
    }
    
    // byFunctionThenId comparator tests
    public void testW50() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW51() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	
    	spy.setRight(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW52() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW53() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW54() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW55() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW56() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW57() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW58() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW59() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW60() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW61() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setLeft(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW62() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setLeft(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW63() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setLeft(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW64() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW65() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW66() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p3);
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW67() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p3);
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW68() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW69() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setLeft(p1, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW70() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setLeft(p1, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW71() {
    	Spy.Part p1 = spy.newPart("leg", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setLeft(p1, p2);
    	spy.setLeft(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW72() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW73() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p3);
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW74() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("arm", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p1, p3);
    	spy.setLeft(p1, p2);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW75() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setLeft(p1, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW76() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW77() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("leg", null, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setLeft(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW78() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setLeft(p1, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW79() {
    	Spy.Part p1 = spy.newPart("head", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p2, p3);
    	r = spy.create(p1, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW80() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW81() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW82() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p3);
    	r = spy.create(p2, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW83() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p2, p3);
    	r = spy.create(p2, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW84() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("leg", p1, null);
    	Spy.Part p3 = spy.newPart("head", null, null);
    	
    	spy.setRight(p1, p3);
    	r = spy.create(p2, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW85() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("leg", p1, null);
    	Spy.Part p4 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p3, p4);
    	r = spy.create(p3, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW86() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("leg", p1, null);
    	Spy.Part p4 = spy.newPart("leg", null, null);
    	
    	spy.setLeft(p1, p2);
    	spy.setRight(p3, p4);
    	r = spy.create(p3, byFunctionThenId);
    	assertWellFormed(false, r);
    }
    
    public void testW87() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("head", null, null);
    	Spy.Part p3 = spy.newPart("leg", p1, null);
    	Spy.Part p4 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p1, p2);
    	spy.setRight(p3, p4);
    	r = spy.create(p3, byFunctionThenId);
    	assertWellFormed(false, r);
    }
    
    public void testW88() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("head", p2, null);
    	Spy.Part p4 = spy.newPart("leg", null, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p3, p4);
    	spy.setRight(p4, p5);
    	r = spy.create(p3, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    public void testW89() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("head", p2, null);
    	Spy.Part p4 = spy.newPart("leg", null, null);
    	Spy.Part p5 = spy.newPart("leg", null, null);
    	
    	spy.setRight(p3, p4);
    	spy.setRight(p4, p5);
    	r = spy.create(p3, byFunctionThenId);
    	assertWellFormed(true, r);
    }
    
    public void testW90() {
    	Spy.Part p1 = spy.newPart("arm", null, null);
    	Spy.Part p2 = spy.newPart("arm", p1, null);
    	Spy.Part p3 = spy.newPart("arm", null, null);
    	Spy.Part p4 = spy.newPart("arm", p2, null);
    	Spy.Part p5 = spy.newPart("arm", null, null);
    	
    	spy.setRight(p2, p3);
    	spy.setRight(p4, p5);
    	r = spy.create(p4, nonDiscrimination);
    	assertWellFormed(true, r);
    }
    
    
    /// testYxx: tests of the get helper method.
    
    public void testY00() {
    	Cell<Integer> mi = new Cell<>(0);
    	assertNull(spy.getHelper(null, null, mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY01() {
    	Cell<Integer> mi = new Cell<>(1);
    	assertNull(spy.getHelper(null, null, mi));
    	assertEquals(Integer.valueOf(1), mi.get());
    }
    
    public void testY02() {
    	Cell<Integer> mi = new Cell<>(0);
    	assertNull(spy.getHelper(null, "arm", mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY10() {
    	Spy.Part p1 = new Spy.Part("arm", null, null);
    	Cell<Integer> mi = new Cell<>(0);
    	assertSame(p1, spy.getHelper(p1, null, mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY11() {
    	Spy.Part p1 = new Spy.Part("arm", null, null);
    	Cell<Integer> mi = new Cell<>(1);
    	assertNull(spy.getHelper(p1, null, mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY12() {
    	Spy.Part p1 = new Spy.Part("arm", null, null);
    	Cell<Integer> mi = new Cell<>(0);
    	assertSame(p1, spy.getHelper(p1, new String("arm"), mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY13() {
    	Spy.Part p1 = new Spy.Part("arm", null, null);
    	Cell<Integer> mi = new Cell<>(1);
    	assertNull(spy.getHelper(p1, new String("arm"), mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY14() {
    	Spy.Part p1 = new Spy.Part("arm", null, null);
    	Cell<Integer> mi = new Cell<>(0);
    	assertNull(spy.getHelper(p1, "ARM", mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY15() {
    	Spy.Part p1 = new Spy.Part("arm", null, null);
    	Cell<Integer> mi = new Cell<>(1);
    	assertNull(spy.getHelper(p1, "ARM", mi));
    	assertEquals(Integer.valueOf(1), mi.get());
    }
    
    
    // helper method for making getHelper tests.
	// if your code gets caught up in the cycles
	// that means it is not being properly lazy.
	// It is continuing to look even when it has found the answer    
    protected Spy.Part makeCycle() {
    	Spy.Part cyc1 = new Spy.Part("arm", null, null);
    	Spy.Part cyc2 = new Spy.Part("leg", cyc1, null);
    	Spy.Part cyc3 = new Spy.Part("head", cyc2, cyc1);
    	spy.setLeft(cyc1, cyc3);
    	spy.setRight(cyc1, cyc2);
    	spy.setRight(cyc2, cyc3);
    	return cyc1;
    }
    
    
    /// testY2x: testing getHelper in big situations
    // The tree is the same for all these tests, except for the placement of the cycle
    public void testY20() {	
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setLeft(p2, makeCycle());
    	Cell<Integer> mi = new Cell<>(0);
    	assertSame(p1, spy.getHelper(p5, null, mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }

    public void testY21() {	
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setLeft(p2, makeCycle());
    	Cell<Integer> mi = new Cell<>(0);
    	assertSame(p1, spy.getHelper(p5, new String("leg"), mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }

    public void testY22() {	
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setRight(p2, makeCycle());
    	Cell<Integer> mi = new Cell<>(0);
    	assertSame(p2, spy.getHelper(p5, new String("arm"), mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY23() {	
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setRight(p4, makeCycle());
    	Cell<Integer> mi = new Cell<>(0);
    	assertSame(p4, spy.getHelper(p5, new String("head"), mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }

    public void testY24() {	
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setRight(p2, makeCycle());
    	Cell<Integer> mi = new Cell<>(1);
    	assertSame(p2, spy.getHelper(p5, null, mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY25() {
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setLeft(p4, makeCycle());
    	Cell<Integer> mi = new Cell<>(1);
    	assertSame(p3, spy.getHelper(p5, new String("arm"), mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY26() {	
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setRight(p7, makeCycle());
    	Cell<Integer> mi = new Cell<>(1);
    	assertSame(p6, spy.getHelper(p5, new String("leg"), mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
    public void testY27() {	
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setLeft(p6, makeCycle());
    	Cell<Integer> mi = new Cell<>(1);
    	assertSame(p5, spy.getHelper(p5, new String("head"), mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }

    public void testY28() {	
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setLeft(p4, makeCycle());
    	Cell<Integer> mi = new Cell<>(2);
    	assertSame(p3, spy.getHelper(p5, null, mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }

    public void testY29() {	
    	Spy.Part p2 = new Spy.Part("arm", null, null);
    	Spy.Part p1 = new Spy.Part("leg", null, p2);
    	Spy.Part p4 = new Spy.Part("head", null, null);
    	Spy.Part p3 = new Spy.Part("arm", p1, p4);
    	Spy.Part p6 = new Spy.Part("leg", null, null);
    	Spy.Part p7 = new Spy.Part("head", p6, null);
    	Spy.Part p9 = new Spy.Part("arm", null, null);
    	Spy.Part p8 = new Spy.Part("leg", p7, p9);
    	Spy.Part p5 = new Spy.Part("head", p3, p8);
    	
    	spy.setRight(p4, makeCycle());
    	Cell<Integer> mi = new Cell<>(3);
    	assertSame(p4, spy.getHelper(p5, null, mi));
    	assertEquals(Integer.valueOf(0), mi.get());
    }
    
}