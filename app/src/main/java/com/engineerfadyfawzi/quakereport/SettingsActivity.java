package com.engineerfadyfawzi.quakereport;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.settings_activity );
    }
    
    public static class EarthquakePreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate( Bundle savedInstanceState )
        {
            super.onCreate( savedInstanceState );
            addPreferencesFromResource( R.xml.settings_main );
        }
    }
}