import java.util.Comparator;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.WiredRobot;
import edu.uwm.cs351.WiredRobot.FunctionalPart;

public class TestWiredRobot extends LockedTestCase {
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (Throwable ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}
	
	public static Comparator<FunctionalPart> nonDiscrimination = (p1,p2) -> 0;
	public static Comparator<FunctionalPart> byFunction = (p1,p2) -> p1.getFunction().compareTo(p2.getFunction());
	public static Comparator<FunctionalPart> reverseByFunction = (p2,p1) -> p1.getFunction().compareTo(p2.getFunction());
	public static Comparator<FunctionalPart> olderFirst = (p1,p2) -> p1.getId() - p2.getId();
	public static Comparator<FunctionalPart> youngerFirst = (p1,p2) -> p2.getId() - p1.getId();
	public static Comparator<FunctionalPart> byFunctionThenId = (p1,p2) -> {
		int c = p1.getFunction().compareTo(p2.getFunction());
		if (c != 0) return c;
		return p1.getId() - p2.getId();
	};
	public static Comparator<FunctionalPart> reverseByFunctionThenId = (p2,p1) -> byFunctionThenId.compare(p1,p2);
	
	WiredRobot self;
	
	@Override
	protected void setUp() {
		try {
			assert self.getFirst() == null;
			assertFalse("Assertions must be enabled to run these tests", true);
		} catch (NullPointerException ex) {
			assertTrue("All good", true);
		}
		self = new WiredRobot();
	}

	protected String asString(Supplier<?> supp) {
		try {
			return "" + supp.get();
		} catch(RuntimeException ex) {
			return ex.getClass().getSimpleName();
		}
	}
	
	protected String asString(Runnable r) {
		return asString(() -> { r.run(); return "void"; });
	}

	
	/// test0x: tests of getFirst(), addPart without comparators
	
	public void test00() {
		assertNull(self.getFirst());
	}
	
