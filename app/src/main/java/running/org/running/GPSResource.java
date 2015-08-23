package running.org.running;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.GpsStatus;
import android.location.GpsSatellite;
import android.os.Bundle;
import android.util.Log;

public class GPSResource extends Resource {
    private static final String LOG_TAG = "Run4Fun.GPSResource";

    protected static GPSResource instance = null;

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private GpsStatus.Listener gpsStatusListener = null;

    private Location oldLocation = null;

    int knownSatellites = 0;
    int usedInLastFixSatellites = 0;

    // If the location provided has an accuracy <= ACCURACY (10 meter) the location will be
    // considered, otherwise it will be discarded
    private final float FIX_ACCURACY = 10;

    // If the GPS signal comes at least from 2 satellites, it can be considered good.
    private final int FIX_SATELLITES = 2;

    // If the GPS signal comes within 3 seconds, it will be considered good enough
    private final int FIX_TIME = 3;

    // If true this boolean specify that the GPS signal has been acquired. If it is false then
    // the GPS signal is lost.
    private boolean gpsFixAcquired = false;

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
                Log.i(LOG_TAG, "onLocationChanged -- begin");
                if (location.hasAccuracy()) {
                    Log.i(LOG_TAG, "GPS location has accuracy = " + location.getAccuracy());
                } else {
                    Log.i(LOG_TAG, "GPS location is not accurate.");
                }
                if (location.hasAccuracy() && location.getAccuracy() < FIX_ACCURACY) {
                    Log.i(LOG_TAG, "GPS fix acquired. Accuracy < 10 m");
                    gpsFixAcquired = true;
                } else if (oldLocation != null && (location.getTime() - oldLocation.getTime()) <= (1000 * FIX_TIME)) {
                    Log.i(LOG_TAG, "GPS fix acquired. Signal arrived in less than 3 secs.");
                    gpsFixAcquired = true;
                } else if (knownSatellites >= FIX_SATELLITES) {
                    Log.i(LOG_TAG, "GPS fix acquired. Signal arrived from at least two satellites.");
                    gpsFixAcquired = true;
                }
                oldLocation = location;
                setState(location);
            }

            @Override
            public void onProviderDisabled(final String provider) {
                Log.i(LOG_TAG, "onLocationChanged -- begin");
                if (provider.equalsIgnoreCase("gps")) {
                    Log.i(LOG_TAG, "User disabled GPS");
                    gpsFixAcquired = false;
                    knownSatellites = 0;
                    usedInLastFixSatellites = 0;
                    oldLocation = null;
                    setState();
                }
            }

            @Override
            public void onProviderEnabled(final String provider) {
                Log.i(LOG_TAG, "onProviderEnabled -- begin");
                if (provider.equalsIgnoreCase("gps")) {
                    Log.i(LOG_TAG, "User enabled GPS");
                    knownSatellites = 0;
                    usedInLastFixSatellites = 0;
                    oldLocation = null;
                    setState();
                }
            }

            @Override
            public void onStatusChanged(final String provider, final int status, final Bundle extras) {
                Log.i(LOG_TAG, "onStatusChanged -- begin");
                if (provider.equalsIgnoreCase("gps")) {
                    if (status == LocationProvider.OUT_OF_SERVICE ||
                            status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                        Log.i(LOG_TAG, "GPS signal lost");
                        gpsFixAcquired = false;
                        knownSatellites = 0;
                        usedInLastFixSatellites = 0;
                        oldLocation = null;
                        setState();
                    }
                }
            }
        };

        gpsStatusListener = new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {
                Log.i(LOG_TAG, "onGpsStatusChanged -- begin");
                if (locationManager == null)
                    return;

                android.location.GpsStatus gpsStatus = locationManager
                        .getGpsStatus(null);

                if (gpsStatus == null)
                    return;

                int cnt0 = 0, cnt1 = 0;
                Iterable<GpsSatellite> satelliteList = gpsStatus.getSatellites();
                for (GpsSatellite satellite : satelliteList) {
                    cnt0++;
                    if (satellite.usedInFix()) {
                        cnt1++;
                    }
                }
                knownSatellites = cnt0;
                usedInLastFixSatellites = cnt1;
                Log.i(LOG_TAG, "knownSatellites: " + knownSatellites + "usedInLastFixSatellites: " +
                        usedInLastFixSatellites);
                setState();
            }
        };

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.addGpsStatusListener(gpsStatusListener);
        }
    }

    public boolean isGPSEnabled() {
        Log.i(LOG_TAG, "isGPSEnabled -- begin");
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.i(LOG_TAG, "isGPSEnabled: " + gpsEnabled);
        return gpsEnabled;
    }

    public boolean isGpsFixAcquired() {
        Log.i(LOG_TAG, "isGpsFixAcquired -- begin");
        Log.i(LOG_TAG, "isGpsFixAcquired: " + gpsFixAcquired);
        return gpsFixAcquired;
    }

    public void destroy() {
        Log.i(LOG_TAG, "destroy -- begin");
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        if (locationManager != null && gpsStatusListener != null) {
            locationManager.removeGpsStatusListener(gpsStatusListener);
        }
        locationManager = null;
        locationListener = null;
        gpsStatusListener = null;
        instance = null;
    }
}
