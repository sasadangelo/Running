package running.org.running;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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

    /** Called when the user clicks the Send button */
    public void startRunning(View view) {
        Intent intent = new Intent(this, RunningActivity.class);
        startActivity(intent);
    }
}
