package com.engineerfadyfawzi.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public class QueryUtils
{
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils()
    {
    
    }
    
    /**
     * Query the USGS data set and return a list of {@link Earthquake} objects.
     *
     * @param stringUrl
     * @return
     */
    public static List< Earthquake > fetchEarthquakeData( String stringUrl )
    {
        Log.i( LOG_TAG, "TEST: fetchEarthquakeData() called ..." );
        
        // Create URL object
        URL url = createUrl( stringUrl );
        
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = makeHttpRequest( url );
        
        // Extract relevant fields from the JSON response and create a list of {@link List<Earthquake}s
        List< Earthquake > earthquakes = extractFeatureFromJson( jsonResponse );
        
        // Return the list of {@link Earthquake}s
        return earthquakes;
    }
    
    /**
     * Returns new URL object from the given string URL.
     *
     * @param stringUrl
     * @return
     */
    private static URL createUrl( String stringUrl )
    {
        URL url = null;
        
        try
        {
            url = new URL( stringUrl );
        }
        catch ( MalformedURLException malformedURLException )
        {
            Log.e( LOG_TAG, "Problem building the URL", malformedURLException );
        }
        
        return url;
    }
    
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     *
     * @param url
     * @return
     */
    private static String makeHttpRequest( URL url )
    {
        String jsonResponse = "";
        
        // If the URL is null, then return early
        if ( url == null )
            return jsonResponse;
        
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        
        try
        {
            urlConnection = ( HttpURLConnection ) url.openConnection();
            urlConnection.setRequestMethod( "GET" );
            urlConnection.setReadTimeout( 10000 /* milliseconds */ );
            urlConnection.setConnectTimeout( 15000 /* milliseconds */ );
            urlConnection.connect();
            
            // If the request was successful (response code 200),
            // then read the input sream and parse the response.
            int responseCode = urlConnection.getResponseCode();
            if ( responseCode == 200 )
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream( inputStream );
            }
            else
            {
                Log.e( LOG_TAG, "Error response code: " + responseCode );
            }
            
        }
        catch ( IOException ioException )
        {
            Log.e( LOG_TAG, "Problem retrieving the earthquake JSON results.", ioException );
        }
        finally
        {
            if ( urlConnection != null )
                urlConnection.disconnect();
            
            try
            {
                if ( inputStream != null )
                    // Closing the input stream could throw an IOException.
                    inputStream.close();
            }
            catch ( IOException ioException )
            {
                Log.e( LOG_TAG, "Error closing input stream", ioException );
            }
        }
        
        return jsonResponse;
    }
    
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream( InputStream inputStream ) throws IOException
    {
        StringBuilder output = new StringBuilder();
        
        if ( inputStream != null )
        {
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream, Charset.forName( "UTF-8" ) );
            BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
            
            String line = bufferedReader.readLine();
            while ( line != null )
            {
                output.append( line );
                line = bufferedReader.readLine();
            }
        }
        
        return output.toString();
    }
    
    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List< Earthquake > extractFeatureFromJson( String earthquakeJSON )
    {
        // If the JSON string is empty or null, then return early
        if ( TextUtils.isEmpty( earthquakeJSON ) )
            return null;
        
        // Create an empty ArrayList that we can start adding earthquakes to
        List< Earthquake > earthquakes = new ArrayList<>();
        
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try
        {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject( earthquakeJSON );
            
            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray( "features" );
            
            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for ( int i = 0; i < earthquakeArray.length(); i++ )
            {
                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentEarthquake = earthquakeArray.getJSONObject( i );
                
                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.
                JSONObject properties = currentEarthquake.getJSONObject( "properties" );
                
                // Extract the value for the key called "mag"
                double magnitude = properties.getDouble( "mag" );
                
                // Extract the value for the key called "place"
                String location = properties.getString( "place" );
                
                // Extract the value for the key called "time"
                long time = properties.getLong( "time" );
                
                // Extract the value for the key called "url"
                String url = properties.getString( "url" );
                
                // Create a new {@link Earthquake} object with the magnitude, location, time and url
                // from the JSON response.
                Earthquake earthquake = new Earthquake( magnitude, location, time, url );
                
                // Add the new {@link Earthquake} to the list of earthquakes.
                earthquakes.add( earthquake );
            }
        }
        catch ( JSONException jsonException )
        {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e( "QueryUtils", "Problem parsing the earthquake JSON results", jsonException );
        }
        
        // Return the list of earthquakes
        return earthquakes;
    }
}
