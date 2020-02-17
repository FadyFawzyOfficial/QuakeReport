package com.engineerfadyfawzi.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
        final EarthquakeAdapter adapter = new EarthquakeAdapter( this, earthquakes );
        
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter( adapter );
        
        // set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick( AdapterView< ? > adapterView, View view, int position, long l )
            {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = adapter.getItem( position );
                
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse( currentEarthquake.getUrl() );
                
                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent( Intent.ACTION_VIEW, earthquakeUri );
                
                // Send the intent to launch a new activity
                startActivity( websiteIntent );
            }
        } );
    }
}
