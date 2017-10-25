package qqkj.qqkj_library.sound.soundpool;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

import qqkj.qqkj_library.threadpool.ThreadPoolUtil;

/**
 * 这个类是用来做甚的
 * <p> 播放短声音
 * Created by 陈二狗 on 2017/10/25.
 */

public class SoundPoolUtil {

    private static SoundPoolUtil _sound_util = null;

    private SoundPool _sound_pool = null;

    private Context _context;

    public Map<Integer, Integer> _sound_map = new HashMap<>();


    public static SoundPoolUtil getIns() {

        if (null == _sound_util) {

            _sound_util = new SoundPoolUtil();
        }

        return _sound_util;
    }


    /**
     * 第一次初始化声音集合 Application
     */
    public void _init_sound(final Context _context, final int[] _sounds) {

        if (null == _sounds || _sounds.length == 0) {

            return;
        }

        _sound_pool = new SoundPool(_sounds.length, AudioManager.STREAM_SYSTEM, 0);

        _sound_map.clear();

        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < _sounds.length; i++) {

                    _sound_map.put(i, _sound_pool.load(_context, _sounds[i], 1));
                }
            }
        });
    }


    /**
     * 播放声音
     *
     * @param _sound_map_index
     * @return
     */
    public boolean _get_sound(int _sound_map_index) {

        if (null == _sound_pool) {

            return false;
        }

        _sound_pool.play(_sound_map.get(_sound_map_index), (float) 1, (float) 1, 0, 0, (float) 1);

        return true;
    }
}
