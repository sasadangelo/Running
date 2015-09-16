package running.org.running;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

public class TickPlayer  {
	MediaPlayerPool tickPool;
	boolean metronomeRunning = false;
	private long mTickDuration;
	PowerManager.WakeLock mWakeLock;

	public TickPlayer(Context ctx) {
		PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MetronomeLock");
		tickPool = new MediaPlayerPool(ctx, 10, R.raw.tock);
	}
	
	public void start(int ticksPerSec) {
		metronomeRunning = true;
		mTickDuration = 60000 / ticksPerSec;
		mWakeLock.acquire();
		run();
	}
	
	private  void run() {
		if (!metronomeRunning)
			return;
		tickPool.play();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mTickDuration);
	}
	
	public void stop() {
		metronomeRunning = false;
		tickPool.stop();
		mHandler.removeMessages(MSG);
		mWakeLock.release();
	}

	public void pause() {
		metronomeRunning = false;
		tickPool.pause();
		//mHandler.removeMessages(MSG);
		//mWakeLock.release();
	}

	public void resume() {
		metronomeRunning = true;
		tickPool.resume();
		//mHandler.removeMessages(MSG);
		//mWakeLock.release();
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
