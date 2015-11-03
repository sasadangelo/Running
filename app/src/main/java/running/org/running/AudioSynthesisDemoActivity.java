package running.org.running;

import running.org.running.Synthesizer.Note0;
import android.app.Activity;
import android.os.Bundle;
/**
 * Created by sasadangelo on 03/11/2015.
 */
    public class AudioSynthesisDemoActivity extends Activity {
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start_activity);

            Synthesizer synthesizer = new Synthesizer();

            synthesizer.play(Note0.C, 5, 1.0/8);
            synthesizer.play(Note0.E, 5, 1.0/8);

            synthesizer.play(Note0.F, 5, 1.0/4);
            synthesizer.play(Note0.A, 5, 1.0/4);
            synthesizer.play(Note0.A, 5, 3.0/8);
            synthesizer.play(Note0.B, 5, 1.0/8);

            synthesizer.play(Note0.A, 5, 1.0/4);
            synthesizer.play(Note0.F, 5, 1.0/4);
            synthesizer.play(Note0.C, 5, 3.0/8);
            synthesizer.play(Note0.E, 5, 1.0/8);

            synthesizer.play(Note0.F, 5, 1.0/4);
            synthesizer.play(Note0.F, 5, 1.0/4);
            synthesizer.play(Note0.E, 5, 1.0/4);
            synthesizer.play(Note0.C, 5, 1.0/4);

            synthesizer.play(Note0.E, 5, 3.0/4);
            synthesizer.play(Note0.C, 5, 1.0/8);
            synthesizer.play(Note0.E, 5, 1.0/8);

            synthesizer.play(Note0.F, 5, 1.0/4);
            synthesizer.play(Note0.A, 5, 1.0/4);
            synthesizer.play(Note0.A, 5, 3.0/8);
            synthesizer.play(Note0.B, 5, 1.0/8);

            synthesizer.play(Note0.A, 5, 1.0/4);
            synthesizer.play(Note0.F, 5, 1.0/4);
            synthesizer.play(Note0.C, 5, 3.0/8);
            synthesizer.play(Note0.E, 5, 1.0/8);

            synthesizer.play(Note0.F, 5, 1.0/4);
            synthesizer.play(Note0.F, 5, 1.0/4);
            synthesizer.play(Note0.E, 5, 1.0/4);
            synthesizer.play(Note0.C, 5, 1.0/4);

            synthesizer.play(Note0.C, 5, 1);

            synthesizer.stop();
        }
    }
