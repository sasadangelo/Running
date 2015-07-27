package running.org.running;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import running.org.running.RunningActivity;

public class StartActivity2 extends Activity {
    private Button startButton;

    private TextView timerValue;
    private TextView speedValue;
    private TextView averageSpeedValue;
    private TextView distanceValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity2);

        startButton = (Button) findViewById(R.id.startButton);
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

    /** Called when the user clicks the Send button */
    public void startRunning(View view) {
        Intent intent = new Intent(this, RunningActivity.class);
        startActivity(intent);
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
    }}
