package it.unipi.dm.mpsolve.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RootsPagerAdapter extends FragmentPagerAdapter {

	private RootsListFragment rootsListFragment;
	private RootsRendererFragment rootsRendererFragment;
	
	public RootsPagerAdapter(FragmentManager fm) {
		super(fm);
		rootsListFragment = new RootsListFragment();
		rootsRendererFragment = new RootsRendererFragment();
	}	
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return rootsRendererFragment;
		case 1:
			return rootsListFragment;
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

}
