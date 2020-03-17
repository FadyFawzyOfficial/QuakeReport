package com.engineerfadyfawzi.quakereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderCallbacks< List< Earthquake > >
{
    /**
     * Tag for log messages
     */
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    
    /**
     * URL for earthquake data from the USGS data set
     *
     * Later we’ll use UriBuilder.appendQueryParameter() methods to add additional parameters to
     * the URI (such as JSON response format, 10 earthquakes requested, minimum magnitude value,
     * and sort order).
     */
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    
    /**
     * Adapter for the list of earthquakes
     */
    private EarthquakeAdapter mAdapter;
    
    /**
     * TextView that is displayed when the list is empty.
     */
    private TextView mEmptyStateTextView;
    
    /**
     * ProgressBar that is displayed when the app loading (internet connection is poor).
     */
    private View loadingSpinner;
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        Log.i( LOG_TAG, "TEST: Earthquake Activity onCreate() called ..." );
        
        super.onCreate( savedInstanceState );
        setContentView( R.layout.earthquake_activity );
        
        getEarthquakeData();
    }
    
    /**
     * We need onCreateLoader(), for when the LoaderManager has determined that the loader with our
     * specified ID isn't running, so we should create a new one.
     *
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader< List< Earthquake > > onCreateLoader( int id, Bundle args )
    {
        Log.i( LOG_TAG, "TEST: onCreateLoader() called ..." );
        
        // Read the user's latest preferences for the minimum magnitude,
        // construct a proper URI with their preference, and then
        // create a new Loader for that URI.
        
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( this );
        String minMagnitude = sharedPreferences.getString(
                getString( R.string.settings_min_magnitude_key ),
                getString( R.string.settings_min_magnitude_default ) );
        
        Uri baseUri = Uri.parse( USGS_REQUEST_URL );
        Uri.Builder uriBuilder = baseUri.buildUpon();
        
        uriBuilder.appendQueryParameter( "format", "geojson" );
        uriBuilder.appendQueryParameter( "limit", "10" );
        uriBuilder.appendQueryParameter( "minmag", minMagnitude );
        uriBuilder.appendQueryParameter( "orderby", "time" );
        
        // Create a new loader for the previous URL built
        return new EarthquakeLoader( this, uriBuilder.toString() );
    }
    
    /**
     * We need onLoadFinished(), where we'll do exactly what we did in onPostExecute(),
     * and use the earthquake data to update our UI - by updating the data set in the adapter.
     *
     * @param loader
     * @param earthquakes
     */
    @Override
    public void onLoadFinished( Loader< List< Earthquake > > loader, List< Earthquake > earthquakes )
    {
        Log.i( LOG_TAG, "TEST: onLoadFinished() called ..." );
        
        // Hide loading indicator because the data has been loaded
        loadingSpinner.setVisibility( View.GONE );
        
        // Check the internet connection before setText to EmptyStateTextView
        // to avoid show there no earthquakes instead of no internet while it's really not connected
        if ( isConnected() )
            mEmptyStateTextView.setText( R.string.no_earthquakes );
        else
            // Set empty state text to display "No earthquakes found."
            mEmptyStateTextView.setText( R.string.no_internet_connection );
        
        // Update the UI with the result
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        
        // If there is a valid list of {@link Earthquake}s, then add them to the adapters's
        // data set. This will trigger the ListView to update.
        if ( earthquakes != null && !earthquakes.isEmpty() )
            mAdapter.addAll( earthquakes );
    }
    
    /**
     * We need onLoaderReset(), we're we're being informed that the data from our loader
     * is no longer valid. This isn't actually a case that's going to come up with our simple
     * loader, but the correct thing to do is to remove all the earthquake data from our UI by
     * clearing out the adapter’s data set.
     *
     * @param loader
     */
    @Override
    public void onLoaderReset( Loader< List< Earthquake > > loader )
    {
        Log.i( LOG_TAG, "TEST: onLoaderReset() called ..." );
        
        // Loader reset, so we can clear out our existing data.
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        int id = item.getItemId();
        
        if ( id == R.id.action_settings )
        {
            Intent settingsIntent = new Intent( this, SettingsActivity.class );
            startActivity( settingsIntent );
            return true;
        }
        
        return super.onOptionsItemSelected( item );
    }
    
    /**
     * Just Organization: By collecting all onCreate statements in this one method
     */
    private void getEarthquakeData()
    {
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById( R.id.list_view );
        
        // initialize and set the value of this global loading spinner
        loadingSpinner = findViewById( R.id.loading_spinner );
        
        // To avoid the “No earthquakes found.” message blinking on the screen when the app first
        // launches, we can leave the empty state TextView blank, until the first load completes.
        // In the onLoadFinished callback method, we can set the text to be the string
        // “No earthquakes found.” It’s okay if this text is set every time the loader finishes
        // because it’s not too expensive of an operation. There’s always trade offs, and this user
        // experience is better.
        mEmptyStateTextView = findViewById( R.id.empty_view );
        earthquakeListView.setEmptyView( mEmptyStateTextView );
        
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter( this, new ArrayList< Earthquake >() );
        
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter( mAdapter );
        
        // If there is a network connection, fetch data
        if ( isConnected() )
        {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();
            
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.i( LOG_TAG, "TEST: calling initLoader() ..." );
            loaderManager.initLoader( EARTHQUAKE_LOADER_ID, null, this );
        }
        else
        {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingSpinner.setVisibility( View.GONE );
            
            // Update the empty state with no connection error message
            mEmptyStateTextView.setText( R.string.no_internet_connection );
        }
    }
    
    /**
     * Check if there is a network connection or not.
     *
     * @return
     */
    private boolean isConnected()
    {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager =
                ( ConnectivityManager ) getSystemService( Context.CONNECTIVITY_SERVICE );
        
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        
        // Check if there is a network connection or not and store the result in boolean variable.
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}