package running.org.running;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

public class Metronome extends ActionBarActivity {
	private static final int MAX_TEMPO = 220;
	private static final int DEFAULT_TEMPO = 75;

	private SeekBar metronomeSeekBar;
	private Button metronomePlusButton;
	private Button metronomeMinusButton;
	private TextView metronomeText;

	private int metronomeTempo = DEFAULT_TEMPO;

	private boolean metronomeActive = false;
	private boolean metronomeContinue = true;

	Button mStartStopButton;
	TickPlayer tp;
	PowerManager.WakeLock mWakeLock;

	private static final String KEY_TEMPO = "METRONOME_TEMPO";
	private static final String PREFS = "metronome.prefs";

	boolean metronomeRunning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_metronome);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MetronomeLock");
		tp = new TickPlayer(this);

		metronomeSeekBar = (SeekBar) findViewById(R.id.tempo);
		metronomeSeekBar.setMax(MAX_TEMPO + 1);
		metronomeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
				metronomeTempo = progress;
				metronomeText.setText("" + metronomeTempo);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				metronomeTempo = seekBar.getProgress();
				restart();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});

		metronomeMinusButton = (Button) findViewById(R.id.minus);
		metronomeMinusButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (metronomeTempo > 1) --metronomeTempo;
				restart();
			}
		});

		metronomePlusButton = (Button) findViewById(R.id.plus);
		metronomePlusButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (metronomeTempo < MAX_TEMPO) ++metronomeTempo;
				restart();
			}
		});

		metronomeText = (TextView) findViewById(R.id.text);

		CheckBox metronomecheckBox= (CheckBox) findViewById(R.id.checkbox_metronome);
		metronomecheckBox.setChecked(metronomeActive);

		RadioGroup metronomeRadioGroup= (RadioGroup) findViewById(R.id.radiogroup_metronome);
		if (metronomeActive) {
			metronomeRadioGroup.setVisibility(View.VISIBLE);
			metronomeSeekBar.setVisibility(View.VISIBLE);
			metronomeMinusButton.setVisibility(View.VISIBLE);
			metronomePlusButton.setVisibility(View.VISIBLE);
			metronomeText.setVisibility(View.VISIBLE);
		} else {
			metronomeRadioGroup.setVisibility(View.GONE);
			metronomeSeekBar.setVisibility(View.GONE);
			metronomeMinusButton.setVisibility(View.GONE);
			metronomePlusButton.setVisibility(View.GONE);
			metronomeText.setVisibility(View.GONE);
		}

		RadioButton metronomeRadioContinue= (RadioButton) findViewById(R.id.radio_metronome_continue);
		RadioButton metronomeRadioIntervals= (RadioButton) findViewById(R.id.radio_metronome_intervals);
		if (metronomeContinue) {
			metronomeRadioContinue.setChecked(true);
			metronomeRadioIntervals.setChecked(false);
		} else {
			metronomeRadioContinue.setChecked(false);
			metronomeRadioIntervals.setChecked(true);
		}

		mStartStopButton = (Button) findViewById(R.id.startstop);
		mStartStopButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				changeState();
			}
		});

		SharedPreferences settings = getSharedPreferences(PREFS, 0);
		metronomeTempo = settings.getInt(KEY_TEMPO, DEFAULT_TEMPO);
		restart();
	}

	public void onCheckboxClicked(View view) {
		metronomeActive = ((CheckBox) view).isChecked();

		RadioGroup metronomeRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_metronome);

		switch(view.getId()) {
		case R.id.checkbox_metronome:
			if (metronomeActive) {
				metronomeRadioGroup.setVisibility(View.VISIBLE);
				metronomeSeekBar.setVisibility(View.VISIBLE);
				metronomeMinusButton.setVisibility(View.VISIBLE);
				metronomePlusButton.setVisibility(View.VISIBLE);
				metronomeText.setVisibility(View.VISIBLE);
			} else {
				metronomeRadioGroup.setVisibility(View.GONE);
				metronomeSeekBar.setVisibility(View.GONE);
				metronomeMinusButton.setVisibility(View.GONE);
				metronomePlusButton.setVisibility(View.GONE);
				metronomeText.setVisibility(View.GONE);
			}
			break;
		}
	}

	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch(view.getId()) {
		case R.id.radio_metronome_continue:
			if (checked) {
				metronomeContinue=true;
				RadioButton metronomeRadioContinue= (RadioButton) findViewById(R.id.radio_metronome_continue);
				RadioButton metronomeRadioIntervals= (RadioButton) findViewById(R.id.radio_metronome_intervals);
				metronomeRadioContinue.setChecked(true);
				metronomeRadioIntervals.setChecked(false);
			}
			break;
		case R.id.radio_metronome_intervals:
			if (checked) {
				metronomeContinue=false;
				RadioButton metronomeRadioContinue= (RadioButton) findViewById(R.id.radio_metronome_continue);
				RadioButton metronomeRadioIntervals= (RadioButton) findViewById(R.id.radio_metronome_intervals);
				metronomeRadioContinue.setChecked(false);
				metronomeRadioIntervals.setChecked(true);
			}
			break;
		}
	}

	private void restart()
	{
		metronomeSeekBar.setProgress(metronomeTempo);
		metronomeText.setText("" + metronomeTempo);
		metronomeMinusButton.setClickable(metronomeTempo > 0);
		metronomePlusButton.setClickable(metronomeTempo < MAX_TEMPO);
		
		if (metronomeRunning) {
			tp.onStop();
			tp.onStart(/*mPeriod,*/ metronomeTempo);
		}
	}

	protected void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		//editor.putInt(KEY_PERIOD, mPeriod);
		editor.putInt(KEY_TEMPO, metronomeTempo);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_metronome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_metronome:
			metronomeSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void metronomeSettings() {
		Intent i = new Intent(Metronome.this, MetronomeSettingsActivity.class);
		startActivity(i);
	}

	private void changeState() {
     	metronomeRunning = !metronomeRunning;
    	if (metronomeRunning) {
    		mWakeLock.acquire();
    		
    		mStartStopButton.setText(R.string.stop);
    		tp.onStart(/*mPeriod,*/ metronomeTempo);
    	} else {
    		mWakeLock.release();
    		tp.onStop();
    		
    		mStartStopButton.setText(R.string.start);
    	}
    }

    protected void onDestroy() {
    	if (metronomeRunning) {
    		changeState();
    	}
    	tp.onDestroy();
    
    	super.onDestroy();
    }
}
