package running.org.running;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class TickPlayer  {
	MediaPlayerPool tickPool;
	//MediaPlayerPool tockPool;
	boolean metronomeRunning = false;
	//int mCount = 0;
	//int mPeriod = 1;
	private long mTickDuration;

	public TickPlayer(Context ctx) {
		tickPool = new MediaPlayerPool(ctx, 10, R.raw.tock);
		//tockPool = new MediaPlayerPool(ctx, 10, R.raw.tick);
	}
	
	public void onStart(/*int period,*/ int ticksPerSec) {
		metronomeRunning = true;
		//mPeriod = (period == 0) ? 1 : period;
		//mCount = 0;
		
		mTickDuration = 60000 / ticksPerSec;
		run();
	}
	
	private  void run() {
		if (!metronomeRunning)
			return;

		//if ((mPeriod != 1) && (++mCount % mPeriod == 0)) {
		//	mCount = 0;
		//	tockPool.start();
		//} else {
		//	tickPool.start();
		//}
		tickPool.start();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mTickDuration);
	}
	
	public void onStop() {
		metronomeRunning = false;
		//mCount = 0;
		tickPool.stop();
		//tockPool.stop();
		mHandler.removeMessages(MSG);
	}
	
	public void onDestroy() {
		onStop();
		tickPool.onDestroy();
		//tockPool.onDestroy();
	}

	private static final int MSG = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			run();
		}
	};
}

