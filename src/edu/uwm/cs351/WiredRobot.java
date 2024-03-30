package edu.uwm.cs351;

import java.util.Comparator;
import java.util.function.Consumer;

import edu.uwm.cs351.util.Cell;

public class WiredRobot implements Robot {
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);
	
	private static boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	public static class FunctionalPart extends Part {
		public FunctionalPart() {
			super("W");
		}
		
		// the following fields are mutable:
		String function;
		FunctionalPart left, right;
		
		/**
		 * Return the function of this part.
		 * @return string of this part, null if this part not in a robot
		 */
		public String getFunction() {
			return function;
		}
		
		/**
		 * Return the next part in this robot.
		 * This is valid only if the part is in a robot.
		 * @return the next part, possibly null
		 * @exception IllegalStateException if this part is not a part of a robot
		 */
		public FunctionalPart getNext() {
			throw new UnsupportedOperationException("not implemented");
		}
	}
	
	private FunctionalPart root;
	private Comparator<FunctionalPart> comparator; // null = don't care
	
	/**
	 * Compare two parts according to our purposes.
	 * We use the supplied comparator except when it 
	 * says it doesn't care, in which case, we use the (unique) id to 
	 * distinguish two parts, older first.
	 * @param p1 first part, must not be null
	 * @param p2 second part, must not be null
	 * @return comparison, only 0 if p1 and p2 are the same
	 */
    private int compare(FunctionalPart p1, FunctionalPart p2) {
        if (comparator != null) {
            int result = comparator.compare(p1, p2);
            if (result != 0) {
                return result;
            }
        }
        return Long.compare(p1.getId(), p2.getId());
    }
	
	/**
	 * Check a tree of parts within bounds.
	 * The subtree must be properly ordered, and every part
	 * must have a non-null function assigned.
	 * @param r root of subtree to check, may be null
	 * @param lo lower bound (exclusive) of all parts in subtree.  
	 *   If null, then no lower bound
	 * @param hi upper bound (exclusive) of all parts in subtree.
	 *   If null, then no upper bound
	 * @return whether the subtree checks out OK.
	 * If false is returned, then exactly one report has been generated.
	 */
    private boolean checkInRange(FunctionalPart r, FunctionalPart lo, FunctionalPart hi) {
        if (r == null) {
            return true;
        }

        if (lo != null && compare(r, lo) <= 0) {
            return report("Node " + r.getId() + " is not greater than lower bound " + lo.getId());
        }

        if (hi != null && compare(r, hi) >= 0) {
            return report("Node " + r.getId() + " is not less than upper bound " + hi.getId());
        }

        if (r.function == null) {
            return report("Node " + r.getId() + " has a null function");
        }

        return checkInRange(r.left, lo, r) && checkInRange(r.right, r, hi);
    }
	
	private boolean wellFormed() {
		// Invariant:
		// 1. all the parts are arranged correctly in a BST
		// 2. no part in the robot has a null function
		// This can be delegated to the helper method
		return checkInRange(root, null, null);
	}
	
	private WiredRobot(boolean ignore) {} // do not change this constructor

	/**
	 * Create a wired robot without parts
	 * and no order.
	 */
	public WiredRobot() {
		this(null);
	}
	
	/**
	 * Create a wired robot without parts
	 * with the given order 
	 * @param comp order to use, if null, then unordered
	 */
	public WiredRobot(Comparator<FunctionalPart> comp) {
		// TODO
		assert wellFormed() : "Invariant not established by constructor";
	}
	
	/**
	 * Return the first part in this robot.
	 * @return the first part, null if this robot is empty
	 */
	public FunctionalPart getFirst() {
		return null; // TODO: don't forget invariant!
	}
	
	// helper method for an efficient implementation of "get"
	/**
	 * Return the 0-based index part from the subtree given that
	 * matches the given function.  The mutable inex should be decremented
	 * for every part we find that matches, so that if we find a match
	 * when it is zero we return that one.  Once we find the result, we stop
	 * looking (and stop decrementing).  If there is no such functional part,
	 * this method returns null.
	 * @param r subtree to examine, may be null
	 * @param function kind of part to look for, null means any part
	 * @param mutableIndex index to look for (0-based); decremented whenever a matching
	 *   part is encountered in in-order traversal until the desired index.  
	 * @return 0-based matching part, may be null
	 */
	private static FunctionalPart getHelper(FunctionalPart r, String function, Cell<Integer> mutableIndex) {
		return null; // TODO
		// Use recursion.  Do not micro-manage!
		// Never check a child for null or look at its function.
	}

	// TODO: the three robot methods

	// TODO: need helper method for setComparator
	
	/**
	 * Change the comparator used to order the robot parts.
	 * The parts will be reorganized as necessary to accommodate the new order,
	 * but two parts will be reordered only if necessary.
	 * (The sorting is "stable".) 
	 * @param comp comparator to use, if null, then henceforth the parts
	 * can be in any order.
	 */
	public void setComparator(Comparator<FunctionalPart> comp) {
		assert wellFormed() : "invariant broken in setComparator";
		// TODO: Complete this!
		// Hint: After handling special easy case,
		//   reinsert all parts back into an emptied tree
		//      in pre-order over the tree.
		//      This requires a new helper method.
		assert wellFormed() : "invariant broken by setComparator";
	}
	

	@Override
	public boolean addPart(String function, edu.uwm.cs351.Part part) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Part removePart(String function) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Part getPart(String function, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Class for internal testing.  Do not modify.
	 * Do not use in client/application code
	 */
	public static class Spy {
		/**
		 * A public version of the data structure's internal node class.
		 * This class is only used for testing.
		 */
		public static class Part extends FunctionalPart {
			/**
			 * Create a part with null function and null left and right fields.
			 */
			public Part() {
				this(null, null, null);
			}
			
			/**
			 * Create a part with the given values
			 * @param f function for new part, may be null
			 * @param l left for new part, may be null
			 * @param r right for new part, may be null
			 */
			public Part(String f, Part l, Part r) {
				super();
				this.function = f;
				this.left = l;
				this.right = r;
			}
		}
		
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}

		/**
		 * Create a part for testing.
		 * @param f function, may be null
		 * @param l left part, may be null
		 * @param r right part, may be null
		 * @return newly created test node
		 */
		public Part newPart(String f, Part l, Part r) {
			return new Part(f, l, r);
		}
		
		/**
		 * Set the function of a spy part
		 * @param p part to set, must not be null
		 * @param f function to set it to
		 */
		public void setFunction(Part p, String f) {
			p.function = f;
		}
		
		/**
		 * Change a part's left field
		 * @param n1 part to change, must not be null
		 * @param n2 part to point to, may be null
		 */
		public void setLeft(Part n1, Part n2) {
			n1.left = n2;
		}

		/**
		 * Change a part's right field
		 * @param n1 part to change, must not be null
		 * @param n2 part to point to, may be null
		 */
		public void setRight(Part n1, Part n2) {
			n1.right = n2;
		}

		/**
		 * Create an instance of the ADT with given data structure.
		 * This should only be used for testing.
		 * @param r root
		 * @param c comparator
		 * @return instance of WiredRobot with the given field values.
		 */
		public WiredRobot create(Part r, Comparator<FunctionalPart> c) {
			WiredRobot result = new WiredRobot(false);
			result.root = r;
			result.comparator = c;
			return result;
		}
		
		/**
		 * Use the robot's comparison method
		 * @param r robot to use, must not be null
		 * @param p1 one part to compare
		 * @param p2 second part to compare
		 * @return what the robot says about comparing them.
		 */
		public int compare(WiredRobot r, FunctionalPart p1, FunctionalPart p2) {
			return r.compare(p1, p2);
		}
		
		/** Call checkInRange helper method with these arguments
		 * @param wr wired robot to use, must not be null
		 * @param r subtree to examine
		 * @param lo lower bound (exclusive) of all parts in subtree. (Null means no bound)
		 * @param hi upper bound (exclusive) of all parts in subtree. (Null means no bound)
		 * @return whether any problems were found within the specified subtree 
		 */
		public boolean checkInRange(WiredRobot wr, Part r, Part lo, Part hi) {
			return wr.checkInRange(r, lo, hi);
		}

		/**
		 * Call the helper method with these arguments
		 * @param r subtree to examine 
		 * @param function function to call with
		 * @param mutableIndex mutable index, must not be null
		 * @return whatever the helper method returns
		 */
		public FunctionalPart getHelper(Part r, String function, Cell<Integer> mutableIndex) {
			return WiredRobot.getHelper(r, function, mutableIndex);
		}

		/**
		 * Return whether the wellFormed routine returns true for the argument
		 * @param s robot to check.
		 * @return
		 */
		public boolean wellFormed(WiredRobot s) {
			return s.wellFormed();
		}

		
	}

}
