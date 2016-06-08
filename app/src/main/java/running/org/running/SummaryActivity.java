package running.org.running;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SummaryActivity extends ActionBarActivity {
    private static final String LOG_TAG = "Run4Fun.SummaryActivity";
    private TextView timeValue;
    private TextView averageSpeedValue;
    private TextView distanceValue;

    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate -- begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        doneButton = (Button) findViewById(R.id.startButton);

        distanceValue = (TextView) findViewById(R.id.distanceValue);
        timeValue = (TextView) findViewById(R.id.timeValue);
        averageSpeedValue = (TextView) findViewById(R.id.averageSpeedValue);

        Intent intent = getIntent();
        String timeMessage = intent.getStringExtra(RunningActivity.TIME_MESSAGE);
        String averageSpeedMessage = intent.getStringExtra(RunningActivity.AVERAGE_SPEED_MESSAGE);
        String distanceMessage = intent.getStringExtra(RunningActivity.DISTANCE_MESSAGE);

        distanceValue.setText(distanceMessage);
        timeValue.setText(timeMessage);
        averageSpeedValue.setText(averageSpeedMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Done button */
    public void doneSummary(View view) {
        Log.i(LOG_TAG, "doneSummary -- begin");
        Intent intent = new Intent(SummaryActivity.this, StartActivity.class);
        startActivity(intent);
    }
}
