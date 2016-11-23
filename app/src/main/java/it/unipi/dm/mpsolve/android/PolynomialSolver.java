package it.unipi.dm.mpsolve.android;

import android.content.Context;
import android.util.Log;

/**
 * @brief This class is a small Java layer between the Application and
 * MPSolve. It provides convenience functions to obtain approximations
 * to the roots of a polynomial by means of {@link Approximation} objects. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 *
 */
public class PolynomialSolver {
	
	static {
		System.loadLibrary("stlport_shared");
		System.loadLibrary("mpsolvebridge");
	}
	
	/**
	 * @brief Select the algorithm to use in MPSolve: may be one of
	 * - 's': Secsolve algorithm.
	 * - 'u': Unisolve algorithm. 
	 */
	public char algorithm = 's';
	
	/**
	 * @brief The number of digits required in the approximations. 
	 */
	public int  digits = 10;
	
	/**
	 * @brief Select the MPSolve goal for the approximation: may be one
	 * of: 
	 *  - 'a': Approximate the roots with the required digits
	 *  - 'i': Isolate the roots 
	 */
	public char goal = 'a';
	
	public native Approximation[] nativeSolvePolynomial(String input);
	public native Approximation[] nativeSolvePolynomialFile(String path);
	
	/** 
	 * Solve a polynomial. 
	 * 
	 * @param context is the MainActivity that has called the 
	 * solver. 
	 * @param polynomial is a string representing the polynomial 
	 * to solve. 
	 */
	public void solvePolynomial (Context context, String polynomial) {
		new PolynomialSolverTask().execute(this, context, polynomial, "inline");
	}
	
	/** 
	 * Solve a polynomial described by a .pol file on the system.  
	 * 
	 * @param context is the MainActivity that has called the 
	 * solver. 
	 * @param path is the path to the .pol file that describes the 
	 * polynomial to solve.  
	 */	
	public void solvePolynomialFile (Context context, String path) {
		Log.d("MPSolve", "Solving .pol file with path = " + path);
		new PolynomialSolverTask().execute(this, context, path, "file");
	}
	
}
