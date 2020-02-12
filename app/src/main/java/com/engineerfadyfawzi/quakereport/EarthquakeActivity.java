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
        
        // Create a fake list of earthquake locations.
        ArrayList< String > earthquakes = new ArrayList<>();
        earthquakes.add( "San Francisco" );
        earthquakes.add( "London" );
        earthquakes.add( "Tokyo" );
        earthquakes.add( "Mexico City" );
        earthquakes.add( "Moscow" );
        earthquakes.add( "Rio de Janeiro" );
        earthquakes.add( "Paris" );
        
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById( R.id.list_view );
        
        // Create a new {@link ArrayAdapter} of earthquakes
        ArrayAdapter< String > adapter = new ArrayAdapter< String >(
                this, android.R.layout.simple_list_item_1, earthquakes );
        
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter( adapter );
    }
}
