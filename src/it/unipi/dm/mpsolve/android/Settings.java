package it.unipi.dm.mpsolve.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * @brief Application wide settings. 
 * @author Leonardo Robol <leo@robol.it>
 */
public class Settings {
	
	public enum Algorithm {
		UNISOLVE,
		SECSOLVE
	}
	
	public enum Goal {
		APPROXIMATE,
		ISOLATE
	}
	
	public static Algorithm getAlgorithm(Context context) {
		SharedPreferences p = 
				PreferenceManager.getDefaultSharedPreferences(context);
		
		String algPref = p.getString("pref_algorithm", "secsolve");
		
		if (algPref.equals("secsolve")) {
			return Algorithm.SECSOLVE;
		}
		
		if (algPref.equals("unisolve")) {
			return Algorithm.UNISOLVE;
		}
		
		Log.w("MPSolve", "Invalid preference for the algorithm found: " + 
				algPref);
		
		return Algorithm.SECSOLVE;
	}
	
	public static int getDigits(Context context) {
		SharedPreferences p = 
				PreferenceManager.getDefaultSharedPreferences(context);
		
		int digits = 10;
		try {
			digits = Integer.parseInt(p.getString("pref_digits", "10"));
		} catch (NumberFormatException e) {
			Log.w("MPSolve", "Cannot parse the pref_digits value to an integer");
		}
		
		return digits;
	}
	
	public static Goal getGoal(Context context) {
		SharedPreferences p = 
				PreferenceManager.getDefaultSharedPreferences(context);
		
		try {
			String goal_c = p.getString("pref_goal", "a");
			
			if (goal_c.equals("a")) {
				target = Goal.APPROXIMATE;
			}
			else if (goal_c.equals("i")) {
				target = Goal.ISOLATE;
			}
			else {
				Log.d("MPSolve", "Unhandled string in pref_goal: " + goal_c);
			}
		} catch (Exception e) {
			Log.d("MPSolve", "Exception while parsing pref_goal");
			e.printStackTrace();
		}
		
		return target;
	}
	
	public static int digits = 10;
	
	public static Goal target = Goal.APPROXIMATE;
	
}
