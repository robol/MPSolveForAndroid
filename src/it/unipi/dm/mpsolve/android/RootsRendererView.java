package it.unipi.dm.mpsolve.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;

public class RootsRendererView extends WebView {
	
	private RootsAdapter adapter;
	private DataSetObserver observer = null;
	
	private static String URL = "file:///android_asset/rootsrenderer.html";

	private boolean loadingPage = true;
	
	public RootsRendererView(Context context) {
		super(context);	
		Log.d("MPsolve", "called RootsRendererView(Context)");
		setupWebView();
	}

	public RootsRendererView(Context context, AttributeSet set) {
		super(context, set);
		Log.d("MPsolve", "called RootsRendererView(Context, AttributeSet)");
		setupWebView();	
	}
	
	public void setRootsAdapter (RootsAdapter adapter) {
    	
    	if (observer != null)
			this.adapter.unregisterDataSetObserver(observer);
		
		this.adapter = adapter;
		
		final RootsAdapter currentAdapter = adapter;

		observer = new DataSetObserver() {
			@Override 
			public void onChanged() {
				inflatePoints(currentAdapter.roots);
			}
		};
		
		adapter.registerDataSetObserver(observer);
	}

	@SuppressLint("SetJavaScriptEnabled")	
	private void setupWebView() {		
		
		if (isInEditMode())
			return;
		
    	WebSettings webSettings = getSettings();
    	webSettings.setJavaScriptEnabled(true);
    	
    	setPadding(0, 0, 0, 0);
    	
    	setWebChromeClient(new WebChromeClient() {
    		  public void onConsoleMessage(String message, int lineNumber, String sourceID) {
    		    Log.d("MPSolve", message + " -- From line "
    		                     + lineNumber + " of "
    		                     + sourceID);
    		  }
    		});
    	
    	setWebViewClient(new WebViewClient() {
    		@Override
    		public void onPageStarted(WebView view, String url, Bitmap favicon) {
    			loadingPage = true;
    			
    			// We only need this in non landscape mode. 
    			if (! Utils.isLandscape(getContext()))
    				ApplicationData.startLoadingMessage(getContext(), 
    						"Rendering", 
    						"MPSolve is loading the approximation renderer. Please wait...");    			
    		}
    		
    		public void onPageFinished(WebView view, String url) {
    			loadingPage = false;
    			
    			// Reload points in every case.
    			ApplicationData.stopLoadingMessage();
    			inflatePoints(adapter.roots);
    		}
    	});
    	
    	setRootsAdapter(ApplicationData.getRootsAdapter(getContext()));
    	
    	// Load our custom RootsRenderer implemented in HTML + js
    	Log.d("MPSolve", "Reloading web page");
    	loadUrl(RootsRendererView.URL);
	}
	
	private void callJavascript(String instruction) {
		loadUrl("javascript:$(document).ready( function() { " +
				"if (typeof $.rootsRenderer === 'undefined') { $.rootsRenderer = new RootsRenderer(); } \n" +
				 instruction +
				"});");
	}
	
    /**
     * @brief Inflate the points stored in the MainActivity into the WebView
     * so that they will be plotted. 
     * 
     * This method should be called after altering the value of MainActivity.points
     * or when the WebView needs to reload the plot (for example after a layout
     * change). 
     */
    public void inflatePoints(Approximation[] points) {
    	if (points == null)
    		return;
    	
    	if (! loadingPage) {
	    	// loadUrl("javascript:$(document).ready(function() { $.rootsRenderer.clear()); });");
    		callJavascript("$.rootsRenderer.clear();");
	    	for (int i = 0; i < points.length; i++) {
	    		// This is a poor man converter between our internal string
	    		// representation of complex numbers and arrays in JavaScript.
	    		callJavascript("$.rootsRenderer.addPoint([ " + points[i].realValue + ", " + points[i].imagValue + "]);");
	    	}
	    	
	    	callJavascript("$.rootsRenderer.redraw();");
	    	// loadUrl("javascript:$.rootsRenderer.redraw()");
    	}
    }

}
