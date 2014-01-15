package it.unipi.dm.mpsolve.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstance) {
		
		View mainView = inflater.inflate(R.layout.welcome, null);
		return mainView;
		
	}
	
}
