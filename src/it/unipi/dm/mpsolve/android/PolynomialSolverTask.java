package it.unipi.dm.mpsolve.android;

import android.os.AsyncTask;

public class PolynomialSolverTask extends AsyncTask<Object, Void, Approximation[]> {
	
	private PolynomialSolver caller = null;
	private MainActivity     activity = null;

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
		
		if (mode == "inline") {
			return caller.nativeSolvePolynomial(polynomial);			
		}  
		else if (mode == "file") {
			return caller.nativeSolvePolynomialFile(polynomial);
		}
		
		return new Approximation[0];
	}
	
	@Override
	protected void onPostExecute(Approximation[] points) {
		activity.onPolynomialSolved(points);
	}
	
	
	
}
