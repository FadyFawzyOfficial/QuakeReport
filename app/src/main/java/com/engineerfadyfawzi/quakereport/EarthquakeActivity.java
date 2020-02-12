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
        
        // Create a fake list of earthquakes.
        ArrayList< Earthquake > earthquakes = new ArrayList<>();
        earthquakes.add( new Earthquake( "7.2", "San Francisco", "Feb 2, 2016" ) );
        earthquakes.add( new Earthquake( "6.1", "London", "July 20, 2015" ) );
        earthquakes.add( new Earthquake( "3.9", "Tokyo", "Nov 10, 2014" ) );
        earthquakes.add( new Earthquake( "5.4", "Mexico City", "May 3, 2014" ) );
        earthquakes.add( new Earthquake( "2.8", "Moscow", "Jan 31, 2013" ) );
        earthquakes.add( new Earthquake( "4.9", "Rio de Janeiro", "Aug 19, 2012" ) );
        earthquakes.add( new Earthquake( "1.6", "Paris", "Oct 30, 2011" ) );
        
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById( R.id.list_view );
        
        // Create a new adapter that takes the list of earthquakes as input
        EarthquakeAdapter adapter = new EarthquakeAdapter( this, earthquakes );
        
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter( adapter );
    }
}
