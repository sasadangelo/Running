package running.org.running;

/**
 * Created by sasadangelo on 01/10/2015.
 */
public class MetronomeConfiguration {
    public MetronomeConfiguration(int stepsByMinute, long duration/*, int stepsByMinute2, long intervalDuration2, int repeat*/) {
        this.stepsByMinute = stepsByMinute;
        this.duration = duration;
    }

    public int getStepsByMinute() {
        return stepsByMinute;
    }

    public long getDuration() {
        return duration;
    }

    private int stepsByMinute;
    private long duration;
}
