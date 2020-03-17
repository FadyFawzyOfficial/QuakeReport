package com.engineerfadyfawzi.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.settings_activity );
    }
    
    /**
     * This class implements the OnPreferenceChangeListener interface to get notified
     * when a preference changes.
     * Then when a single Preference has been changed by the user and is about to be saved,
     * the OnPreferenceChange() method will be invoked with the Key of the preference
     * that was changed.
     */
    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener
    {
        @Override
        public void onCreate( Bundle savedInstanceState )
        {
            super.onCreate( savedInstanceState );
            addPreferencesFromResource( R.xml.settings_main );
            
            // Update the preference summary when the settings activity is launched.
            // Use PreferenceFragment's findPreference() method to get the Preference object, and
            Preference minMagnitude = findPreference( getString( R.string.settings_min_magnitude_key ) );
            // setup the preference using this helper method.
            bindPreferenceSummaryToValue( minMagnitude );
        }
        
        /**
         * The code in this method takes care of updating the displayed preference summary
         * after it has been changed.
         *
         * Note that this method returns a boolean, which allows us to prevent a proposed preference
         * change by returning false.
         *
         * @param preference that has been changed by user.
         * @param value of the shared preference.
         * @return
         */
        @Override
        public boolean onPreferenceChange( Preference preference, Object value )
        {
            String stringValue = value.toString();
            preference.setSummary( stringValue );
            return true;
        }
        
        /**
         * Set the current EarthquakePreferenceFragment instance as the listener on each preference.
         * We also read the current value of the preference stored in the SharedPreferences on
         * the device, and display that in the preference summary (so that the user can see the
         * current value of the preference).
         *
         * @param preference that will show or update it's value
         */
        private void bindPreferenceSummaryToValue( Preference preference )
        {
            preference.setOnPreferenceChangeListener( this );
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences( preference.getContext() );
            // getString: returns the preference value if it exists, or default value which is the
            // second argument of this method (Value to return if this preference does not exist).
            String preferenceString = sharedPreferences.getString( preference.getKey(), "" );
            onPreferenceChange( preference, preferenceString );
        }
    }
}