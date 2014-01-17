package it.unipi.dm.mpsolve.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RootsPagerAdapter extends FragmentPagerAdapter {

	private RootsListFragment rootsListFragment;
	private RootsRendererFragment rootsRendererFragment;
	private WelcomeFragment welcomeFragment = null;
	
	private boolean firstTimeVisitor = true;
	
	public RootsPagerAdapter(FragmentManager fm) {
		super(fm);
		rootsListFragment = new RootsListFragment();
		rootsRendererFragment = new RootsRendererFragment();
		
		if (firstTimeVisitor)
			welcomeFragment = new WelcomeFragment();
	}	
	
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

	@Override
	public int getCount() {
		// First time visitors will want to see the welcome
		// fragment that explains the basics. 
		return firstTimeVisitor ? 3 : 2;
	}

}
