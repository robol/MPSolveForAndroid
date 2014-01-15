package it.unipi.dm.mpsolve.android;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class RootsListFragment extends ListFragment {
	
	public static String TAG = "RootsListFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		setListAdapter(ApplicationData.getRootsAdapter(getActivity()));
	}
	
}
