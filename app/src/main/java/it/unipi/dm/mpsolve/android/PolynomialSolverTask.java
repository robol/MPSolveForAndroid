package it.unipi.dm.mpsolve.android;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @brief A convenience class used to perform the polynomial solution in an
 * asynchronous way. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 *
 */
public class PolynomialSolverTask extends AsyncTask<Object, Void, Approximation[]> {
	
	private PolynomialSolver caller = null;
	private MainActivity     activity = null;

	/**
	 * @brief Override method that performs the solution of the polynomial
	 * described in params. 
	 * 
	 * @param params should be an array such that: 
	 *   params[0] is the PolynomialSolver that is calling this method. 
	 *   params[1] is the MainActivity in which we are running. 
	 *   params[2] is a String describing the polynomial, or a path to a file
	 *   params[3] is "inline" if params[2] is a string describing a polynomial, 
	 *    or "file" otherwise. 
	 * 
	 */
	@Override
	protected Approximation[] doInBackground(Object... params) {
		
		caller = (PolynomialSolver) params[0];
		activity = (MainActivity) params[1];
		String  polynomial = (String) params[2];
		String mode = (String) params[3];
		
		switch (Settings.getAlgorithm(activity)) {
		case UNISOLVE:
			caller.algorithm = 'u';
			break;
		case SECSOLVE:
			caller.algorithm = 's';
			break;
		}
		
		caller.digits = Settings.getDigits(activity);
		
		switch (Settings.getGoal(activity)) {
		case ISOLATE:
			caller.goal = 'i';
			break;
		case APPROXIMATE:
			caller.goal = 'a';
			break;
		}

		if (mode.equals("inline")) {
			return caller.nativeSolvePolynomial(polynomial);
		} else if (mode.equals("file")) {
			return caller.nativeSolvePolynomialFile(polynomial);
		}

		return null;
	}
	
	/**
	 * @brief This metod is called once the solution has been completed (successfully
	 * or not) to communicated to the MainActivity the results. 
	 * 
	 * @params points is the array containing the solutions, or an empty one
	 * if an error was encountered. 
	 */
	@Override
	protected void onPostExecute(Approximation[] points) {
		activity.onPolynomialSolved(points);
	}
	
	
	
}
