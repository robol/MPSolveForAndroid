package it.unipi.dm.mpsolve.android;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends FragmentActivity {
	
	public String ROOTS_LIST_TAG = "RootsList";
	public String ROOTS_RENDERER_TAG = "RootsRenderer";
	
	private enum State {
		NONE,
		ROOTSRENDERER,
		ROOTLIST
	}
	
	private PolynomialSolver solver = new PolynomialSolver();
	public String[] points = new String[0];
	
	private GestureDetector gestureDetector;
	public RootsAdapter rootsAdapter;
	private State currentState = State.NONE;
	
	private RootsListFragment rootsListFragment;
	private RootsRendererFragment rootsRendererFragment;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGestureDetector();
        
		
		Log.d("MPSolve", "Loading the the Fragments for the application");
        if (savedInstanceState == null || true) {
        	rootsListFragment = new RootsListFragment();
        	rootsRendererFragment = new RootsRendererFragment();
        }
        	
        // TODO: We should be able to recover the Fragments from the
       	// other layout, but it currently doesn't work due to reparenting
        // issues that I haven't worked out. 
        
        rootsAdapter = ApplicationData.getRootsAdapter(this);        
        setContentView(R.layout.activity_main);
        
        // Handle the loading of the contents based on the 
        // current Layout of the Device. 
        loadContent();
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		Log.d("MPSolve", "Saving the state of the Fragments");
		
		// TODO: Saving the fragments should go here. 
		
		// Remove the fragments from the current layout
		/* getSupportFragmentManager().beginTransaction().remove(
				rootsListFragment).commitAllowingStateLoss();
		getSupportFragmentManager().beginTransaction().remove(
				rootsRendererFragment).commitAllowingStateLoss(); */
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
					rootsListFragment, ROOTS_LIST_TAG).commit();
			
			getSupportFragmentManager().beginTransaction().add(
					R.id.fragmentLayoutRight,
					rootsRendererFragment,
							ROOTS_RENDERER_TAG).commit();
		}
		else {
			loadRootsRenderer();
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
    
    public void loadRootsRenderer() {
    	int destinationFrame = Utils.isLandscape(this) ? 
    			R.id.fragmentLayoutRight : R.id.approximationFrame;
    	
    	Log.d("MPSolve", "Loading RootsRenderer");
    	currentState = State.ROOTSRENDERER;
    	
    	FragmentManager manager = getSupportFragmentManager();
    	final FragmentTransaction ft = manager.beginTransaction();

    	ft.replace(destinationFrame, rootsRendererFragment, 
    			ROOTS_RENDERER_TAG);
    	
    	ft.addToBackStack(null);
    	ft.commitAllowingStateLoss();
    }
    
    public void loadRootsList() {
    	int destinationFrame = Utils.isLandscape(this) ? 
    			R.id.fragmentLayoutLeft : R.id.approximationFrame;
    	
    	Log.d("MPSolve", "Loading Roots list");
    	currentState = State.ROOTLIST;
    	
    	FragmentManager manager = getSupportFragmentManager();
    	FragmentTransaction ft = manager.beginTransaction();
    	
    	ft.replace(destinationFrame, rootsListFragment, 
    			ROOTS_LIST_TAG);
    	
    	ft.addToBackStack(null);    	
    	ft.commitAllowingStateLoss();
    }
    
    public void switchView() {
    	// No need for this in case of landscape view, we are 
    	// are already showing showing both views. 
    	if (Utils.isLandscape(this))
    		return;
    	
    	Log.d("MPSolve", "Switching View");
    	
    	switch (currentState) {
    	case NONE:
    	case ROOTLIST:
    		loadRootsRenderer();
    		break;
    	case ROOTSRENDERER:
    		loadRootsList();
    		break;
    	}
    }
    
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    return gestureDetector.onTouchEvent(event);		
	}
	
	private void setupGestureDetector() {
    	gestureDetector = new GestureDetector(getApplicationContext(), 
    			new GestureDetector.OnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				if (Math.abs(velocityX) > Math.abs(velocityY)) {
					switchView();
					return false;
				}
				return true;
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
    
}
