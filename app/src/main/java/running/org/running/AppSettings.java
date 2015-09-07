package running.org.running;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {
    private static AppSettings instance = null;

    //private static final String UNIT_STRING = "MeasureUnit";
    private static final String PREF_NAME = "RunningApp";

    public static final String SPEED_PEACE_SETTING = "SpeedPeaceSetting";

    public static final String METRONOME_SETTING = "MetronomeSetting";
    public static final String MODE_METRONOME_SETTING = "ModeMetronomeSetting";
    public static final String STEPS_BY_MINUTE_SETTING = "StepsByMinuteSetting";
    public static final String STEPS_BY_MINUTE_TIME_SETTING = "StepsByMinuteTimeSetting";
    public static final String STEPS_BY_MINUTE_2ND_INTERVAL_SETTING = "StepsByMinuteSecondIntervalSetting";
    public static final String STEPS_BY_MINUTE_2ND_INTERVAL_TIME_SETTING = "StepsByMinuteSecondIntervalTimeSetting";
    public static final String REPEAT_METRONOME_SETTING = "RepeatMetronomeSetting";

    public static AppSettings getInstance(){
        if (instance == null)
            instance = new AppSettings();
        return instance;
    }

    protected AppSettings() {
    }

    //public int getMeasureUnit(){
    //    return getInt(AppSettings.UNIT_STRING);
    //}

    //public void setMeasureUnit(int limit){
    //    putInt(AppSettings.UNIT_STRING, limit);
    //}

    public int getInt(String key) {
        SharedPreferences pref = RunningApp.applicationContext.getSharedPreferences(PREF_NAME, 0);
        return pref.getInt(key, 0);
    }

    public void putInt(String key, int value) {
        SharedPreferences pref = RunningApp.applicationContext.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }
}
