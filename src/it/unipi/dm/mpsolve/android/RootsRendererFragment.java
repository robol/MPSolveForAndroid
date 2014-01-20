package it.unipi.dm.mpsolve.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

public class RootsRendererFragment extends Fragment {
	
	public static String TAG = "RootsRendererFragment";
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
				
		View rootsRenderer = inflater.inflate(
			R.layout.roots_renderer_fragment, null);
			
		RootsRendererView view = (RootsRendererView)
				rootsRenderer.findViewById(R.id.rootsRendererView1);
		Activity activity = getActivity();
		
		view.setRootsAdapter(ApplicationData.getRootsAdapter(activity));

		return rootsRenderer;
	}
	
}
