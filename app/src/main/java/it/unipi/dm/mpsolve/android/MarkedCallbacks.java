package it.unipi.dm.mpsolve.android;

/**
 * @brief Public interface used to handle the Marked status of an approximation. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 */
public interface MarkedCallbacks {
	/**
	 * @brief Called when the user select an approximation in the list. 
	 *  
	 * @param position may be an integer between 0 and the number of roots
	 * or -1, to clear any previous selection. 
	 */
	public void onListItemSelected(int position);
}