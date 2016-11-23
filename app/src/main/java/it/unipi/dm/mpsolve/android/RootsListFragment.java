package it.unipi.dm.mpsolve.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

/**
 * @brief A {@link Fragment} that shows a list of the current approximations. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 *
 */
public class RootsListFragment extends ListFragment {
	
	/**
	 * @brief The TAG that is used to register the {@link Fragment} in the 
	 * {@link FragmentManager}. 
	 */
	public static String TAG = "RootsListFragment";
	
	/**
	 * @brief The currently registered interface for the 
	 * {@link MarkedCallbacks}, if any. The value is null otherwise. 
	 */
	private MarkedCallbacks mCallbacks = null;

	/**
	 * @brief Called when the Activity for this Fragment is created. 
	 * 
	 * In this method the {@link RootsAdapter} is loaded and the proper
	 * {@link MarkedCallbacks} are registered. 
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		setListAdapter(ApplicationData.getRootsAdapter(getActivity()));
		
		// Allow the user to select an approximation that will be 
		// marked on the plot. 
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		if (getActivity() instanceof MarkedCallbacks) {
			mCallbacks = (MarkedCallbacks) getActivity();
		}
	}
	
	/**
	 * @brief Called when the {@link Activity} removes this Fragment. 
	 * The {@link MarkedCallbacks} are removed. 
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	
	/**
	 * @brief Handle the click of the user on an item in the {@link ListView}.
	 *  
	 * This method will notify the {@link MarkedCallbacks} registered in
	 * mCallbacks.  
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		if (mCallbacks != null)
			mCallbacks.onListItemSelected(position);
	}
	
}
