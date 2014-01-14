package it.unipi.dm.mpsolve.android;

import android.content.Context;

public class ApplicationData {
	
	private static RootsAdapter rootsAdapter = null;
	private static Settings settings = null;

	public static RootsAdapter getRootsAdapter(Context context) {
		if (rootsAdapter == null) {
			rootsAdapter = new RootsAdapter(context);
		}
		
		return rootsAdapter;
	}
	
	public static Settings getSettings() {
		if (settings == null) {
			settings = new Settings();
		}
		
		return settings;
	}
	
}
