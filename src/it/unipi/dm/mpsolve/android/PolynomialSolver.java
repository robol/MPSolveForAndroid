package it.unipi.dm.mpsolve.android;

public class PolynomialSolver {
	
	static {
		System.loadLibrary("stlport_shared");
		System.loadLibrary("mpsolvebridge");
	}
	
	private native String[] nativeSolvePolynomial(String input);
	
	/** 
	 * Solve a polynomial. 
	 * 
	 * @param polynomial is a string representing the polynomial 
	 * to solve. 
	 * @return An array of strings containing the approximation and the 
	 * radii in odd and even positions, respectively.  
	 */
	public String[] solvePolynomial (String polynomial) {
		return nativeSolvePolynomial(polynomial);  
	}
	
}
