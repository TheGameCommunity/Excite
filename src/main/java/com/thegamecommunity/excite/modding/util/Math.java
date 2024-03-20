package com.thegamecommunity.excite.modding.util;

public class Math {

	/**
	 * Calculates the closest multiple of a given number (m) to another number (n) that is greater than or equal to n.
	 *
	 * @param n the number to find the closest multiple of m to. Must be non-negative.
	 * @param m the multiple to consider. Must be non-negative.
	 * @return the closest multiple of m to n that is greater than or equal to n
	 * @throws ArithmeticException if either n or m is negative
	 *
	 * @implNote This method specifically calculates the next highest multiple of m that is greater than or equal to n.
	 * It does not find the absolute closest multiple in the mathematical sense (which would be m itself if m is greater than or equal to n).
	 *
	 * @example
	 * <pre>
	 * int closest = nearestMultiple(16, 15); // Returns 30 (the next multiple of 15 that's greater than 16)
	 * int closest = nearestMultiple(15, 15); // Returns 15 (since 15 is already a multiple of 15)
	 * int closest = nearestMultiple(0, 12);  // Returns 12 (the closest multiple of 12 greater than or equal to 0)
	 * </pre>
	 */
	public static int nearestMultiple(int n, int m) throws ArithmeticException {
	    if (n < 0) {
	        throw new ArithmeticException("number cannot be negative");
	    }
	    if(m < 0) {
	    	throw new ArithmeticException("multiple cannot be negative");
	    }

	    return n == 0 ? 0 : n == m ? m : ((m + (n - 1)) / n) * n;
	}
	
}
