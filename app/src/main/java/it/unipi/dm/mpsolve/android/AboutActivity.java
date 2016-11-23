package it.unipi.dm.mpsolve.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AboutActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportFragmentManager().beginTransaction().add(android.R.id.content, 
				new AboutFragment(), AboutFragment.TAG).commit();
	}
	
}
