package it.unipi.dm.mpsolve.android;

import android.content.Context;

public class PolynomialSolver {
	
	static {
		System.loadLibrary("stlport_shared");
		System.loadLibrary("mpsolvebridge");
	}
	
	private char algorithm = 's';
	private int  digits = 10;
	
	private native Approximation[] nativeSolvePolynomial(String input);
	
	/** 
	 * Solve a polynomial. 
	 * 
	 * @param polynomial is a string representing the polynomial 
	 * to solve. 
	 * @return An array of strings containing the approximation and the 
	 * radii in odd and even positions, respectively.  
	 */
	public Approximation[] solvePolynomial (Context context, String polynomial) {
		
		switch (Settings.getAlgorithm(context)) {
		case UNISOLVE:
			algorithm = 'u';
			break;
		case SECSOLVE:
			algorithm = 's';
			break;
		}
		
		digits = Settings.getDigits(context);
		
		return nativeSolvePolynomial(polynomial);  
	}
	
}
