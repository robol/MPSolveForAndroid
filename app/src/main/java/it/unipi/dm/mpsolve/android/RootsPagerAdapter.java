package it.unipi.dm.mpsolve.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

/**
 * @brief A {@link PagerAdapter} used to view the {@link RootsListFragment}, 
 * {@link RootsRendererFragment} and the {@link WelcomeFragment} when in Portrait mode. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 *
 */
public class RootsPagerAdapter extends FragmentPagerAdapter {

	private RootsListFragment rootsListFragment;
	private RootsRendererFragment rootsRendererFragment;
	private WelcomeFragment welcomeFragment = null;
	
	private boolean firstTimeVisitor = true;
	
	/**
	 * @brief Create a new instance of this {@link PagerAdapter} associated with
	 * the given {@link FragmentManager}. 
	 * 
	 * @param fm the {@link FragmentManager} that should be associated to this
	 * {@link PagerAdapter}. 
	 */
	public RootsPagerAdapter(FragmentManager fm) {
		super(fm);
		rootsListFragment = new RootsListFragment();
		rootsRendererFragment = new RootsRendererFragment();
		
		// If this is the first time that the user opens the Activity, loads
		// the welcomeFragment. Note that, at the moment being, this is done
		// every time the user loads the application. 
		if (firstTimeVisitor)
			welcomeFragment = new WelcomeFragment();
	}	
	
	/**
	 * @brief Retrieve a {@link Fragment} from the {@link PagerAdapter}. 
	 * @param position The position of the desired {@link Fragment}. 
	 * @return The {@link Fragment} found in the given position.  
	 */
	@Override
	public Fragment getItem(int position) {
		
		if (! firstTimeVisitor)
			position += 1;
		
		switch (position) {
		case 0:
			return welcomeFragment;
		case 1:
			return rootsRendererFragment;
		case 2:
			return rootsListFragment;
		default:
			return null;
		}
	}

	/**
	 * @brief Get the total number of pages in the {@link PagerAdapter}. 
	 * @return The total number of pages in the {@link PagerAdapter}. 
	 */
	@Override
	public int getCount() {
		// First time visitors will want to see the welcome
		// fragment that explains the basics. 
		return firstTimeVisitor ? 3 : 2;
	}

}
