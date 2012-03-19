package at.fhooe.mc.linzguide.android.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import at.fhooe.mc.linzguide.android.R;
import at.fhooe.mc.linzguide.android.util.GeodataUpdater;

/**
 * Used to define Update parameters
 * @author Gabler, Koller
 *
 */
public class SettingsMenu extends PreferenceActivity {

	View v;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		   // Get the custom preference
		
		
		//set the preference method for wifiStatus clicked
        Preference wifiPref = (Preference) findPreference("wifiStatus");
        wifiPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                                public boolean onPreferenceClick(Preference preference) {
                                	Intent in = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);   

                                	startActivity(in);

                                	
                                	return true;
                                     
                                }

                        });
        
      //set the preference method for gpsStatus clicked
        Preference gpsPref = (Preference) findPreference("gpsStatus");
        gpsPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                                public boolean onPreferenceClick(Preference preference) {
                                	Intent in = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);   

                                	startActivity(in);

                                	return true;
                                     
                                }

                        });
      //set the preference method for manUpdate clicked
        Preference manUpdate = (Preference) findPreference("manUpdate");
        manUpdate.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                                public boolean onPreferenceClick(Preference preference) {
                                	
                                	//call the start Update 
                                	

                                	return true;
                                     
                                }

                        });
		
		
	}


}
