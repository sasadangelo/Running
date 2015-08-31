package running.org.running;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.provider.Settings;

public class StartActivity extends ActionBarActivity implements Observer {
    private static final String LOG_TAG = "Run4Fun.StartActivity";
    private static final int REQUEST_CODE = 0;

    private Button startButton;
    private Button enableGPS;

    private TextView timerValue;
    private TextView speedValue;
    private TextView averageSpeedValue;
    private TextView distanceValue;

    private AbsoluteSizeSpan sizeSpanLarge = null;
    private AbsoluteSizeSpan sizeSpanSmall = null;

    private GPSResource gpsResource = null;

    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate -- begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity);

        startButton = (Button) findViewById(R.id.startButton);
        enableGPS = (Button) findViewById(R.id.enableGPSButton);

        timerValue = (TextView) findViewById(R.id.timerValue);
        speedValue = (TextView) findViewById(R.id.speedValue);
        averageSpeedValue = (TextView) findViewById(R.id.averageSpeedValue);
        distanceValue = (TextView) findViewById(R.id.distanceValue);

        Intent intent = getIntent();
        String timeMessage = intent.getStringExtra(RunningActivity.TIME_MESSAGE);
        String speedMessage = intent.getStringExtra(RunningActivity.SPEED_MESSAGE);
        String averageSpeedMessage = intent.getStringExtra(RunningActivity.AVERAGE_SPEED_MESSAGE);
        String distanceMessage = intent.getStringExtra(RunningActivity.DISTANCE_MESSAGE);

        timerValue.setText(timeMessage);
        speedValue.setText(speedMessage);
        averageSpeedValue.setText(averageSpeedMessage);
        distanceValue.setText(distanceMessage);

        gpsResource = GPSResource.getInstance();
        gpsResource.attach(this);

        if (gpsResource.isGPSEnabled()) {
            //((TextView) findViewById(R.id.infoMessage)).setText(getString(R.string.info));
            enableGPS.setVisibility(View.GONE);
            startButton.setVisibility(View.VISIBLE);
        } else {
            enableGPS.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.GONE);
        }
        if (gpsResource.isGpsFixAcquired()) {
            setSpeedText(R.id.infoMessage, getString(R.string.gpsReady));
        } else {
            setSpeedText(R.id.infoMessage, getString(R.string.info));
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "onDestroy -- begin");
        gpsResource.destroy();
        gpsResource = null;
        super.onDestroy();
    }

    public void update(Object context) {
        Log.i(LOG_TAG, "update -- begin");
        if (context == null) {
            onGPSUpdate();
        }
    }

    public void onGPSUpdate()  {
        Log.i(LOG_TAG, "onGPSUpdate -- begin");
        GPSResource gpsResource = GPSResource.getInstance();
        if (gpsResource.isGpsFixAcquired()) {
            setSpeedText(R.id.infoMessage, getString(R.string.gpsReady));
        } else {
            setSpeedText(R.id.infoMessage, getString(R.string.info));
        }
        if (gpsResource.isGPSEnabled()) {
            enableGPS.setVisibility(View.GONE);
            startButton.setVisibility(View.VISIBLE);
        } else {
            enableGPS.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG_TAG, "onCreateOptionsMenu -- begin");
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_start_activity, menu);
        //return true;
        getMenuInflater().inflate(R.menu.menu_metronome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(LOG_TAG, "onOptionsItemSelected -- begin");
        //boolean result = true;

        //switch(item.getItemId()) {
        //    case R.id.menu_about: {
        //        displayAboutDialog();
        //        break;
        //    }
        //    case R.id.unit_kmh: {
        //        AppSettings.getInstance().putInt(AppSettings.SPEED_PEACE_PREF, Constants.INDEX_KMH);
        //        break;
        //    }
        //    case R.id.unit_mk: {
        //        AppSettings.getInstance().putInt(AppSettings.SPEED_PEACE_PREF, Constants.INDEX_MIN_KM);
        //        break;
        //    }
        //    default: {
        //        result = super.onOptionsItemSelected(item);
        //        break;
        //    }
        //}
        //return result;
        switch (item.getItemId()) {
            case R.id.action_metronome:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Called when the user clicks the Start button */
    public void startRunning(View view) {
        Log.i(LOG_TAG, "startRunning -- begin");

        if (!GPSResource.getInstance().isGpsFixAcquired()) {
            try {
                new AlertDialog.Builder(StartActivity.this)
                        .setTitle("Start running")
                        .setMessage("GPS signal was not found, are you sure you want to start the running session?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(StartActivity.this, RunningActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                              // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } catch(Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /** Called when the user clicks the Enable GPS button */
    public void enableGPS(View view) {
        Log.i(LOG_TAG, "enableGPS -- begin");
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG_TAG, "onActivityResult -- begin");
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE) {
            if (gpsResource.isGPSEnabled()) {
                ((TextView) findViewById(R.id.infoMessage)).setText(getString(R.string.info));
                enableGPS.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
            } else {
                enableGPS.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);
            }
        }
    }

    private void displayAboutDialog() {
        Log.i(LOG_TAG, "displayAboutDialog -- begin");
        final LayoutInflater inflator = LayoutInflater.from(this);
        final View settingsview = inflator.inflate(R.layout.activity_about, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.app_name));
        builder.setView(settingsview);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();
    }

    private void setSpeedText(int textid, String text) {
        Log.i(LOG_TAG, "setSpeedText -- begin");
        Spannable span = new SpannableString(text);
        int firstPos = text.indexOf(32);

        span.setSpan(sizeSpanLarge, 0, firstPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(sizeSpanSmall, firstPos + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tv = ((TextView)findViewById(textid));

        tv.setText(span);
    }
}
