package it.unipi.dm.mpsolve.android;

import java.net.URISyntaxException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	private PolynomialSolver solver = new PolynomialSolver();
	
	public Approximation[] points = new Approximation[0];	
	public RootsAdapter rootsAdapter;
	
	private int currentPosition = 0;
	
	public static final int REQUEST_OPEN_POL_FILE = 1;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        rootsAdapter = ApplicationData.getRootsAdapter(this);        
        setContentView(R.layout.activity_main);
        
        // Handle the loading of the contents based on the 
        // current Layout of the Device. 
        loadContent();
        
        if (savedInstanceState != null) {
        	currentPosition  = savedInstanceState.getInt("pagerPosition");
        	
        	if (! Utils.isLandscape(this)) {
    			Log.d("MPSolve", "Restoring pagerPosition = " + currentPosition);        		
        		((ViewPager) findViewById(R.id.pager)).setCurrentItem(currentPosition, false);
        	}
        }
        else {
        	currentPosition = 0;
        }
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {	
		super.onSaveInstanceState(outState);
		
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		if (pager != null) {
			currentPosition = pager.getCurrentItem();
		}		

		Log.d("MPSolve", "Saving current pagerPosition = " + currentPosition);
		outState.putInt("pagerPosition", currentPosition);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		case R.id.action_load_file:
			loadPolFile();
			return true;
		}
		
		return false;
	}
	
	public void loadPolFile() {
		// Ask the user to select a .pol file 
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.setType("file/*");
		i.addCategory(Intent.CATEGORY_OPENABLE);
		
		startActivityForResult(i, REQUEST_OPEN_POL_FILE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		
		// Try to solve a polynomial defined by a .pol file. 
		case REQUEST_OPEN_POL_FILE:
			if (data != null) {
				Uri uri = data.getData();
				try {
					String path = FileUtils.getPath(this, uri);
					ApplicationData.startLoadingMessage(this, "Solving", 
			    			"MPSolve is working, please wait..."); 
					solver.solvePolynomialFile(this, path);
				} catch (URISyntaxException e) {
					Toast.makeText(this, "Cannot open the selected file", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void loadContent() {		
		// The list of Roots is needed only in case we are
		// already in Landscape mode. Otherwise it will be
		// loaded on demand as soon as the user flings left/right.
		if (Utils.isLandscape(this)) {
			getSupportFragmentManager().beginTransaction().add(
					R.id.fragmentLayoutLeft,
					new RootsListFragment(), RootsListFragment.TAG).commit();
			
			getSupportFragmentManager().beginTransaction().add(
					R.id.fragmentLayoutRight,
					new RootsRendererFragment(),
							RootsRendererFragment.TAG).commit();
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
    
	public void onExampleButtonClicked(View view) {
		solvePolynomial("x^70 - 2e4x^10 + 6/7");
	}
	
	public void onHintTouched(View view) {
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		if (pager != null) {
			pager.setCurrentItem(1, true);
		}
	}
    
    public void solvePolynomial (String polynomial) {
    	EditText polyLineEdit = (EditText) findViewById(R.id.polyEditText);
    	polyLineEdit.setText(polynomial);     	
    	onSolveButtonClicked(polyLineEdit);
    }
    
    public void onSolveButtonClicked (View view) {
    	
    	ApplicationData.startLoadingMessage(this, "Solving", 
    			"MPSolve is working, please wait...");

    	EditText polyLineEdit = (EditText) findViewById(R.id.polyEditText);
    	
    	solver.solvePolynomial(
    		this,
    		polyLineEdit.getText().toString());
    }
    	
    public void onPolynomialSolved (Approximation[] points) {
    	this.points = points;
    	
    	ApplicationData.stopLoadingMessage();
    	
    	if (points.length == 0) {
    		Toast.makeText(this, "Failed to parse the polynomial", 
    				Toast.LENGTH_SHORT).show();
    	}
    	else {    		
    		rootsAdapter.setPoints(points);
    	}
    	
    	// If the user is in Portrait mode, scroll to the first view
    	// with a representation of the roots, if needed.
    	// TODO: We should really handle this in the ViewPager by appropriately
    	// subclassing it. This is left for the future. 
    	if (! Utils.isLandscape(this)) {
			ViewPager pager = (ViewPager) findViewById(R.id.pager);
			if (pager.getAdapter().getCount() == 3 &&
					pager.getCurrentItem() == 0) {
				pager.setCurrentItem(1, true);
			}
    	}
    }
        
}
