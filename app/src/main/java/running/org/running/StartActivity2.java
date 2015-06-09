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

    //private TextView timerValue;
    //private long startTime = 0L;
    //private Handler customHandler = new Handler();
    //long timeInMilliseconds = 0L;
    //long timeSwapBuff = 0L;
    //long updatedTime = 0L;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity2);

        startButton = (Button) findViewById(R.id.startButton);
        timerValue = (TextView) findViewById(R.id.timerValue);

        Intent intent = getIntent();
        String message = intent.getStringExtra(RunningActivity.TIME_MESSAGE);
        timerValue.setText(message);
        //startButton.setOnClickListener(new View.OnClickListener() {
    //        public void onClick(View view) {
    //      }
    //  });
    }

    /** Called when the user clicks the Send button */
    public void startRunning(View view) {
        Intent intent = new Intent(this, RunningActivity.class);
        startActivity(intent);
    }
}
