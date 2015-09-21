package running.org.running;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings {
    private static AppSettings instance = null;

    private static final String PREF_NAME = "RunningApp";

    public static final String SPEED_PEACE_SETTING = "listSpeedSettings";

    public static final String METRONOME_SETTING = "metronomeSettings";
    public static final String MODE_METRONOME_SETTING = "modeMetronomeSettings";
    public static final String STEPS_BY_MINUTE_SETTING = "stepsByMinuteSettings";
    public static final String STEPS_BY_MINUTE_TIME_SETTING = "stepsByMinuteTimeSettings";
    public static final String STEPS_BY_MINUTE_2ND_INTERVAL_SETTING = "stepsByMinuteSecondIntervalSettings";
    public static final String STEPS_BY_MINUTE_2ND_INTERVAL_TIME_SETTING = "stepsByMinuteSecondIntervalTimeSettings";
    public static final String REPEAT_METRONOME_SETTING = "repeatMetronomeSettings";

    public static AppSettings getInstance(){
        if (instance == null)
            instance = new AppSettings();
        return instance;
    }

    protected AppSettings() {
        PreferenceManager.setDefaultValues(RunningApp.applicationContext, PREF_NAME, Context.MODE_PRIVATE, R.xml.settings, false);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        SharedPreferences pref = RunningApp.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, defaultValue);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        SharedPreferences pref = RunningApp.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return Integer.parseInt(pref.getString(key, "" + defaultValue));
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences pref = RunningApp.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences pref = RunningApp.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences pref = RunningApp.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
