package it.unipi.dm.mpsolve.android;

import java.net.URISyntaxException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements 
	MarkedCallbacks {
	
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
	
	/**
	 * @brief Loads the content into the {@link View}. 
	 * 
	 * This function handles some configuration differences between devices.
	 *  
	 * Precisely: 
	 *  - Potrait view is always rendered the same, using a {@link ViewPager} with
	 *    all the {@link Fragment} in. 
	 *  - Landscape view is rendered differently on small and big devices. On the 
	 *    former only a {@link RootsListFragment} and a {@link RootsRendererFragment}
	 *    are loaded side by side, while on the latter the {@link RootsListFragment} is
	 *    replaced by the {@link WelcomeFragment}. 
	 *    
	 *  In the Landscape configuration the {@link WelcomeFragment} will be replaced
	 *  as soon as the user solves a polynomial. See the function onPolynomialSolved(). 
	 *  
	 *  This is justified by the fact that most tablet users will open the application
	 *  in Landscape mode while it is reasonable to expect that smartphone user won't. 
	 *   
	 *  Moreover, the {@link WelcomeFragment} is unlikely to fit the left half of the
	 *  screen of a small handset, so we should not use it in that case. 
	 */
	private void loadContent() {
		if (Utils.tabletViewNeeded(this)) {
			if (ApplicationData.getRootsAdapter(this).roots == null) {
				getSupportFragmentManager().beginTransaction().add(
						R.id.fragmentLayoutLeft, 
						new WelcomeFragment(), WelcomeFragment.TAG).commit(); 
			}
			else {
				getSupportFragmentManager().beginTransaction().add(
						R.id.fragmentLayoutLeft,
						new RootsListFragment(), RootsListFragment.TAG).commit();
			}
			
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
    	// Fake a clear selection event since we are changing the polynomial. 
    	onListItemSelected(-1);
    	
    	this.points = points;
    	
    	ApplicationData.stopLoadingMessage();
    	
    	if (points.length == 0) {
    		Toast.makeText(this, "Failed to parse the polynomial", 
    				Toast.LENGTH_SHORT).show();
    	}
    	else {    		
    		rootsAdapter.setPoints(points);
    	}
    	
    	// If the user is in Landscape mode and the WelcomeFragment is still loaded, replace
    	// it with the RootsListFragment. This only happens when the application is used
    	// on a Tablet device (or a big enough one). 
    	if (Utils.isLandscape(this)) {
    		if (getSupportFragmentManager().findFragmentByTag(RootsListFragment.TAG) == null) {
    			getSupportFragmentManager().beginTransaction().replace(
    					R.id.fragmentLayoutLeft, 
    					new RootsListFragment(), RootsListFragment.TAG).commit();
    		}
    	}
    	else {
    		// If the user is in Portrait mode, scroll to the first view
        	// with a representation of the roots, if needed.
			ViewPager pager = (ViewPager) findViewById(R.id.pager);
			if (pager.getAdapter().getCount() == 3 &&
					pager.getCurrentItem() == 0) {
				pager.setCurrentItem(1, true);
			}
    	}
    }

	@Override
	public void onListItemSelected(int position) {
		ApplicationData.setMarkedPosition(position);
	}
}
