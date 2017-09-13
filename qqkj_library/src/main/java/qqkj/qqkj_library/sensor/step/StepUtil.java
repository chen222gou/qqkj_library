package qqkj.qqkj_library.sensor.step;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.provider.Settings;

/**
 * 获取当前设备的计步器步数
 * Created by chen2gou on 2017/8/17.
 */

public class StepUtil {

    private Context _context = null;

    private static StepUtil _step_util = null;

    public static final String STEP_BR_NAME = "_step_manager";

    public static final String STEP_BR_PARAM = "_step";

    private SensorManager _manager = null;

    private Intent _intent = new Intent(STEP_BR_NAME);

    private Sensor _sensor_step = null;

    private boolean _sensor_intent_more = false;    //是否开启 达到一定步数再发送广播,默认不开启,每一步都发送广播

    private int _step_sum = 0;  //间隔模式总步数

    private static Handler _handler = null;   //间隔模式消息机制

    private int _handler_time = 5000;   //间隔时间

    private long _system_time = 0l;

    /**
     * 默认模式构造函数
     *
     * @param _context
     */
    private StepUtil(Context _context) {

        this._context = _context;
        this._sensor_intent_more = false;
    }

    /**
     * 间隔模式构造函数
     *
     * @param _context
     */
    private StepUtil(Context _context, boolean _sensor_intent_more, int _handler_time) {

        this._context = _context;
        this._sensor_intent_more = _sensor_intent_more;
        this._handler_time = _handler_time;
    }

    /**
     * 默认模式单例
     *
     * @param _context
     */
    public static StepUtil getIns(Context _context) {

        if (_step_util == null) {

            _step_util = new StepUtil(_context);
        }

        return _step_util;
    }


    /**
     * 间隔模式单利
     *
     * @param _context
     */
    public static StepUtil getIns(Context _context, int _handler_time) {

        //实例化间隔消息
        _handler = new Handler();

        if (_step_util == null) {

            _step_util = new StepUtil(_context, true, _handler_time);
        }

        return _step_util;
    }


    public static StepUtil getNew(Context _context) {

        return new StepUtil(_context);
    }


    public static StepUtil getNew(Context _context, int _handler_time) {

        _handler = new Handler();

        return new StepUtil(_context, true, _handler_time);
    }


    /**
     * 启动计步器
     * <p>
     * 返回false,表示用户没有传感器,返回true,可以开始进行计步
     */

    public boolean _get_step() {

        //初始化计步器模块

        if (null == _manager) {

            _manager = (SensorManager) _context.getSystemService(Context.SENSOR_SERVICE);

            if (null == _manager) {

                return false;
            }
        }

        if (null == _sensor_step) {

            _sensor_step = _manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            if (null == _sensor_step) {

                //该设备无运动传感器,执行摇一摇计步

                _sensor_step = _manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                if (null == _sensor_step) {

                    return false;
                }
            }
        }

        //开启间隔模式

        if (null != _handler && _sensor_intent_more) {

            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    _send_data();
                }
            }, _handler_time);
        }

        if (_sensor_step.getType() == Sensor.TYPE_STEP_DETECTOR) {

            _manager.registerListener
                    (_sensor_listener_hardware, _sensor_step, Sensor.TYPE_STEP_DETECTOR, SensorManager.SENSOR_DELAY_UI);

        } else if (_sensor_step.getType() == Sensor.TYPE_ACCELEROMETER) {

            _manager.registerListener
                    (_sensor_listener_soft, _sensor_step, Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_UI);
        }


        return true;
    }


    /**
     * 硬件计步器
     */
    private SensorEventListener _sensor_listener_hardware = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            int _step = (int) sensorEvent.values[0];

            if (_sensor_intent_more) {

                //间隔模式
                _step_sum = _step_sum + _step;
            } else {

                //默认模式
                _intent.putExtra(STEP_BR_PARAM, String.valueOf(_step));
                _context.sendBroadcast(_intent);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };


    /**
     * 软件计步器
     */
    private SensorEventListener _sensor_listener_soft = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {


            if (System.currentTimeMillis() - _system_time < 300) {

                return;
            }

            _system_time = System.currentTimeMillis();

            //获取三个方向值
            float[] values = sensorEvent.values;

            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) > 15 || Math.abs(y) > 15 || Math
                    .abs(z) > 15)) {

                int _step = 1;

                if (_sensor_intent_more) {

                    //间隔模式
                    _step_sum = _step_sum + _step;
                } else {

                    //默认模式
                    _intent.putExtra(STEP_BR_PARAM, String.valueOf(_step));
                    _context.sendBroadcast(_intent);
                }

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };


    /**
     * 间隔模式
     */
    private void _send_data() {

        if (null != _handler && _sensor_intent_more) {

            //发送计步数据
            _intent.putExtra(STEP_BR_PARAM, String.valueOf(_step_sum));
            _context.sendBroadcast(_intent);

            //清空之前积累步数
            _step_sum = 0;

            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    _send_data();
                }
            }, _handler_time);
        }
    }


    /**
     * 回收计步器
     */
    public void _destroy_step() {

        if (null != _manager && null != _sensor_listener_hardware) {

            _manager.unregisterListener(_sensor_listener_hardware);
        }

        if (null != _manager && null != _sensor_listener_soft) {

            _manager.unregisterListener(_sensor_listener_soft);
        }

        _sensor_intent_more = false;

        _handler = null;

        _sensor_step = null;

        _manager = null;
    }
}
