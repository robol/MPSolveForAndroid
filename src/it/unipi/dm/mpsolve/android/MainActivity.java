package it.unipi.dm.mpsolve.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends FragmentActivity {
	
	public String ROOTS_LIST_TAG = "RootsList";
	public String ROOTS_RENDERER_TAG = "RootsRenderer";
	
	private PolynomialSolver solver = new PolynomialSolver();
	public String[] points = new String[0];
	
	public RootsAdapter rootsAdapter;
	private WelcomeFragment welcomeFragment;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
		
		Log.d("MPSolve", "Loading the the Fragments for the application");
		        
    	welcomeFragment = new WelcomeFragment();
        
        rootsAdapter = ApplicationData.getRootsAdapter(this);        
        setContentView(R.layout.activity_main);
        
        // Handle the loading of the contents based on the 
        // current Layout of the Device. 
        loadContent();
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		}
		
		return false;
	}
	
	private void loadContent() {
		
		// The list of Roots is needed only in case we are
		// already in Landscape mode. Otherwise it will be
		// loaded on demand as soon as the user flings left/right.
		if (Utils.isLandscape(this)) {
			getSupportFragmentManager().beginTransaction().add(
					R.id.fragmentLayoutLeft,
					new RootsListFragment(), ROOTS_LIST_TAG).commit();
			
			getSupportFragmentManager().beginTransaction().add(
					R.id.fragmentLayoutRight,
					new RootsRendererFragment(),
							ROOTS_RENDERER_TAG).commit();
		}
		else {
			ViewPager pager = (ViewPager) findViewById(R.id.pager);
			pager.setAdapter(new RootsPagerAdapter(getSupportFragmentManager()));
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onSolveButtonClicked (View view) {     	
    	Log.d("MPSolve", "User has asked to solve a polynomial");

    	EditText polyLineEdit = (EditText) findViewById(R.id.polyEditText);
    	
    	points = solver.solvePolynomial(
    			this,
    			polyLineEdit.getText().toString());
    	
    	if (points.length == 0) {
    		// TODO: Warn the user about the fact that polynomial solving has
    		// failed. 
    	}
    	else {
    		String[] roots = new String[points.length / 2];
    		String[] radii = new String[points.length / 2];
    		
    		for (int i = 0; i < roots.length; i++) {
    			roots[i] = points[2*i];
    			radii[i] = points[2*i + 1];
    		}
    		
    		rootsAdapter.setPoints(roots, radii);
    	}
    }
        
}
