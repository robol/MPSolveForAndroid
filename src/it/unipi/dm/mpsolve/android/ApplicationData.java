package it.unipi.dm.mpsolve.android;

import android.app.ProgressDialog;
import android.content.Context;

public class ApplicationData {
	
	private static RootsAdapter rootsAdapter = null;
	private static Settings settings = null;
	
	private static ProgressDialog loadingDialog = null;
	
	public static void startLoadingMessage(Context context, String title, String message) {
		stopLoadingMessage();
		loadingDialog = ProgressDialog.show(context, title, message);
	}
	
	public static void stopLoadingMessage() {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}

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
