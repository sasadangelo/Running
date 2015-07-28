package running.org.running;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.provider.Settings;

public class StartActivity2 extends Activity implements Observer {
    private static final int REQUEST_CODE = 0;

    private Button startButton;
    private Button enableGPS;

    private TextView timerValue;
    private TextView speedValue;
    private TextView averageSpeedValue;
    private TextView distanceValue;

    private AbsoluteSizeSpan sizeSpanLarge = null;
    private AbsoluteSizeSpan sizeSpanSmall = null;

    private GPSManager gpsManager = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity2);

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

        gpsManager = new GPSManager();
        gpsManager.attach(this);
        gpsManager.startListening(getApplicationContext());
        gpsManager.setGPSCallback(this);

        if (gpsManager.isGPSenabled()) {
            enableGPS.setVisibility(View.GONE);
            startButton.setVisibility(View.VISIBLE);
        } else {
            enableGPS.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.GONE);
        }
    }

    public void update(Object context) {
        if (context instanceof Location) {
            onGPSUpdate((Location) context);
        }
    }

    public void onGPSUpdate(Location location)  {
        setSpeedText(R.id.infoMessage, getString(R.string.gpsReady));
    }

    @Override
    protected void onDestroy() {
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);
        gpsManager = null;

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start_activity2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;

        switch(item.getItemId()) {
            case R.id.menu_about: {
                displayAboutDialog();
                break;
            }
            case R.id.unit_kmh: {
                AppSettings.getInstance().setMeasureUnit(this, Constants.INDEX_KMH);
                break;
            }
            case R.id.unit_mk: {
                AppSettings.getInstance().setMeasureUnit(this, Constants.INDEX_MIN_KM);
                break;
            }
            default: {
                result = super.onOptionsItemSelected(item);
                break;
            }
        }

        return result;
    }

    /** Called when the user clicks the Start button */
    public void startRunning(View view) {
        Intent intent = new Intent(this, RunningActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Enable GPS button */
    public void enableGPS(View view) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_CODE);
        //Intent intent = new Intent(this, RunningActivity.class);
        //startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE) {
            if (gpsManager.isGPSenabled()) {
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
        Spannable span = new SpannableString(text);
        int firstPos = text.indexOf(32);

        span.setSpan(sizeSpanLarge, 0, firstPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(sizeSpanSmall, firstPos + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tv = ((TextView)findViewById(textid));

        tv.setText(span);
    }
}
