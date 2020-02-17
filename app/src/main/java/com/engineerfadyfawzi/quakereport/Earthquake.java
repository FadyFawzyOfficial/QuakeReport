package com.engineerfadyfawzi.quakereport;

/**
 * An {@link Earthquake} object contains information related to a single earthquake.
 */
public class Earthquake
{
    /**
     * Magnitude of the earthquake
     */
    private String mMagnitude;
    
    /**
     * Location of the earthquake
     */
    private String mLocation;
    
    /**
     * Time of the earthquake
     */
    private long mTimeInMilliseconds;
    
    /**
     * Constructs a new {@link Earthquake} object.
     *
     * @param magnitude is the magnitude (size) of the earthquake
     * @param location is the city location of the earthquake
     * @param timeInMilliseconds is the time in milliseconds (from the Epoch) when
     * the earthquake happened
     */
    public Earthquake( String magnitude, String location, long timeInMilliseconds )
    {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
    }
    
    /**
     * Returns the magnitude of the earthquake.
     */
    public String getMagnitude()
    {
        return mMagnitude;
    }
    
    /**
     * Returns the location of the earthquake.
     */
    public String getLocation()
    {
        return mLocation;
    }
    
    /**
     * Returns the time of the earthquake.
     */
    public long getTimeInMilliseconds()
    {
        return mTimeInMilliseconds;
        
    }
}