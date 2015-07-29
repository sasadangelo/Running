package running.org.running;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

    private static AppSettings instance = null;

    private static final String UNIT_STRING = "MeasureUnit";
    private static final String PREF_NAME = "RunningApp";

    public static AppSettings getInstance(){
        if (instance == null)
            instance = new AppSettings();
        return instance;
    }

    protected AppSettings() {
    }

    public int getMeasureUnit(Context context){
        return getInt(context, AppSettings.UNIT_STRING);
    }

    public void setMeasureUnit(Context context,int limit){
        putInt(context, AppSettings.UNIT_STRING, limit);
    }

    private int getInt(Context context, String tag) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, 0);
        return pref.getInt(tag, 0);
    }

    public void putInt(Context context, String tag, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(tag, value);
        editor.commit();
    }
}
