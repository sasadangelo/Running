package running.org.running;

/**
 * Created by sasadangelo on 03/11/2015.
 */
public class Synthesizer {

        private final short silenceDuration = 200;
        private final short noteDuration = 2400;
        private AudioGenerator audio = new AudioGenerator(8000);

        public enum Note0 {

            C(16.35),
            Csharp(17.32),
            D(18.35),
            Dsharp(19.45),
            E(20.60),
            F(21.83),
            Fsharp(23.12),
            G(24.50),
            Gsharp(25.96),
            A(27.50),
            Asharp(29.14),
            B(30.87);

            private final double frequency;

            Note0(double frequency) {
                this.frequency = frequency;
            }

            public double getFrequency() {
                return frequency;
            }
        }

        Synthesizer() {
            audio.createPlayer();
        }

        public void play(Note0 note,int octave,double duration) {
            audio.writeSound(audio.getSineWave((int) (noteDuration*duration*4), 8000, note.getFrequency()*Math.pow(2, octave)));
            audio.writeSound(audio.getSineWave(silenceDuration, 8000, 0));
        }

        public void stop() {
            audio.destroyAudioTrack();
        }

    }
