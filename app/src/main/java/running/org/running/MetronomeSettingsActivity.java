package running.org.running;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;

public class MetronomeSettingsActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_metronome_settings);

		// get action bar
		ActionBar actionBar = getSupportActionBar();

		// Enabling Up / Back navigation
		actionBar.setDisplayHomeAsUpEnabled(true);
	}}
