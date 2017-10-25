package qqkj.qqkj_library.sound.vibrate;

import android.content.Context;
import android.os.Vibrator;

/**
 * 这个类是用来做甚的
 * <p> 打开震动
 * Created by 陈二狗 on 2017/10/25.
 */

public class VibrateUtil {

    private static VibrateUtil _vibrate_util = null;

    private Vibrator _vibrator = null;

    public static VibrateUtil getIns(){

        if(_vibrate_util == null){

            _vibrate_util = new VibrateUtil();
        }

        return _vibrate_util;
    }


    /**
     * 初始化震动 Application
     * @param _context
     */
    public void _init_vibarate(Context _context){

        _vibrator = (Vibrator) _context.getSystemService(Context.VIBRATOR_SERVICE);
    }


    /**
     * 播放声音
     * @param _vibrate_time
     */
    public void _get_vibrate(long _vibrate_time){

        if(null == _vibrator){

            return;
        }

        _vibrator.vibrate(_vibrate_time);
    }
}
