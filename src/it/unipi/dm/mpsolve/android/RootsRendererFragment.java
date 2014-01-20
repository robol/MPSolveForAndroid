package it.unipi.dm.mpsolve.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * This is a {@link Fragment} that renders the {@link Approximation} currently stored
 * in the global {@link RootsAdapter}. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 *
 */
public class RootsRendererFragment extends Fragment {
	
	/**
	 * @brief The tag that is used to register the {@link Fragment} in the
	 * {@link FragmentManager}. 
	 */
	public static String TAG = "RootsRendererFragment";
	
	/**
	 * @brief Called to create the {@link View} associated to this {@link Fragment}.
	 * @return A newly created {@link View} for the {@link Fragment}.   
	 */
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
