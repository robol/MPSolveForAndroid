package it.unipi.dm.mpsolve.android;

import java.lang.reflect.Array;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
    	Array test = solver.nativeSolvePolynomial("Test");     	
    	Log.d("MPSolve", "User has asked to solve a polynomial");
    	
    	// Assume that the polynomial is x^4 - 1, for the moment being. 
    	Button solveButton = (Button) findViewById(R.id.solveButton);
    	solveButton.setText(solver.nativeTest());
    }
    
}
