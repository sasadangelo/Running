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

