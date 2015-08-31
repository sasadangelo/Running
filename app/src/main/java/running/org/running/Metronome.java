package running.org.running;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.view.Menu;
import android.view.MenuItem;

public class Metronome extends ActionBarActivity {
	private static final int MAX_TEMPO = 220;
	private static final int DEFAULT_TEMPO = 160;

    private CheckBox metronomeCheckBox;
    private Spinner metronomeSPMI1Spinner;
    private Spinner metronomeSPMI2Spinner;
    private Spinner metronomeTimeI1Spinner;
    private Spinner metronomeTimeI2Spinner;
	private Spinner metronomeRepeatSpinner;

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
		setContentView(R.layout.activity_actionbar);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MetronomeLock");
		tp = new TickPlayer(this);

		String bpm[]=new String[MAX_TEMPO+1];
		for (int i=0; i<MAX_TEMPO+1; i++) bpm[i]= "" + i;
        bpm[0]="Silent";

        metronomeSPMI1Spinner= (Spinner) findViewById(R.id.spinner_spm_metronome_i1);
		ArrayAdapter<String> adapterSPMI1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bpm);
        adapterSPMI1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metronomeSPMI1Spinner.setAdapter(adapterSPMI1);
        metronomeSPMI1Spinner.setSelection(DEFAULT_TEMPO);
        metronomeSPMI1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        metronomeSPMI2Spinner= (Spinner) findViewById(R.id.spinner_spm_metronome_i2);
        ArrayAdapter<String> adapterSPMI2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bpm);

        adapterSPMI2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metronomeSPMI2Spinner.setAdapter(adapterSPMI2);
        metronomeSPMI2Spinner.setSelection(0);
        metronomeSPMI2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        String time[] = { "2 secs", "5 secs", "15 secs", "30 secs", "1 minute", "2 minutes",
            "3 minutes", "5 minutes", "8 minutes", "10 minutes", "20 minutes", "22 minutes",
            "25 minutes", "28 minutes", "30 minutes" };

        metronomeTimeI1Spinner= (Spinner) findViewById(R.id.spinner_time_metronome_i1);
        ArrayAdapter<String> adapterTimeI1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time);

        adapterTimeI1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metronomeTimeI1Spinner.setAdapter(adapterTimeI1);
        metronomeTimeI1Spinner.setSelection(4);
        metronomeTimeI1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        metronomeTimeI2Spinner= (Spinner) findViewById(R.id.spinner_time_metronome_i2);
        ArrayAdapter<String> adapterTimeI2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time);

        adapterTimeI2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metronomeTimeI2Spinner.setAdapter(adapterTimeI2);
        metronomeTimeI2Spinner.setSelection(5);
        metronomeTimeI2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

		String repeats[] = { "1 time", "2 times", "3 times", "4 times", "5 times",
				"6 time", "7 times", "8 times", "9 times", "10 times",
				"11 time", "12 times", "13 times", "14 times", "15 times",
				"16 time", "17 times", "18 times", "19 times", "20 times",
				"Until I stop" };

		metronomeRepeatSpinner= (Spinner) findViewById(R.id.spinner_repeat_metronome);
		ArrayAdapter<String> adapterRepeat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, repeats);

		adapterTimeI2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		metronomeRepeatSpinner.setAdapter(adapterRepeat);
		metronomeRepeatSpinner.setSelection(0);
		metronomeRepeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// your code here
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

        metronomeCheckBox= (CheckBox) findViewById(R.id.checkbox_metronome);
		metronomeCheckBox.setChecked(metronomeActive);


		RadioGroup metronomeRadioGroup= (RadioGroup) findViewById(R.id.radiogroup_metronome);
		if (metronomeActive) {
			metronomeRadioGroup.setVisibility(View.VISIBLE);
            metronomeSPMI1Spinner.setVisibility(View.VISIBLE);
		} else {
			metronomeRadioGroup.setVisibility(View.GONE);
            metronomeSPMI1Spinner.setVisibility(View.GONE);
		}

		RadioButton metronomeRadioContinue= (RadioButton) findViewById(R.id.radio_metronome_continue);
		RadioButton metronomeRadioIntervals= (RadioButton) findViewById(R.id.radio_metronome_intervals);
		if (metronomeContinue) {
			metronomeRadioContinue.setChecked(true);
			metronomeRadioIntervals.setChecked(false);
            metronomeSPMI2Spinner.setVisibility(View.GONE);
            metronomeTimeI1Spinner.setVisibility(View.GONE);
            metronomeTimeI2Spinner.setVisibility(View.GONE);
			metronomeRepeatSpinner.setVisibility(View.GONE);
		} else {
			metronomeRadioContinue.setChecked(false);
			metronomeRadioIntervals.setChecked(true);
            metronomeSPMI2Spinner.setVisibility(View.VISIBLE);
            metronomeTimeI1Spinner.setVisibility(View.VISIBLE);
            metronomeTimeI2Spinner.setVisibility(View.VISIBLE);
			metronomeRepeatSpinner.setVisibility(View.VISIBLE);
		}

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
                metronomeSPMI1Spinner.setVisibility(View.VISIBLE);
                if (!metronomeContinue) {
                    metronomeSPMI2Spinner.setVisibility(View.VISIBLE);
                    metronomeTimeI1Spinner.setVisibility(View.VISIBLE);
                    metronomeTimeI2Spinner.setVisibility(View.VISIBLE);
					metronomeRepeatSpinner.setVisibility(View.VISIBLE);
                }
			} else {
				metronomeRadioGroup.setVisibility(View.GONE);
                metronomeSPMI1Spinner.setVisibility(View.GONE);
                metronomeSPMI2Spinner.setVisibility(View.GONE);
                metronomeTimeI1Spinner.setVisibility(View.GONE);
                metronomeTimeI2Spinner.setVisibility(View.GONE);
				metronomeRepeatSpinner.setVisibility(View.GONE);
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
                metronomeSPMI2Spinner.setVisibility(View.GONE);
                metronomeTimeI1Spinner.setVisibility(View.GONE);
                metronomeTimeI2Spinner.setVisibility(View.GONE);
				metronomeRepeatSpinner.setVisibility(View.GONE);
			}
			break;
		case R.id.radio_metronome_intervals:
			if (checked) {
				metronomeContinue=false;
				RadioButton metronomeRadioContinue= (RadioButton) findViewById(R.id.radio_metronome_continue);
				RadioButton metronomeRadioIntervals= (RadioButton) findViewById(R.id.radio_metronome_intervals);
				metronomeRadioContinue.setChecked(false);
				metronomeRadioIntervals.setChecked(true);
                metronomeSPMI2Spinner.setVisibility(View.VISIBLE);
                metronomeTimeI1Spinner.setVisibility(View.VISIBLE);
                metronomeTimeI2Spinner.setVisibility(View.VISIBLE);
				metronomeRepeatSpinner.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	private void restart()
	{
		if (metronomeRunning) {
			tp.onStop();
			tp.onStart(/*mPeriod,*/ metronomeTempo);
		}
	}

	protected void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
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
		Intent i = new Intent(Metronome.this, SettingsActivity.class);
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
