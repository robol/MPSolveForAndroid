package it.unipi.dm.mpsolve.android;

import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends FragmentActivity {
	
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

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGestureDetector();
        
        rootsAdapter = ApplicationData.getRootsAdapter(this);
        
        setContentView(R.layout.activity_main);
        
        loadRootsRenderer();
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
    	
    	points = solver.solvePolynomial(polyLineEdit.getText().toString());
    	
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
    
    public boolean isLandscape() {
    	return (getResources().getConfiguration().orientation == 
    				Configuration.ORIENTATION_LANDSCAPE);
    }
    
    public void loadRootsRenderer() {
    	if (isLandscape()) {
    		return;
    	}
    	
    	Log.d("MPSolve", "Loading RootsRenderer");
    	currentState = State.ROOTSRENDERER;
    	
    	FragmentManager manager = getSupportFragmentManager();
    	final FragmentTransaction ft = manager.beginTransaction();
    	
    	Fragment rootsRenderer = manager.findFragmentByTag("RootsRenderer");
    	Log.d("MPSolve", "Fragment = " + rootsRenderer);
    	if (rootsRenderer == null) {
    		rootsRenderer = new RootsRendererFragment();
    	}
    	
    	ft.replace(R.id.approximationFrame, rootsRenderer, "RootsRenderer");
    	
    	// ft.addToBackStack(null);
    	ft.commit();
    }
    
    public void loadRootsList() {
    	if (isLandscape()) {
    		return;
    	}
    	
    	Log.d("MPSolve", "Loading Roots list");
    	currentState = State.ROOTLIST;
    	
    	FragmentManager manager = getSupportFragmentManager();
    	FragmentTransaction ft = manager.beginTransaction();
    	
    	Fragment rootsFragment = manager.findFragmentByTag("RootsList");
    	
    	if (rootsFragment == null)
    		rootsFragment = new RootsListFragment();
    	
    	ft.replace(R.id.approximationFrame, rootsFragment, 
    			"RootsList");
    	
    	// ft.addToBackStack(null);    	
    	ft.commit();
    }
    
    public void switchView() {
    	Log.d("MPSolve", "Switching View");
    	
    	switch (currentState) {
    	case NONE:
    	case ROOTLIST:
    		loadRootsRenderer();
    	case ROOTSRENDERER:
    		loadRootsList();
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
