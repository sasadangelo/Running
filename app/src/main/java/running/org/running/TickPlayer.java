package running.org.running;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

public class TickPlayer  {
	private MediaPlayerPool tickPool;
	private boolean metronomeRunning = false;
	private PowerManager.WakeLock mWakeLock;
	private MetronomeConfiguration configuration;
	private long tickDuration;
	private long numTicks;

	public TickPlayer(Context ctx) {
		PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MetronomeLock");
		tickPool = new MediaPlayerPool(ctx, 10, R.raw.tock);
	}
	
	public void start(MetronomeConfiguration conf) {
		metronomeRunning = true;
		mWakeLock.acquire();
		configuration = conf;
		tickDuration = 60000 / configuration.getStepsByMinute();
		if (!configuration.isContinue()) {
			numTicks = Long.MAX_VALUE;
		} else {
			numTicks = configuration.getStepsByMinute() * configuration.getIntervalDuration();
		}
		run();
	}

	private  void run() {
		if (!metronomeRunning)
			return;
		tickPool.play();
		if (numTicks < Long.MAX_VALUE && numTicks > 0) numTicks--;
		if (numTicks > 0) {
			mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), tickDuration);
		} else {
            stop();
		}
	}
	
	public void stop() {
		metronomeRunning = false;
		tickPool.stop();
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
		tickPool.onDestroy();
	}

	private static final int MSG = 1;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			run();
		}
	};
}
