package it.unipi.dm.mpsolve.android;

import android.content.Context;
import android.util.Log;

public class PolynomialSolver {
	
	static {
		System.loadLibrary("stlport_shared");
		System.loadLibrary("mpsolvebridge");
	}
	
	public char algorithm = 's';
	public int  digits = 10;
	
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
	
	public void solvePolynomialFile (Context context, String path) {
		Log.d("MPSolve", "Solving .pol file with path = " + path);
		new PolynomialSolverTask().execute(this, context, path, "file");
	}
	
}
