package qqkj.qqkj_library.sensor.step;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

/**
 * 获取当前设备的计步器步数
 * Created by apple on 2017/8/17.
 */

public class StepUtil {

    private Context _context = null;

    private static StepUtil _step_util = null;

    public static final String STEP_BR_NAME = "_step_manager";

    public static final String STEP_BR_PARAM = "_step";

    private SensorManager _manager = null;

    private SensorEventListener _sensor_listener = null;

    private Intent _intent = new Intent(STEP_BR_NAME);

    private Sensor _sensor_step = null;

    private boolean _sensor_intent_more = false;    //是否开启 达到一定步数再发送广播,默认不开启,每一步都发送广播

    private int _step_sum = 0;  //间隔模式总步数

    private Handler _handler = new Handler();   //间隔模式消息机制

    private int _handler_time = 5000;

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

        if (_step_util == null) {

            _step_util = new StepUtil(_context, true, _handler_time);
        }

        return _step_util;
    }

    /**
     * 启动计步器
     * <p>
     * 返回null,表示用户没有该传感器,返回_go_step,可以开始进行计步
     * 增加广播(名称:_step_manager)进行监听返回字段_step的值
     */

    public boolean _get_step() {

        if (null == _manager) {

            _manager = (SensorManager) _context.getSystemService(Context.SENSOR_SERVICE);

            if (null == _manager) {

                return false;
            }
        }

        if (null == _sensor_step) {

            _sensor_step = _manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            if (null == _sensor_step) {

                //该设备无运动传感器
                return false;
            }
        }

        //开始间隔模式
        if (_sensor_intent_more) {

            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    _send_data();
                }
            }, _handler_time);
        }

        _sensor_listener = new SensorEventListener() {
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

        _manager.registerListener(_sensor_listener, _sensor_step, Sensor.TYPE_STEP_DETECTOR, SensorManager.SENSOR_DELAY_UI);

        return true;
    }


    /**
     * 间隔模式
     */
    private void _send_data() {

        if (_sensor_intent_more) {

            //发送计步数据
            _intent.putExtra(STEP_BR_PARAM, String.valueOf(_step_sum));
            _context.sendBroadcast(_intent);

            //清空之前积累步数
            _step_sum = 0;

            //
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

        if (null != _manager && null != _sensor_listener) {

            _manager.unregisterListener(_sensor_listener);

            _sensor_step = null;

            _manager = null;
        }
    }
}
