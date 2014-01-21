package it.unipi.dm.mpsolve.android;

import android.content.Context;
import android.content.res.Configuration;

/**
 * @brief Various routines used throughout the program. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 *
 */
public class Utils {
	
	/**
	 * @brief Detect if the application is running in landscape
	 * mode or not. 
	 * 
	 * @param view The current {@link View}. 
	 * @return true if the application is currently in landscape mode.
	 */
	public static boolean isLandscape(Context c) {
		return c.getResources().getConfiguration()
				.orientation == Configuration.ORIENTATION_LANDSCAPE;
	}
	
	/**
	 * @brief Check if we are on a big enough handset/tablet so that we
	 * should enable the Tablet View for MPSolve. 
	 * 
	 * @param c A current Android {@link Context}. 
	 * @return true if the tablet view is needed. 
	 */
	public static boolean tabletViewNeeded(Context c) {
		return isLandscape(c) && (
				c.getResources().getConfiguration().screenLayout >=
				Configuration.SCREENLAYOUT_SIZE_LARGE
				);
	}

}
