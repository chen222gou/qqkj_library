package qqkj.qqkj_library.sensor.temperature;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;

/**
 * 这个类是用来做甚的
 * 获取温度传感器
 * <p>
 * Created by 陈二狗 on 2017/9/6.
 */

public class TemperatureUtil {

    private static TemperatureUtil _temperature = null;

    public static final String TEMPERATURE_BR_NAME = "_temperature_manager";

    public static final String TEMPERATURE_BR_PARAM = "_temperature";

    private Intent _intent = new Intent(TEMPERATURE_BR_NAME);

    private SensorManager _sensor_manager = null;

    private Sensor _temperature_sensor = null;

    private Context _context;

    private SensorEventListener _sensor_listener = null;


    /**
     * 构造函数
     *
     * @param _context
     */
    private TemperatureUtil(Context _context) {

        this._context = _context;
    }


    /**
     * 单例
     *
     * @param _context
     */
    public static TemperatureUtil getIns(Context _context) {

        if (null == _temperature) {

            _temperature = new TemperatureUtil(_context);
        }

        return _temperature;
    }


    public static TemperatureUtil getNew(Context _context){

        return new TemperatureUtil(_context);
    }


    /**
     * 获取温度传感器
     *
     * @return
     */
    public boolean _get_temperature() {

        if (null == _sensor_manager) {

            _sensor_manager = (SensorManager) _context.getSystemService(Context.SENSOR_SERVICE);

            if (null == _sensor_manager) {

                return false;
            }
        }

        if (null == _temperature_sensor) {

            _temperature_sensor = _sensor_manager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

            if (null == _temperature_sensor) {

                _temperature_sensor = _sensor_manager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);

                if (null == _temperature_sensor) {

                    return false;
                }
            }
        }

        _sensor_listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                int _value = (int)sensorEvent.values[0];

                _intent.putExtra(TEMPERATURE_BR_PARAM, String.valueOf(_value));
                _context.sendBroadcast(_intent);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        _sensor_manager.registerListener(_sensor_listener, _temperature_sensor, SensorManager.SENSOR_DELAY_UI);

        return true;
    }


    /**
     * 回收温度传感器
     */
    public void _destroy_temperature() {

        if (null != _sensor_manager && null != _temperature_sensor) {

            _sensor_manager.unregisterListener(_sensor_listener);

            _temperature_sensor = null;

            _sensor_manager = null;
        }
    }
}
