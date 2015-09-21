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
import android.util.Log;

public final class MediaPlayerPool {
	private MediaPlayer mp[];
	private int mLast;
	private boolean paused = false;

	public MediaPlayerPool(Context ctx, int num, int resId) {
		mp = new MediaPlayer[num];
		for (int i = 0; i < num; i++) {
			mp[i] = MediaPlayer.create(ctx, resId);
			mp[i].setLooping(false);
			mp[i].setOnErrorListener(new MediaPlayer.OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					Log.v("MediaPlayerPool", "error on media player what=" + what + " extra=" + extra);
					return false;
				}
			});
		}
		mLast = -1;
	}
	
	public void play() {
		for (int i = mLast + 1; i < mp.length; i++) {
			if (!mp[i].isPlaying()) {
				mLast = i;
				mp[i].start();
				return;
			}
		}
		for (int i = 0; i <= mLast; i++) {
			if (!mp[i].isPlaying()) {
				mLast = i;
				mp[i].start();
				return;
			}
		}
	}

	public void stop() {
		for (int i = 0; i < mp.length; i++) {
			if (mp[i].isPlaying())
				mp[i].stop();
		}
	}
	
	public void onDestroy() {
		stop();
		for (int i = 0; i < mp.length; i++) {
			mp[i].release();
			mp[i] = null;
		}
	}
}
