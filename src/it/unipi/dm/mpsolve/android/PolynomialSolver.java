package it.unipi.dm.mpsolve.android;

import java.lang.reflect.Array;

public class PolynomialSolver {
	
	static {
		System.loadLibrary("stlport_shared");
		/* System.loadLibrary("gmp"); 
		System.loadLibrary("mps"); */
		System.loadLibrary("mpsolvebridge");
	}

	public PolynomialSolver() {
		// TODO: Actually load MPSolve here
	}
	
	public native String[] nativeSolvePolynomial(String input);
	
	/** 
	 * Solve a polynomial. 
	 * 
	 * @param polynomial is a string representing the polynomial 
	 * to solve. 
	 * @return true if the polynomial could be parsed, false otherwise. 
	 */
	public boolean solvePolynomial (String polynomial) {
		// Assume that the polynomial is x^4 - 1, for the 
		// moment being. 		
		return false; 
	}
	
}
