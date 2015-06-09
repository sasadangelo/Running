package running.org.running;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import running.org.running.StartActivity2;

public class RunningActivity extends Activity {
    public final static String TIME_MESSAGE = "running.org.running.MESSAGE";

    private Button stopButton;
    private Button pauseButton;
    private Button resumeButton;

    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        timerValue = (TextView) findViewById(R.id.timerValue);
        stopButton = (Button) findViewById(R.id.stopButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        resumeButton = (Button) findViewById(R.id.resumeButton);

        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                customHandler.removeCallbacks(updateTimerThread);
                Intent intent = new Intent(RunningActivity.this, StartActivity2.class);
                intent.putExtra(TIME_MESSAGE, timerValue.getText().toString());
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
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            if (hours > 0) {
                timerValue.setText(String.format("%02d", hours) + ":"
                        + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));
                customHandler.postDelayed(this, 0);
            } else {
                timerValue.setText(String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));
                customHandler.postDelayed(this, 0);
            }
        }
    };
}
