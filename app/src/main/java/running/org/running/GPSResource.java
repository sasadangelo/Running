package running.org.running;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSResource extends Resource {
    private static final String LOG_TAG = "GPSResource";

    protected static GPSResource instance = null;

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;

    //private boolean gpsFixAcquired = false;
    //private Location oldLocation = null;

    // If the location provided has an accurancy <= ACCURACY (10 meter) the location will be
    // considered, otherwise it will be discarded
    private final float FIX_ACCURACY = 10;

    // If the GPS signal comes at least from 2 satellites, it can be considered good.
    private final int FIX_SATELLITES = 2;

    // If the GPS signal comes within 3 seconds, it will be considered good enough
    private final int FIX_TIME = 3;

    public static GPSResource getInstance() {
        Log.i(LOG_TAG, "getInstance -- begin");
        if (instance == null) {
            instance = new GPSResource();
        }
        return instance;
    }

    protected GPSResource() {
        Log.i(LOG_TAG, "GPSResource -- begin");
        locationManager = (LocationManager) RunningApp.applicationContext.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //oldLocation = location;
                //if (location.hasAccuracy() && location.getAccuracy() < FIX_ACCURACY) {
                //    gpsFixAcquired = true;
                //} else if (oldLocation != null && (location.getTime() - oldLocation.getTime()) <= (1000 * FIX_TIME)) {
                //    gpsFixAcquired = true;
                //} else if (mKnownSatellites >= FIX_SATELLITES) {
                //    gpsFixAcquired = true;
                //}
                setState(location);
            }

            @Override
            public void onProviderDisabled(final String provider) {
            }

            @Override
            public void onProviderEnabled(final String provider) {
            }

            @Override
            public void onStatusChanged(final String provider, final int status, final Bundle extras) {
            }
        };

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public boolean isGPSEnabled() {
        Log.i(LOG_TAG, "isGPSEnabled -- begin");
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isGPSFixAcquired() {
        Log.i(LOG_TAG, "isGPSFixAcquired -- begin");
        //return gpsFixAcquired;
        return false;
    }

    public void destroy() {
        Log.i(LOG_TAG, "destroy -- begin");
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        locationManager = null;
        locationListener = null;
        instance = null;
    }
}
