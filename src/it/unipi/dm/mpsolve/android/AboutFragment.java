package it.unipi.dm.mpsolve.android;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @brief Simple {@link Fragment} showing some license and copyright information
 * about MPSolve. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 */
public class AboutFragment extends Fragment {
	
	public static final String TAG = "AboutFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		Log.d("AboutFragment", "Creating View");
		View root = inflater.inflate(R.layout.about_fragment, null);		
		TextView title = (TextView) root.findViewById(R.id.aboutTitle);
		
		String version = "";
		
		PackageInfo pInfo;
		try {
			pInfo = getActivity().getPackageManager().
					getPackageInfo(getActivity().getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		title.setText("MPSolve for Android v" + version);
		
		return root;
	}

}
