package running.org.running;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	public static final String KEY_SPEED_SETTINGS = "listSpeedSettings";

	public static final String KEY_METRONOME_SETTINGS = "metronomeSettings";
	public static final String KEY_MODE_METRONOME_SETTINGS = "modeMetronomeSettings";

	private ListPreference listSpeedSettings;

	private CheckBoxPreference  metronomeSettings;
	private ListPreference modeMetronomeSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		listSpeedSettings = (ListPreference)getPreferenceScreen().findPreference(KEY_SPEED_SETTINGS);
		metronomeSettings = (CheckBoxPreference)getPreferenceScreen().findPreference(KEY_METRONOME_SETTINGS);
		metronomeSettings.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (newValue.toString().equals("true")) {
				} else {
				}
				return true;
			}
		});
		modeMetronomeSettings = (ListPreference)getPreferenceScreen().findPreference(KEY_MODE_METRONOME_SETTINGS);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Setup the initial values
		listSpeedSettings.setSummary(listSpeedSettings.getEntry().toString());
		modeMetronomeSettings.setSummary(modeMetronomeSettings.getEntry().toString());
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		AppSettings.getInstance().putInt(AppSettings.SPEED_PEACE_SETTING, Constants.SPEED_SETTING_KMH);
		AppSettings.getInstance().putInt(AppSettings.METRONOME_SETTING, Constants.METRONOME_SETTING_OFF);
		AppSettings.getInstance().putInt(AppSettings.MODE_METRONOME_SETTING, Constants.MODE_METRONOME_SETTING_CONTINUE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// Set new summary, when a preference value changes
		if (key.equals(KEY_SPEED_SETTINGS)) {
			String s = listSpeedSettings.getValue().toString();
			int speedSetting = Integer.parseInt(s);
			listSpeedSettings.setSummary(listSpeedSettings.getEntry().toString());
			switch(speedSetting) {
				case R.id.unit_kmh: {
					AppSettings.getInstance().putInt(AppSettings.SPEED_PEACE_SETTING, Constants.SPEED_SETTING_KMH);
					break;
				}
				case R.id.unit_mk: {
					AppSettings.getInstance().putInt(AppSettings.SPEED_PEACE_SETTING, Constants.SPEED_SETTING_MIN_KM);
					break;
				}
			}
		}
		if (key.equals(KEY_MODE_METRONOME_SETTINGS)) {
			String s = listSpeedSettings.getValue().toString();
			int modeMetronomeSetting = Integer.parseInt(s);
			modeMetronomeSettings.setSummary(modeMetronomeSettings.getEntry().toString());
			switch(modeMetronomeSetting) {
				case Constants.MODE_METRONOME_SETTING_CONTINUE: {
					AppSettings.getInstance().putInt(AppSettings.MODE_METRONOME_SETTING, Constants.MODE_METRONOME_SETTING_CONTINUE);
					break;
				}
				case Constants.MODE_METRONOME_SETTING_INTERVAL: {
					AppSettings.getInstance().putInt(AppSettings.MODE_METRONOME_SETTING, Constants.MODE_METRONOME_SETTING_INTERVAL);
					break;
				}
			}
		}
	}
}
