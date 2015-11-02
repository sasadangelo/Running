package running.org.running;

/**
 * Created by sasadangelo on 01/10/2015.
 */
public class MetronomeConfiguration {
    public MetronomeConfiguration(int stepsByMinute, long intervalDuration/*, int stepsByMinute2, long intervalDuration2, int repeat*/) {
        this.stepsByMinute = stepsByMinute;
        this.intervalDuration = intervalDuration;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getStepsByMinute() {
        return stepsByMinute;
    }

    public long getIntervalDuration() {
        return intervalDuration;
    }

    private boolean enabled;
    private int stepsByMinute;
    private long intervalDuration;
}
