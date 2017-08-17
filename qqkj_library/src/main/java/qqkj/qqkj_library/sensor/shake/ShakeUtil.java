package qqkj.qqkj_library.sensor.shake;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by chen2gou on 2017/8/17.
 */

public class ShakeUtil {

    private Context _context = null;

    private Intent _intent = new Intent("_shake_manager");

    private static ShakeUtil _shake = null;

    private SensorManager _manager = null;

    private SensorEventListener _sensor_listener = null;

    private boolean _shake_state = false;

    public ShakeUtil( Context _context ){

        this._context = _context;
    }


    public static ShakeUtil getIns( Context _context ){

        if( _shake==null ){

            _shake = new ShakeUtil( _context );
        }

        return _shake;
    }

    /**
     * 启动摇一摇传感器
     * @return
     */
    public boolean _get_shake(){

        _manager = ((SensorManager) _context.getSystemService(_context.SENSOR_SERVICE));

        if (_manager != null) {

            //获取加速度传感器
            Sensor _accelerometer = _manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (_accelerometer != null) {

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

                            if ((Math.abs(x) > 16 || Math.abs(y) > 16 || Math
                                    .abs(z) > 16) && !_shake_state) {

                                _shake_state = true;

                                _intent.putExtra("_shake",1);
                                _context.sendBroadcast(_intent);

                            }
                        }

                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                        System.out.println("sensor = [" + sensor + "], i = [" + i + "]");
                    }
                };

                _manager.registerListener(_sensor_listener, _accelerometer, SensorManager.SENSOR_DELAY_UI);
            }else{

                return false;
            }
        }else{

            return false;
        }

        return true;
    }

    /**
     * 重置摇一摇
     */
    public void _reset_shake(){

        this._shake_state = false;
    }


    /**
     *
     * 回收摇一摇传感器
     */
    public void _destroy_shake(){

        if( null!=_manager && null!=_sensor_listener ){

            _manager.unregisterListener(_sensor_listener);
        }
    }
}
