package it.unipi.dm.mpsolve.android;

import android.content.Context;
import android.content.res.Configuration;

public class Utils {
	
	/*
	 * @brief Detect if the application is running in landscape
	 * mode or not. 
	 * 
	 * @param view The current view
	 * @return true if the application is currently in landscape mode.
	 */
	public static boolean isLandscape(Context c) {
		return c.getResources().getConfiguration()
				.orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

}
