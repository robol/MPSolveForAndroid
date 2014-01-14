package it.unipi.dm.mpsolve.android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

public class RootsRendererFragment extends Fragment {
	
	private View rootsRenderer = null;
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("MPSolve", "Destroying view");
		rootsRenderer = null;
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		Log.d("MPSolve", "Creating the View for the RootsRendererFragment");
		
		if (rootsRenderer == null) {
			Log.d("MPSolve", "View not stored, recreating it");
			rootsRenderer = inflater.inflate(
				R.layout.roots_renderer_fragment, null);
			
			RootsRendererView view = (RootsRendererView)
					rootsRenderer.findViewById(R.id.rootsRendererView1);
			view.setRootsAdapter(ApplicationData.getRootsAdapter(getActivity()));
			
		}

		return rootsRenderer;
	}
	
}
