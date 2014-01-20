package it.unipi.dm.mpsolve.android;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @brief Subclass of {@link PreferenceActivity} that shows the Preference
 * screen to the user.
 * 
 * The preferences selected here will then be retrieve through the global
 * {@link Settings} object stored in the {@link ApplicationData} class. 
 *  
 * @author Leonardo Robol <leo@robol.it>
 */
public class SettingsActivity extends PreferenceActivity {
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
