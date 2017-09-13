package qqkj.qqkj_library.sensor.compass;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 这个类是用来做甚的
 * 指南针工具类
 * <p>
 * Created by 陈二狗 on 2017/9/6.
 */

public class CompassUtil {

    private static CompassUtil _compass_util;

    public static final String COMPASS_BR_NAME = "_compass_manager";

    public static final String COMPASS_BR_PARAM = "_compass";

    public static final String COMPASS_ACCURACY_CHANGE_BR_NAME = "_compass_accuracy_change_manager";

    public static final String COMPASS_ACCURACY_CHANGE_BR_PARAM = "_compass_accuracy";

    private Context _context;

    private Intent _intent = new Intent(COMPASS_BR_NAME);

    private Intent _intent_accuracy = new Intent(COMPASS_ACCURACY_CHANGE_BR_NAME);

    private SensorManager _sensor_manager = null;

    private Sensor _accelerometer_sensor = null;

    private Sensor _magnetic_sensor = null;

    private float[] _acceleration_values = new float[3];

    private float[] _magnetic_values = new float[3];

    private float[] _values = new float[3];

    private float[] _r = new float[9];

    private SensorEventListener _sensor_listener = null;


    /**
     * 构造方法
     *
     * @param _context
     */
    private CompassUtil(Context _context) {

        this._context = _context;
    }


    /**
     * 单例
     *
     * @param _context
     * @return
     */
    public static CompassUtil getIns(Context _context) {

        if (null == _compass_util) {

            _compass_util = new CompassUtil(_context);
        }

        return _compass_util;
    }


    public static CompassUtil getNew(Context _context) {

        return new CompassUtil(_context);
    }


    /**
     * 获取当前手机指南针数据
     *
     * @return
     */
    public boolean _get_compass() {

        if (null == _sensor_manager) {

            _sensor_manager = (SensorManager) _context.getSystemService(Context.SENSOR_SERVICE);

            if (null == _sensor_manager) {

                return false;
            }
        }

        if (null == _accelerometer_sensor) {

            _accelerometer_sensor = _sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (null == _accelerometer_sensor) {

                return false;
            }
        }

        if (null == _magnetic_sensor) {

            _magnetic_sensor = _sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            if (null == _magnetic_sensor) {

                return false;
            }
        }

        _sensor_listener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent _event) {

                if (_event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    _acceleration_values = _event.values;
                }

                if (_event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                    _magnetic_values = _event.values;
                }

                //获取转换后的矩阵
                SensorManager.getRotationMatrix(_r, null, _acceleration_values, _magnetic_values);

                //转换为弧度值
                SensorManager.getOrientation(_r, _values);

                //弧度转成角度值
                _values[0] = (float) Math.toDegrees(_values[0]);

                int _result_value = (int) _values[0];

                if (_result_value < 0) {

                    _result_value = 360 + _result_value;
                }

                _intent.putExtra(COMPASS_BR_PARAM, String.valueOf(_result_value));

                _context.sendBroadcast(_intent);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

                if (accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) {

                    _intent_accuracy.putExtra(COMPASS_ACCURACY_CHANGE_BR_PARAM, true);

                    _context.sendBroadcast(_intent_accuracy);
                }
            }
        };

        _sensor_manager.registerListener(_sensor_listener, _accelerometer_sensor, SensorManager.SENSOR_DELAY_UI);

        _sensor_manager.registerListener(_sensor_listener, _magnetic_sensor, SensorManager.SENSOR_DELAY_UI);

        return true;
    }


    /**
     * 回收指南针传感器
     */
    public void _destroy_compass() {

        if (null != _sensor_manager && null != _accelerometer_sensor && null != _magnetic_sensor) {

            _sensor_manager.unregisterListener(_sensor_listener);

            _accelerometer_sensor = null;

            _magnetic_sensor = null;

            _sensor_manager = null;
        }
    }
}
