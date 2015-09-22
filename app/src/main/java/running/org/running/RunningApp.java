package running.org.running;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RunningApp extends Application {
    private static final String LOG_TAG = "Run4Fun.RunningApp";
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e);
            }
        });
    }

    private void handleUncaughtException (Thread thread, Throwable e)
    {
        Log.e(LOG_TAG, "Uncaught Exception: ", e);
    }
}