	public void test01() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("ARM", p1);
		assertEquals("ARM", p1.getFunction());
	}
	
	public void test02() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("ARM", p1);
		assertException(UnsupportedOperationException.class, () -> p1.getNext());
	}
	
	public void test03() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("ARM", p1);
		assertSame(p1, self.getFirst());
	}
	
	public void test04() {
		FunctionalPart p1 = new FunctionalPart();
		assertException(UnsupportedOperationException.class, () -> p1.getNext());
	}
	
	public void test05() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		assertTrue(self.addPart("ARM", p2));
		assertSame(p1, self.getFirst());
	}

	public void test06() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p1);
		assertSame(p1, self.getFirst());
	}

	public void test07() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertEquals("ARM", p1.getFunction());
		assertEquals("LEG", p2.getFunction());
	}
	
	public void test08() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		self.addPart("", p2);
		self.addPart("", p1);
		self.addPart("", p3);
		assertSame(p1, self.getFirst());
	}
	
	public void test09() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p3);
		self.addPart("ARM", p1);
		self.addPart("HEAD", p4);
		
		assertSame(p1, self.getFirst());
	}
	
	
	/// test1x: tests of endogenous properties
	
	public void test10() {
		FunctionalPart p1 = new FunctionalPart();
		assertEquals("null", asString(() -> p1.getFunction()));
		assertEquals("UnsupportedOperationException", asString(() -> p1.getNext()));
		assertEquals("true",asString(() -> self.addPart("ARM", p1)));
		assertEquals("IllegalArgumentException", asString(() -> self.addPart("ARM", p1)));
		assertEquals("ARM", asString(() -> p1.getFunction()));
		assertEquals("UnsupportedOperationException", asString(() -> p1.getNext()));
	}
	
	public void test11() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("ARM", p1);
		assertException(IllegalArgumentException.class, () -> self.addPart("LEG", p1));
		assertEquals("ARM", p1.getFunction());
	}
	
	public void test12() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertException(IllegalArgumentException.class, () -> self.addPart("ARM", p1));
	}
	
	public void test13() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("ARM", p1);
		WiredRobot other = new WiredRobot();
		assertException(IllegalArgumentException.class, () -> other.addPart("ARM", p1));
	}
	
	public void test14() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("ARM", p1);
		WiredRobot other = new WiredRobot();
		assertNull(other.getFirst());
	}
	
	public void test15() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		WiredRobot other = new WiredRobot();
		other.addPart("LEG", p2);
		assertSame(p1, self.getFirst());
		assertSame(p2, other.getFirst());
	}

	
	/// test2x/3x: getPart tests without comparators
	
	public void test20() {
		assertNull(self.getPart(null, 0));
	}
	
	public void test21() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("ARM", p1);
		assertSame(p1, self.getPart(null, 0));
	}
	
	public void test22() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("ARM", p1);
		assertNull(self.getPart(null, 1));
	}
	
	public void test23() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("ARM", p1);
		assertNull(self.getPart("arm", 0));
	}
	
	public void test24() {
		FunctionalPart p1 = new FunctionalPart();
		self.addPart("LEG", p1);
		assertSame(p1, self.getPart(new String("LEG"), 0));
	}
	
	public void test25() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertSame(p1, self.getPart(null, 0));
	}
	
	public void test26() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p1);
		assertSame(p1, self.getPart(null, 0));
	}
	
	public void test27() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertSame(p1, self.getPart(new String("ARM"), 0));
	}
	
	public void test28() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertSame(p2, self.getPart("LEG", 0));
	}
	
	public void test29() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertNull(self.getPart("HEAD", 0));
	}
	
	public void test30() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertNull(self.getPart("ARM", 1));		
	}

	public void test31() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertNull(self.getPart("LEG", 1));
	}
	
	public void test32() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertNull(self.getPart(null, 2));
	}
	
	public void test33() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p1);
		self.addPart("ARM", p4);
		self.addPart("HEAD", p3);
		assertSame(p2, self.getPart("ARM", 0));
	}
	
	public void test34() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p1);
		self.addPart("ARM", p4);
		self.addPart("HEAD", p3);
		assertSame(p4, self.getPart(new String("ARM"), 1));
	}
	
	public void test35() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p1);
		self.addPart("ARM", p4);
		self.addPart("HEAD", p3);
		assertSame(p1, self.getPart("LEG", 0));
	}
	
	public void test36() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p1);
		self.addPart("ARM", p4);
		self.addPart("HEAD", p3);
		assertSame(p3, self.getPart(new String("HEAD"), 0));
	}
	
	public void test37() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p1);
		self.addPart("ARM", p4);
		self.addPart("HEAD", p3);
		assertNull(self.getPart("ARM", 2));
		assertNull(self.getPart("LEG", 1));
		assertNull(self.getPart("HEAD", 1));
	}
	
	public void test38() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p1);
		self.addPart("ARM", p4);
		self.addPart("HEAD", p3);
		assertSame(p1, self.getPart(null, 0));
		assertSame(p2, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
		assertNull(self.getPart(null, 4));
	}
	
	public void test39() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.addPart("ARM", p2);
		self.addPart("LEG", p1);
		self.addPart("ARM", p4);
		self.addPart("HEAD", p3);
		assertException(IllegalArgumentException.class, () -> self.getPart(null, -1));
		assertException(IllegalArgumentException.class, () -> self.getPart("ARM", -2));
	}
	
	/// test4x/5x: test of removePart (removed for this homework)
	
	/// test6x: tests of addPart with comparators
	
	public void test60() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self = new WiredRobot(nonDiscrimination);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertSame(p1, self.getFirst());
	}
	
	public void test61() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self = new WiredRobot(byFunction);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
	}
	
	public void test62() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self = new WiredRobot(reverseByFunction);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		assertSame(p2, self.getFirst());
		assertSame(p1, self.getPart(null, 1));
	}
	
	public void test63() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		self.addPart("arm", p1);
		self.setComparator(olderFirst);
		self.addPart("leg", p2);
		self.addPart("head", p3);
		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
	}
	
	public void test65() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(null);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.addPart("ARM", p3);
		self.addPart("HEAD", p4);

		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
	}
	
	public void test66() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self = new WiredRobot(byFunction);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.addPart("ARM", p3);
		self.addPart("HEAD", p4);

		assertSame(p1, self.getFirst());
		assertSame(p3, self.getPart(null, 1));
		assertSame(p4, self.getPart(null, 2));
		assertSame(p2, self.getPart(null, 3));
	}
	
	public void test67() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(reverseByFunctionThenId);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.addPart("ARM", p3);
		self.addPart("HEAD", p4);

		assertSame(p2, self.getFirst());
		assertSame(p4, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p1, self.getPart(null, 3));
	}
	
	public void test68() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(byFunction);
		self.addPart("ARM", p1);
		self.addPart("ARM", p2);
		self.addPart("ARM", p3);
		self.addPart("ARM", p4);

		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
	}
	
	public void test69() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(youngerFirst);
		self.addPart("ARM", p1);
		self.addPart("ARM", p2);
		self.addPart("ARM", p3);
		self.addPart("ARM", p4);

		assertSame(p4, self.getFirst());
		assertSame(p3, self.getPart(null, 1));
		assertSame(p2, self.getPart(null, 2));
		assertSame(p1, self.getPart(null, 3));
	}
	
	/// test7x: no tests
	
	/// test8x: tests of setComparator
	
	public void test80() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.setComparator(null);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.setComparator(byFunction);
		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
	}
	
	public void test81() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		self.setComparator(byFunction);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.setComparator(null);
		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
	}
	
	public void test82() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(null);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.addPart("ARM", p3);
		self.addPart("HEAD", p4);
		
		self.setComparator(reverseByFunction);
		assertSame(p2, self.getFirst());
		assertSame(p4, self.getPart(null, 1));
		assertSame(p1, self.getPart(null, 2));
		assertSame(p3, self.getPart(null, 3));
	}
	
	public void test83() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(byFunction);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.addPart("ARM", p3);
		self.addPart("HEAD", p4);

		assertSame(p1, self.getFirst());
		assertSame(p3, self.getPart(null, 1));
		assertSame(p4, self.getPart(null, 2));
		assertSame(p2, self.getPart(null, 3));
		self.setComparator(byFunctionThenId);
		assertSame(p1, self.getFirst());
		assertSame(p3, self.getPart(null, 1));
		assertSame(p4, self.getPart(null, 2));
		assertSame(p2, self.getPart(null, 3));
	}
	
	public void test84() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(byFunctionThenId);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.addPart("ARM", p3);
		self.addPart("HEAD", p4);

		assertSame(p1, self.getFirst());
		assertSame(p3, self.getPart(null, 1));
		assertSame(p4, self.getPart(null, 2));
		assertSame(p2, self.getPart(null, 3));
		self.setComparator(reverseByFunctionThenId);
		assertSame(p2, self.getFirst());
		assertSame(p4, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p1, self.getPart(null, 3));
	}
	
	public void test85() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(byFunction);
		self.addPart("ARM", p1);
		self.addPart("ARM", p2);
		self.addPart("ARM", p3);
		self.addPart("ARM", p4);

		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
		self.setComparator(reverseByFunction);
		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
	}
	
	public void test86() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(byFunctionThenId);
		self.addPart("ARM", p1);
		self.addPart("ARM", p2);
		self.addPart("ARM", p3);
		self.addPart("ARM", p4);

		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
		self.setComparator(reverseByFunctionThenId);
		assertSame(p4, self.getFirst());
		assertSame(p3, self.getPart(null, 1));
		assertSame(p2, self.getPart(null, 2));
		assertSame(p1, self.getPart(null, 3));
	}
	
	public void test87() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(null);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.addPart("ARM", p3);
		self.addPart("LEG", p4);

		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
		self.setComparator(byFunctionThenId);
		assertSame(p1, self.getFirst());
		assertSame(p3, self.getPart(null, 1));
		assertSame(p2, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
	}
	
	public void test88() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(byFunction);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.addPart("ARM", p3);
		self.addPart("LEG", p4);

		assertSame(p1, self.getFirst());
		assertSame(p3, self.getPart(null, 1));
		assertSame(p2, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
		self.setComparator(null);
		assertSame(p1, self.getFirst());
		assertSame(p2, self.getPart(null, 1));
		assertSame(p3, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
	}
	
	public void test89() {
		FunctionalPart p1 = new FunctionalPart();
		FunctionalPart p2 = new FunctionalPart();
		FunctionalPart p3 = new FunctionalPart();
		FunctionalPart p4 = new FunctionalPart();
		self.setComparator(reverseByFunction);
		self.addPart("ARM", p1);
		self.addPart("LEG", p2);
		self.addPart("ARM", p3);
		self.addPart("LEG", p4);

		assertSame(p2, self.getFirst());
		assertSame(p4, self.getPart(null, 1));
		assertSame(p1, self.getPart(null, 2));
		assertSame(p3, self.getPart(null, 3));
		self.setComparator(byFunctionThenId);
		assertSame(p1, self.getFirst());
		assertSame(p3, self.getPart(null, 1));
		assertSame(p2, self.getPart(null, 2));
		assertSame(p4, self.getPart(null, 3));
	}
}

