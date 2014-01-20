package it.unipi.dm.mpsolve.android;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;

public class ApplicationData {
	
	public interface MarkedPositionChangedListener {
		public void onMarkedPositionChanged (int position);
	}
	
	private static RootsAdapter rootsAdapter = null;
	private static Settings settings = null;
	private static ProgressDialog loadingDialog = null;
	
	private static int markedPosition = -1;
	private static ArrayList<MarkedPositionChangedListener> mMarkedPositionListeners = 
			new ArrayList<MarkedPositionChangedListener>();
	
	public static int getMarkedPosition() {
		return markedPosition;
	}
	
	public static void setMarkedPosition(int position) {
		markedPosition = position;
		
		for (MarkedPositionChangedListener l : mMarkedPositionListeners) {
			l.onMarkedPositionChanged(position);
		}
	}
	
	public static void registerMarkedPositionChangedListener(MarkedPositionChangedListener l) {
		mMarkedPositionListeners.add(l);
	}
	
	public static void unregisterMarkedPositionChangedListener(MarkedPositionChangedListener l) {
		mMarkedPositionListeners.remove(l);
	}
	
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
