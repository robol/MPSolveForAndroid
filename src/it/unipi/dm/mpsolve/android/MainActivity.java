package it.unipi.dm.mpsolve.android;

import java.lang.reflect.Array;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private PolynomialSolver solver = new PolynomialSolver();  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onSolveButtonClicked (View view) {     	
    	Log.d("MPSolve", "User has asked to solve a polynomial");
    	
    	WebView webView = (WebView) findViewById(R.id.approximationsWebView);    	
    	String[] roots = solver.nativeSolvePolynomial("Test");
    	
    	String approximationList = "<html><body> <ul>"; 
    	
    	for (int i = 0; i < 4; i++) { 
    		approximationList += "<li>" + roots[i] + "</li>"; 
    	}
    	
    	approximationList += "</ul></body></html>";
    	
    	webView.loadData(approximationList, "text/html", null);
    }
    
}
