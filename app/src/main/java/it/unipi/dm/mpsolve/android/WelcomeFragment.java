package it.unipi.dm.mpsolve.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * @brief A simple {@link Fragment} with a welcome message for the user and a
 * {@link Button} that allows to solve an example polynomial. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 */
public class WelcomeFragment extends Fragment {
	
	/**
	 * @brief This is the Tag that is used to register the {@link Fragment} into the
	 * {@link FragmentManager}. 
	 */
	public static final String TAG = "WelcomeFragment";

	/**
	 * @brief Create the {@link View} required for this {@link Fragment}. 
	 * @return A newly created {@link View}. 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstance) {
		
		View mainView = inflater.inflate(R.layout.welcome, null);
		
		TextView hint = (TextView) mainView.findViewById(R.id.hint);
		
		// If we are in Landscape mode the hint is not shown. 
		if (hint != null) {
			hint.setText(
					Html.fromHtml(getResources().getString(R.string.welcome_hint)));
		}
		
		return mainView;		
	}
	
}
