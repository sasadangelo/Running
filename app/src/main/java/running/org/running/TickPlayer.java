/*
 * Copyright: 2008 Akshat Aranya
 *
 *    This file is part of Metronome.
 *
 * Metronome is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Metronome is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Metronome.  If not, see <http://www.gnu.org/licenses/>.
 */

package running.org.running;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
/*
public class TickPlayer implements MediaPlayer.OnCompletionListener {
	
	MediaPlayer tick;
	MediaPlayer tock;
	boolean mRunning = false;
	int mCount = 0;
	int mPeriod = 1;
	private long mTickDuration;
	private long mStopTimeInFuture;
	
	public TickPlayer(Context ctx) {
		
		tick = MediaPlayer.create(ctx, R.raw.noises_tick);
		tock = MediaPlayer.create(ctx, R.raw.noises_tock);
		
		
		tick.setOnCompletionListener(this);
		tock.setOnCompletionListener(this);
			
		tick.setLooping(false);
		tock.setLooping(false);
	}
	
	public void onStart(int period, int ticksPerSec) {
		mRunning = true;
		mPeriod = (period == 0) ? 1 : period;
		mCount = 0;
		
		mTickDuration = 60000 / ticksPerSec;
		run();
		Log.i("metronome", "tid: " + Thread.currentThread().getId() + " onStart");
		
	}
	
	private  void run() {
		Log.i("metronome", "tid: " + Thread.currentThread().getId() + " run");
		
		if (!mRunning)
			return;
		
		mStopTimeInFuture = SystemClock.elapsedRealtime() + mTickDuration;
		
		if (++mCount % mPeriod == 0) {
			mCount = 0;
		
			tock.start();
		} else {
			
			tick.start();
		}
	}
	
	public void onStop() {
		mRunning = false;
		mHandler.sendMessage(mHandler.obtainMessage(MSG));
		
	}
	
	public void onDestroy() {
		if (tick != null) {
			tick.release();
			tick = null;
		}
		
		if (tock != null) {
			tock.release();
			tock = null;
		}
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		
		
		//mp.seekTo(0);
		
		long delay = mStopTimeInFuture - SystemClock.elapsedRealtime();
		Log.i("metronome", "tid: " + Thread.currentThread().getId() + "delay = " + delay);
		while (delay < 0)
			delay += mTickDuration;
		
		
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), delay);
		
	}
	
	private static final int MSG = 1;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			run();
		}
	};

}
*/
public class TickPlayer  {
	
	MediaPlayerPool tickPool;
	MediaPlayerPool tockPool;
	boolean mRunning = false;
	int mCount = 0;
	int mPeriod = 1;
	private long mTickDuration;
	//private long mStopTimeInFuture;
	
	public TickPlayer(Context ctx) {
		tickPool = new MediaPlayerPool(ctx, 10, R.raw.tock);
		tockPool = new MediaPlayerPool(ctx, 10, R.raw.tick);
	}
	
	public void onStart(int period, int ticksPerSec) {
		mRunning = true;
		mPeriod = (period == 0) ? 1 : period;
		mCount = 0;
		
		mTickDuration = 60000 / ticksPerSec;
		run();
		//Log.i("metronome", "tid: " + Thread.currentThread().getId() + " onStart");
	}
	
	private  void run() {
		//Log.i("metronome", "tid: " + Thread.currentThread().getId() + " run");
		if (!mRunning)
			return;
		
		//mStopTimeInFuture = SystemClock.elapsedRealtime() + mTickDuration;
		
		if ((mPeriod != 1) && (++mCount % mPeriod == 0)) {
			mCount = 0;
			tockPool.start();
		} else {
			tickPool.start();
		}
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mTickDuration);
	}
	
	public void onStop() {
		mRunning = false;
		mCount = 0;
		tickPool.stop();
		tockPool.stop();
		mHandler.removeMessages(MSG);
		//mHandler.sendMessage(mHandler.obtainMessage(MSG));
	}
	
	public void onDestroy() {
		onStop();
		tickPool.onDestroy();
		tockPool.onDestroy();
	}

	private static final int MSG = 1;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			run();
		}
	};
}

