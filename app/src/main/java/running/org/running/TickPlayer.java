package running.org.running;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class TickPlayer  {
	MediaPlayerPool tickPool;
	boolean metronomeRunning = false;
	private long mTickDuration;

	public TickPlayer(Context ctx) {
		tickPool = new MediaPlayerPool(ctx, 10, R.raw.tock);
	}
	
	public void onStart(int ticksPerSec) {
		metronomeRunning = true;
		mTickDuration = 60000 / ticksPerSec;
		run();
	}
	
	private  void run() {
		if (!metronomeRunning)
			return;
		tickPool.start();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mTickDuration);
	}
	
	public void onStop() {
		metronomeRunning = false;
		tickPool.stop();
		mHandler.removeMessages(MSG);
	}
	
	public void onDestroy() {
		onStop();
		tickPool.onDestroy();
	}

	private static final int MSG = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			run();
		}
	};
}
