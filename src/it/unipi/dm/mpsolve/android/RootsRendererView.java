package it.unipi.dm.mpsolve.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class RootsRendererView extends WebView {

	@SuppressLint("SetJavaScriptEnabled")
	public RootsRendererView(Context context, AttributeSet set) {
		super(context, set);
		
    	WebSettings webSettings = getSettings();
    	webSettings.setJavaScriptEnabled(true);
    	
    	setWebChromeClient(new WebChromeClient() {
    		  public void onConsoleMessage(String message, int lineNumber, String sourceID) {
    		    Log.d("MPSolve", message + " -- From line "
    		                     + lineNumber + " of "
    		                     + sourceID);
    		  }
    		});
    	
    	// Load our custom RootsRenderer implemented in HTML + js
    	loadUrl("file:///android_asset/rootsrenderer.html");
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
    	loadUrl("javascript:$.rootsRenderer.clear()");
    	for (int i = 0; i < points.length; i++) {
    		loadUrl("javascript:$.rootsRenderer.addPoint(" + 
    				points[i].replace("(",  "[").replace(")", "]") + ")"); 
    	}
    	
    	loadUrl("javascript:$.rootsRenderer.redraw()");    	
    }

}
