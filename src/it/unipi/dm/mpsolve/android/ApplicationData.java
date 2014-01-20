package it.unipi.dm.mpsolve.android;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @brief This is a class holding all the static data used by MPSolve For Android. 
 * 
 * @author Leonardo Robo <leo@robol.it>
 */
public class ApplicationData {
	
	/**
	 * @brief This interface represents a listenere that will be
	 * notified of all the changes to the current marked position. 
	 * 
	 * See also {@link getMarkedPosition} and {@link setMarkedPosition}
	 * for getting the current value and {@link registerMarkedPositionChangedListener}
	 * and {@link registerMarkedPositionChangedListener} to register an
	 * interface. 
	 * 
	 * @author Leonardo Robol <leo@robol.it>
	 *
	 */
	public interface MarkedPositionChangedListener {
		public void onMarkedPositionChanged (int position);
	}
	
	/**
	 * @brief A pointer to the current rootsAdapter that is used in the application. 
	 */
	private static RootsAdapter rootsAdapter = null;
	
	/**
	 * @brief The application-wide {@link Settings} object. 
	 */
	private static Settings settings = null;
	
	/**
	 * @brief A global ProgressDialog that is used to notify the user
	 * when a heavy-duty computation is going on. 
	 * 
	 * This is currently used only to signal the state of the MPSolve
	 * solver. 
	 */
	private static ProgressDialog loadingDialog = null;
	
	/**
	 * @brief This is the private value that is returned by 
	 * {@link getMarkedPosition}. 
	 */
	private static int markedPosition = -1;
	
	/**
	 * @brief This is the ArrayList where the currently registered 
	 * {@link MarkedPositionListeners} are hold. 
	 */
	private static ArrayList<MarkedPositionChangedListener> mMarkedPositionListeners = 
			new ArrayList<MarkedPositionChangedListener>();
	
	/**
	 * @brief Retrieve the currently selected position in the RootsAdapter. 
	 * @return An int pointing to the currently selected position. 
	 */
	public static int getMarkedPosition() {
		return markedPosition;
	}
	
	/**
	 * @brief Set the current marked position in the RootsAdapter.
	 * Note that calling this function will also notify all the registered
	 * {@link MarkedPositionChagnedListener} object. 
	 *  
	 * @param position The currently marked position to set. 
	 */
	public static void setMarkedPosition(int position) {
		markedPosition = position;
		
		for (MarkedPositionChangedListener l : mMarkedPositionListeners) {
			l.onMarkedPositionChanged(position);
		}
	}
	
	/**
	 * @brief Register a new object implementing the MarkedPositionChangedListener interface. 
	 * @param l the object to register. 
	 */
	public static void registerMarkedPositionChangedListener(MarkedPositionChangedListener l) {
		mMarkedPositionListeners.add(l);
	}
	
	/**
	 * @brief Unregister an object previously register through a call to 
	 * {@link MarkedPositionChangedListener}. 
	 * @param l the object to unregister. 
	 */
	public static void unregisterMarkedPositionChangedListener(MarkedPositionChangedListener l) {
		mMarkedPositionListeners.remove(l);
	}
	
	/**
	 * @brief This is the external interface used to handle the global ProgressDialog stored
	 * in {@link loadingDialog}. 
	 * 
	 * @param context The current Context of the Application. 
	 * @param title The title that should be shown in the Dialog. 
	 * @param message The message that should be shown in the Dialog. 
	 */
	public static void startLoadingMessage(Context context, String title, String message) {
		stopLoadingMessage();
		loadingDialog = ProgressDialog.show(context, title, message);
	}
	
	/**
	 * @brief Clear any ProgressDialog that has been previously started through a call
	 * to {@link startLoadingMessage}. Do nothing if such a Dialog doesn't exists. 
	 */
	public static void stopLoadingMessage() {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}

	/**
	 * @brief Public interface to access the application-wide RootsAdapter. 
	 * @param context the current Context of the Application
	 * @return the global RootsAdapter
	 */
	public static RootsAdapter getRootsAdapter(Context context) {
		if (rootsAdapter == null) {
			rootsAdapter = new RootsAdapter(context);
		}
		
		return rootsAdapter;
	}
	
	/**
	 * @brief Public interface to the application-wide Settings object. 
	 * @return The required Settings object. 
	 */
	public static Settings getSettings() {
		if (settings == null) {
			settings = new Settings();
		}
		
		return settings;
	}
	
}
