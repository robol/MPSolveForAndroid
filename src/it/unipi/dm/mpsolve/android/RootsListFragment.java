package it.unipi.dm.mpsolve.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class RootsListFragment extends ListFragment {
	
	public static String TAG = "RootsListFragment";
	
	private MarkedCallbacks mCallbacks = null;

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
	
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		if (mCallbacks != null)
			mCallbacks.onListItemSelected(position);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
}
