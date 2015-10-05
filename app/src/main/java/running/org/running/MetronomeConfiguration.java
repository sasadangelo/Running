package running.org.running;

/**
 * Created by sasadangelo on 01/10/2015.
 */
public class MetronomeConfiguration {
    public MetronomeConfiguration() {
    }

    public MetronomeConfiguration(int stepsByMinute) {
        this.continue_ = true;
        this.stepsByMinute = stepsByMinute;
    }

    public MetronomeConfiguration(int stepsByMinute, long intervalDuration, int stepsByMinute2, long intervalDuration2, int repeat) {
        this.continue_ = true;
        this.stepsByMinute = stepsByMinute;
        this.intervalDuration = intervalDuration;
        this.stepsByMinute2 = stepsByMinute2;
        this.intervalDuration2 = intervalDuration2;
        this.repeat = repeat;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isContinue() {
        return continue_;
    }

    public int getStepsByMinute() {
        return stepsByMinute;
    }

    public long getIntervalDuration() {
        return intervalDuration;
    }

    public int getStepsByMinute2() {
        return stepsByMinute2;
    }

    public long getIntervalDuration2() {
        return intervalDuration2;
    }

    public long getRepeat() {
        return repeat;
    }

    private boolean enabled;
    private boolean continue_;
    private int stepsByMinute;
    private long intervalDuration;
    private int stepsByMinute2;
    private long intervalDuration2;
    private int repeat;
}
