package running.org.running;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import running.org.running.StartActivity2;
import android.location.Location;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RunningActivity extends Activity implements GPSCallback {
    public final static String TIME_MESSAGE = "running.org.running.TIME_MESSAGE";
    public final static String SPEED_MESSAGE = "running.org.running.SPEED_MESSAGE";
    public final static String AVERAGE_SPEED_MESSAGE = "running.org.running.AVERAGE_SPEED_MESSAGE";
    public final static String DISTANCE_MESSAGE = "running.org.running.DISTANCE_MESSAGE";

    private Button stopButton;
    private Button pauseButton;
    private Button resumeButton;

    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    private GPSManager gpsManager = null;
    private AbsoluteSizeSpan sizeSpanLarge = null;
    private AbsoluteSizeSpan sizeSpanSmall = null;
    private Location oldLocation = null;
    private double totalDistance = 0.0D;
    private long totalTime = 0L;

    private boolean mBug23937Checked = false;
    private long mBug23937Delta = 0;

    private TextView speedValue;
    private TextView averageSpeedValue;
    private TextView distanceValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        timerValue = (TextView) findViewById(R.id.timerValue);
        speedValue = (TextView) findViewById(R.id.speedValue);
        averageSpeedValue = (TextView) findViewById(R.id.averageSpeedValue);
        distanceValue = (TextView) findViewById(R.id.distanceValue);

        String unitString = measurementUnitString(AppSettings.getInstance().getMeasureUnit(this));
        String speedPeaceString = speedPeaceString(AppSettings.getInstance().getMeasureUnit(this));
        String averageSpeedPeaceString = averageSpeedPeaceString(AppSettings.getInstance().getMeasureUnit(this));

        timerValue.setText(getString(R.string.time) + ": " + "00:00");
        speedValue.setText(speedPeaceString + ": " + "00:00 " + unitString);
        averageSpeedValue.setText(averageSpeedPeaceString + ": " + "00:00 " + unitString);
        distanceValue.setText(getString(R.string.distance) + ": " + "00:00 " + getString(R.string.unit_km));

        stopButton = (Button) findViewById(R.id.stopButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        resumeButton = (Button) findViewById(R.id.resumeButton);

        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                customHandler.removeCallbacks(updateTimerThread);
                Intent intent = new Intent(RunningActivity.this, StartActivity2.class);
                intent.putExtra(TIME_MESSAGE, timerValue.getText().toString());
                intent.putExtra(SPEED_MESSAGE, speedValue.getText().toString());
                intent.putExtra(AVERAGE_SPEED_MESSAGE, averageSpeedValue.getText().toString());
                intent.putExtra(DISTANCE_MESSAGE, distanceValue.getText().toString());
                startActivity(intent);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
                pauseButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.VISIBLE);
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                resumeButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
            }
        });

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        resumeButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);

        gpsManager = new GPSManager();
        gpsManager.startListening(getApplicationContext());
        gpsManager.setGPSCallback(this);
    }

    @Override
    protected void onDestroy() {
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);
        gpsManager = null;

        super.onDestroy();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            if (hours > 0) {
                timerValue.setText(getString(R.string.time) + ": " + String.format("%02d", hours) + ":"
                        + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));
                customHandler.postDelayed(this, 0);
            } else {
                timerValue.setText(getString(R.string.time) + ": " + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));
                customHandler.postDelayed(this, 0);
            }
        }
    };

    @Override
    public void onGPSUpdate(Location location)  {
        long now = System.currentTimeMillis();

        if (!mBug23937Checked) {
            long gpsTime = location.getTime();
            if (gpsTime > now + 3 * 1000) {
                mBug23937Delta = now - gpsTime;
            } else {
                mBug23937Delta = 0;
            }
            mBug23937Checked = true;
        }
        if (mBug23937Delta != 0) {
            location.setTime(location.getTime() + mBug23937Delta);
        }

        double speed = 0.00D;
        double averageSpeed = 0.00D;
        if (oldLocation != null) {
            double distanceDiff = location.distanceTo(oldLocation)/1000;
            long timeDiff = location.getTime()-oldLocation.getTime();
            if (timeDiff < 0) {
                // TODO investigate if this is known...only seems to happen
                // in emulator
                timeDiff = 0;
            }
            totalTime += timeDiff; //location.getTime()-firstLocation.getTime();
            totalDistance += distanceDiff;
            speed = timeDiff==0 ? 0 : distanceDiff*3600000/timeDiff;
            averageSpeed = totalTime==0 ? 0 : totalDistance*3600000/totalTime;
        }
        oldLocation = location;

        String speedString = "" + roundDecimal(convertSpeed(speed),2);
        String averageSpeedString = "" + roundDecimal(convertSpeed(averageSpeed),2);
        String totalDistanceString = "" + roundDecimal(totalDistance,2);
        String unitString = measurementUnitString(AppSettings.getInstance().getMeasureUnit(this));

        String speedPeaceString = speedPeaceString(AppSettings.getInstance().getMeasureUnit(this));
        String averageSpeedPeaceString = averageSpeedPeaceString(AppSettings.getInstance().getMeasureUnit(this));

        speedValue.setText(speedPeaceString + ": " + speedString + " " + unitString);
        averageSpeedValue.setText(averageSpeedPeaceString + ": " + averageSpeedString + " " + unitString);
        distanceValue.setText(getString(R.string.distance) + ": " + totalDistanceString + " " + getString(R.string.unit_km));

        setSpeedText(R.id.infoMessage, getString(R.string.gpsReady));
        setSpeedText(R.id.speedValue, speedValue.getText().toString());
        setSpeedText(R.id.averageSpeedValue, averageSpeedValue.getText().toString());
        setSpeedText(R.id.distanceValue, distanceValue.getText().toString());
    }

    private double roundDecimal(double value, final int decimalPlace) {
        BigDecimal bd = new BigDecimal(value);

        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        value = bd.doubleValue();

        return value;
    }

    private double convertSpeed(double speed) {
        switch(AppSettings.getInstance().getMeasureUnit(this)) {
            case Constants.INDEX_KMH:
                return speed;
            case Constants.INDEX_MIN_KM:
                return speed==0 ? 0 : 60/speed;
        }
        return speed;
    }

    private String measurementUnitString(int unitIndex) {
        String string = getString(R.string.unit_kmh);

        switch(unitIndex) {
            case Constants.INDEX_KMH:
                string = getString(R.string.unit_kmh);
                break;
            case Constants.INDEX_MIN_KM:
                string = getString(R.string.unit_minKm);
                break;
        }

        return string;
    }

    private String speedPeaceString(int unitIndex) {
        String string = getString(R.string.speed);

        switch(unitIndex) {
            case Constants.INDEX_KMH:
                string = getString(R.string.speed);
                break;
            case Constants.INDEX_MIN_KM:
                string = getString(R.string.peace);
                break;
        }

        return string;
    }

    private String averageSpeedPeaceString(int unitIndex) {
        String string = getString(R.string.averageSpeed);

        switch(unitIndex) {
            case Constants.INDEX_KMH:
                string = getString(R.string.averageSpeed);
                break;
            case Constants.INDEX_MIN_KM:
                string = getString(R.string.averagePeace);
                break;
        }

        return string;
    }

    private void setSpeedText(int textid, String text) {
        Spannable span = new SpannableString(text);
        int firstPos = text.indexOf(32);

        span.setSpan(sizeSpanLarge, 0, firstPos,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(sizeSpanSmall, firstPos + 1, text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tv = ((TextView)findViewById(textid));

        tv.setText(span);
    }
}
