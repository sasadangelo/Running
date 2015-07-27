package running.org.running;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Administrator on 7/15/2015.
 */
public class GPSManager {
    private static final int gpsMinTime = 500;
    private static final int gpsMinDistance = 0;

    private static LocationManager locationManager = null;
    private static LocationListener locationListener = null;
    private static GPSCallback gpsCallback = null;

    public GPSManager() {
        GPSManager.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                if (GPSManager.gpsCallback != null) {
                    GPSManager.gpsCallback.onGPSUpdate(location);
                }
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
    }

    public GPSCallback getGPSCallback() {
        return GPSManager.gpsCallback;
    }

    public void setGPSCallback(final GPSCallback gpsCallback) {
        GPSManager.gpsCallback = gpsCallback;
    }

    public void startListening(final Context context) {
        if (GPSManager.locationManager == null) {
            GPSManager.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        if (GPSManager.locationManager != null) {
                GPSManager.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GPSManager.locationListener);
        }
    }

    public void stopListening() {
        try {
            if (GPSManager.locationManager != null && GPSManager.locationListener != null) {
                GPSManager.locationManager.removeUpdates(GPSManager.locationListener);
            }

            GPSManager.locationManager = null;
        } catch (final Exception ex) {

        }
    }
}
