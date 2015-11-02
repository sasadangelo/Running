package running.org.running;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;

public class MetronomePlayer {
	private SoundPool tickPool;
	private int soundID;
	boolean loaded = false;
	private float volume;
	private boolean metronomeRunning = false;
	private PowerManager.WakeLock mWakeLock;
	private MetronomeConfiguration configuration;
	private long tickInterval;
    private long tickDuration;
    private long startTime;

	public MetronomePlayer(Context ctx) {
		PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MetronomeLock");

        AudioManager audioManager = (AudioManager) ctx.getSystemService(ctx.AUDIO_SERVICE);
		volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        startTime = SystemClock.uptimeMillis();

		tickPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		tickPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loaded = true;
			}
		});
		soundID = tickPool.load(ctx, R.raw.tock, 1);
	}
	
	public void start(MetronomeConfiguration conf) {
		metronomeRunning = true;
		mWakeLock.acquire();
		configuration = conf;
        tickInterval = 60000 / configuration.getStepsByMinute();
        tickDuration = configuration.getDuration() == 0 ? Long.MAX_VALUE : configuration.getDuration();
		run();
	}

	private  void run() {
		if (!metronomeRunning)
			return;
		if (loaded) {
			tickPool.play(soundID, volume, volume, 1, 0, 1f);
            if (tickDuration == Long.MAX_VALUE || SystemClock.uptimeMillis() < (startTime + tickDuration * 1000)) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), tickInterval);
            } else {
                stop();
            }
		} else {
            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), tickInterval);
        }
	}
	
	public void stop() {
		metronomeRunning = false;
		tickPool.stop(soundID);
		mHandler.removeMessages(MSG);
		if (mWakeLock != null) {
			try {
				mWakeLock.release();
			} catch (Throwable th) {
				// ignoring this exception, probably wakeLock was already released
			}
		}
	}

	public void onDestroy() {
		stop();
	}

	private static final int MSG = 1;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			run();
		}
	};
}
