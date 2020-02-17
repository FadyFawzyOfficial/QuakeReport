package com.engineerfadyfawzi.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity
{
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.earthquake_activity );
        
        // Get the list of earthquakes from {@link QueryUtils}
        ArrayList< Earthquake > earthquakes = QueryUtils.extractEarthquakes();
        
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById( R.id.list_view );
        
        // Create a new adapter that takes the list of earthquakes as input
        EarthquakeAdapter adapter = new EarthquakeAdapter( this, earthquakes );
        
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter( adapter );
    }
}
