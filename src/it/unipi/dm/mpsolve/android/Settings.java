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
	
	public enum Target {
		APPROXIMATE,
		ISOLATE
	}
	
	public static Algorithm getAlgorithm(Context context) {
		SharedPreferences p = 
				PreferenceManager.getDefaultSharedPreferences(context);
		
		String algPref = p.getString("pref_algorithm", "secsolve");
		
		if (algPref == "secsolve") {
			return Algorithm.SECSOLVE;
		}
		
		if (algPref == "unisolve") {
			return Algorithm.UNISOLVE;
		}
		
		Log.w("MPSolve", "Invalid preference for the algorithm found: " + 
				algPref);
		
		return Algorithm.SECSOLVE;
	}
	
	public static int digits = 10;
	
	public static Target target = Target.APPROXIMATE;
	
}