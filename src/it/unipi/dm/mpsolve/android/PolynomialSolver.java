package it.unipi.dm.mpsolve.android;

import android.content.Context;

public class PolynomialSolver {
	
	static {
		System.loadLibrary("stlport_shared");
		System.loadLibrary("mpsolvebridge");
	}
	
	private char algorithm = 's';
	private int  digits = 10;
	
	private native String[] nativeSolvePolynomial(String input);
	
	/** 
	 * Solve a polynomial. 
	 * 
	 * @param polynomial is a string representing the polynomial 
	 * to solve. 
	 * @return An array of strings containing the approximation and the 
	 * radii in odd and even positions, respectively.  
	 */
	public String[] solvePolynomial (Context context, String polynomial) {
		
		switch (Settings.getAlgorithm(context)) {
		case UNISOLVE:
			algorithm = 'u';
		case SECSOLVE:
			algorithm = 's';
		}
		
		return nativeSolvePolynomial(polynomial);  
	}
	
}
