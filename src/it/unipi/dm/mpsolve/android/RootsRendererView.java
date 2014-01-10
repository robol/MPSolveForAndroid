package it.unipi.dm.mpsolve.android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

	private boolean loadingPage = false;
	
	private ProgressDialog loadingProgressDialog = null;
	
	public RootsRendererView(Context context) {
		super(context);		
		setupWebView();
	}

	public RootsRendererView(Context context, AttributeSet set) {
		super(context, set);
		setupWebView();	
	}
	
	public void setRootsAdapter (RootsAdapter adapter) {
    	
    	if (observer != null)
			this.adapter.unregisterDataSetObserver(observer);
		
		this.adapter = adapter;
		inflatePoints(adapter.roots);
		
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
    			loadingProgressDialog = ProgressDialog.show(getContext(), "Rendering", 
    					"MPSolve is loading the approximation renderer. Please wait...");    			
    		}
    		
    		public void onPageFinished(WebView view, String url) {
    			loadingPage = false;
    			
    			// Reload points in every case. 
    			inflatePoints(adapter.roots);    			
    			loadingProgressDialog.dismiss();
    		}
    	});
    	
    	// Load our custom RootsRenderer implemented in HTML + js
    	loadUrl(RootsRendererView.URL);
	}
	
    /**
     * @brief Inflate the points stored in the MainActivity into the WebView
     * so that they will be plotted. 
     * 
     * This method should be called after altering the value of MainActivity.points
     * or when the WebView needs to reload the plot (for example after a layout
     * change). 
     */
    public void inflatePoints(String[] points) {
    	if (points == null)
    		return;
    	
    	Log.d("MPSolve", "inflatePoints() called : loadingPage = " + loadingPage);
    	
    	if (! loadingPage) {
	    	loadUrl("javascript:$.rootsRenderer.clear()");
	    	for (int i = 0; i < points.length; i++) {
	    		// This is a poor man converter between our internal string
	    		// representation of complex numbers and arrays in JavaScript.
	    		loadUrl("javascript:$.rootsRenderer.addPoint(" + 
	    				points[i].replace("(",  "[").replace(")", "]") + ")"); 
	    	}
	    	
	    	loadUrl("javascript:$.rootsRenderer.redraw()");
    	}
    }

}
