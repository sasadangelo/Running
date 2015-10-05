package running.org.running;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

public class TickPlayer  {
	private MediaPlayerPool tickPool;
	private boolean metronomeRunning = false;
	private long tickDuration;
	PowerManager.WakeLock mWakeLock;

	public TickPlayer(Context ctx) {
		PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MetronomeLock");
		tickPool = new MediaPlayerPool(ctx, 10, R.raw.tock);
	}
	
	public void start(MetronomeConfiguration configuration) {
		metronomeRunning = true;
		mWakeLock.acquire();
		tickDuration = 60000 / configuration.getStepsByMinute();
		run();
	}

	private  void run() {
		if (!metronomeRunning)
			return;
		tickPool.play();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), tickDuration);
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
