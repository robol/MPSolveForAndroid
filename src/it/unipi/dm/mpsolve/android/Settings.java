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
	
	/**
	 * @brief Algorithms available in MPSolve. 
	 * @author Leonardo Robol <leo@robol.it>
	 *
	 */
	public enum Algorithm {
		UNISOLVE,
		SECSOLVE
	}
	
	/**
	 * Computation goals available in MPSolve. 
	 * @author Leonardo Robol <leo@robol.it>
	 */
	public enum Goal {
		APPROXIMATE,
		ISOLATE
	}
	
	/**
	 * @brief Retrieve the Algorithm currently selected in the preferences. 
	 * @param context The current context. 
	 * @return The {@link Algorithm} selected by the user. 
	 */
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
	
	/**
	 * @brief Get the number of correct digits required by the user, and set
	 * in preferences. 
	 * @param context The curren Android context. 
	 * @return an integer with the minimum number of correct digits required. 
	 */
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
	
	/**
	 * @brief Retrieve the current {@link Goal} selected by the user in the
	 * preferences. 
	 * 
	 * @param context The current Android context.
	 * @return The {@link Goal} selected by the user. 
	 */
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
	
	private static Goal target = Goal.APPROXIMATE;
	
}
