package it.unipi.dm.mpsolve.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WelcomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstance) {
		
		View mainView = inflater.inflate(R.layout.welcome, null);
		
		TextView hint = (TextView) mainView.findViewById(R.id.hint);
		hint.setText(Html.fromHtml(getResources().getString(R.string.welcome_hint)));
		
		return mainView;		
	}
	
}
