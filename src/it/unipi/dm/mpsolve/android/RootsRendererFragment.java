package it.unipi.dm.mpsolve.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

public class RootsRendererFragment extends Fragment {
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstance) {
		
		View rootsRenderer = inflater.inflate(
				R.layout.roots_renderer_fragment, null);
		
		MainActivity activity = (MainActivity) getActivity();
		
		// Inflate the RootsAdapter in the View.
		((RootsRendererView) rootsRenderer.findViewById(R.id.rootsRendererView1)).
			setRootsAdapter(activity.rootsAdapter);
		
		return rootsRenderer;
	}
	
}
