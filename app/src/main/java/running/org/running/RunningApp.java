package running.org.running;

import android.app.Application;
import android.content.Context;

public class RunningApp extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}
