package qqkj.qqkj_library.sensor.light;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

/**
 * 光感器
 * @author chen2gou
 */

public class LightUtil {

    private Context _context = null;

    private static LightUtil _light_util = null;

    private SensorManager _manager = null;

    private SensorEventListener _sensor_listener = null;

    private Sensor _light = null;


    public LightUtil(Context _context) {

        this._context = _context;
    }


    public static LightUtil getIns(Context _context) {

        if (_light_util == null) {

            _light_util = new LightUtil(_context);
        }

        return _light_util;
    }


    public static LightUtil getNew(Context _context) {

        return new LightUtil(_context);
    }

    /**
     * 启动光线传感器
     *
     * @return
     */
    public boolean _get_light(final LightListener _light_listener) {

        if (null == _manager) {

            _manager = ((SensorManager) _context.getSystemService(Context.SENSOR_SERVICE));

            if (null == _manager) {

                return false;
            }
        }

        if (null == _light) {

            _light = _manager.getDefaultSensor(Sensor.TYPE_LIGHT);

            if (null == _light) {

                return false;
            }
        }

        _sensor_listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent _event) {

                //返回单位 (lux) 勒克斯
                float _lux = (int)_event.values[0];

                if(_lux < 15.0f){

                    _light_listener.get_light(false);
                }else{

                    _light_listener.get_light(true);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        _manager.registerListener(_sensor_listener, _light, SensorManager.SENSOR_DELAY_UI);

        return true;
    }


    /**
     * 回收光感传感器
     */
    public void _destroy_light() {

        if (null != _manager && null != _sensor_listener) {

            _manager.unregisterListener(_sensor_listener);

            _light = null;

            _manager = null;
        }
    }


    /**
     * 光感回调函数
     */
    public interface LightListener{

        public void get_light(boolean _light);
    }
}
