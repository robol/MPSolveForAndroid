package it.unipi.dm.mpsolve.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class RootsRendererView extends WebView {
	
	private GestureDetector gestureDetector;
	
	public RootsRendererView(Context context) {
		super(context);
		setupWebView();
	}

	public RootsRendererView(Context context, AttributeSet set) {
		super(context, set);
		setupWebView();	
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
    	
    	// Load our custom RootsRenderer implemented in HTML + js
    	loadUrl("file:///android_asset/rootsrenderer.html");
    	
    	setupGestureDetector();		
	}
	
	private void switchView() {
		loadUrl("javascript:$.rootsRenderer.switchView()");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    gestureDetector.onTouchEvent(event);		
		return true;		
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	
	private void setupGestureDetector() {
    	gestureDetector = new GestureDetector(getContext(), 
    			new GestureDetector.OnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				if (Math.abs(velocityX) > Math.abs(velocityY)) {
					switchView();
				}
				return false;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
    	});		
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
