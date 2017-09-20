package qqkj.qqkj_library.sensor.shake;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

/**
 * Created by chen2gou on 2017/8/17.
 */

public class ShakeUtil {

    private Context _context = null;

    public static final String SHAKE_BR_NAME = "_shake_manager";

    public static final String SHAKE_BR_PARAM = "_shake";

    private Intent _intent = new Intent(SHAKE_BR_NAME);

    private static ShakeUtil _shake = null;

    private SensorManager _manager = null;

    private SensorEventListener _sensor_listener = null;

    private boolean _shake_state = false;

    private int _sensor_value = 16;

    private Sensor _accelerometer = null;

    private Handler _time_handler = new Handler();


    public ShakeUtil(Context _context) {

        this._context = _context;
    }


    public static ShakeUtil getIns(Context _context) {

        if (_shake == null) {

            _shake = new ShakeUtil(_context);
        }

        return _shake;
    }


    public static ShakeUtil getNew(Context _context) {

        return new ShakeUtil(_context);
    }

    /**
     * 设置摇一摇基数 默认16
     *
     * @param _sensor_value
     */
    public void _set_sensor_value(int _sensor_value) {

        this._sensor_value = _sensor_value;
    }

    /**
     * 启动摇一摇传感器
     *
     * @return
     */
    public boolean _get_shake(final ShakeListener _shake_listener) {

        if (null == _manager) {

            _manager = ((SensorManager) _context.getSystemService(_context.SENSOR_SERVICE));

            if (null == _manager) {

                return false;
            }
        }

        if (null == _accelerometer) {

            _accelerometer = _manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (null == _accelerometer) {

                return false;
            }
        }

        _sensor_listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent _event) {

                int _type = _event.sensor.getType();

                if (_type == Sensor.TYPE_ACCELEROMETER) {

                    //获取三个方向值
                    float[] values = _event.values;

                    float x = values[0];
                    float y = values[1];
                    float z = values[2];

                    if ((Math.abs(x) > _sensor_value || Math.abs(y) > _sensor_value || Math
                            .abs(z) > _sensor_value) && !_shake_state) {

                        _shake_state = true;
                        _intent.putExtra(SHAKE_BR_PARAM, 1);
                        _context.sendBroadcast(_intent);

                        _shake_listener.get_shake(1);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        _manager.registerListener(_sensor_listener, _accelerometer, SensorManager.SENSOR_DELAY_UI);

        return true;
    }

    /**
     * 重置摇一摇
     * @param _shake_time   摇一摇间隔时间
     */
    public void _reset_shake(int _shake_time) {

        _time_handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                _shake_state = false;
            }
        },_shake_time);

    }


    /**
     * 回收摇一摇传感器
     */
    public void _destroy_shake() {

        if (null != _manager && null != _sensor_listener) {

            _manager.unregisterListener(_sensor_listener);

            _accelerometer = null;

            _manager = null;
        }
    }


    /**
     * 摇一摇回调函数
     */
    public interface ShakeListener{

        public void get_shake(int _shake);
    }
}
