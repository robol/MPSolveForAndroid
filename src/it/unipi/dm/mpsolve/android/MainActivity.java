package it.unipi.dm.mpsolve.android;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private PolynomialSolver solver = new PolynomialSolver();
	private String[] points = new String[0];

    @SuppressLint("SetJavaScriptEnabled")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	
    	WebView webView = (WebView) findViewById(R.id.approximationsWebView);
    	
    	WebSettings webSettings = webView.getSettings();
    	webSettings.setJavaScriptEnabled(true);
    	
    	webView.setWebChromeClient(new WebChromeClient() {
    		  public void onConsoleMessage(String message, int lineNumber, String sourceID) {
    		    Log.d("MPSolve", message + " -- From line "
    		                     + lineNumber + " of "
    		                     + sourceID);
    		  }
    		});
    	
    	webView.loadUrl("file:///android_asset/rootsrenderer.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * @brief Inflate the points stored in the MainActivity into the WebView
     * so that they will be plotted. 
     * 
     * This method should be called after altering the value of MainActivity.points
     * or when the WebView needs to reload the plot (for example after a layout
     * change). 
     */
    public void inflatePoints() {
    	WebView webView = (WebView) findViewById(R.id.approximationsWebView); 
    	
    	webView.loadUrl("javascript:$.rootsRenderer.clear()");
    	for (int i = 0; i < points.length; i++) {
    		webView.loadUrl("javascript:$.rootsRenderer.addPoint(" + 
    				points[i].replace("(",  "[").replace(")", "]") + ")"); 
    	}
    	
    	webView.loadUrl("javascript:$.rootsRenderer.redraw()");    	
    }
    
    public void onSolveButtonClicked (View view) {     	
    	Log.d("MPSolve", "User has asked to solve a polynomial");

    	EditText polyLineEdit = (EditText) findViewById(R.id.polyEditText);
    	
    	points = solver.nativeSolvePolynomial(
    			 polyLineEdit.getText().toString());
    	inflatePoints();
    }
    
}
