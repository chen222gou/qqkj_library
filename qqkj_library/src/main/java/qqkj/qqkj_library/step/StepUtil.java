package qqkj.qqkj_library.step;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by apple on 2017/8/17.
 */

public class StepUtil {

    private Context _context = null;

    private static StepUtil _step_util = null;

    private SensorManager _manager = null;

    private SensorEventListener _sensor_listener = null;

    private Intent _intent = new Intent("_step_manager");

    /**
     * 构造函数,传入上下文对象
     * @param _context
     */
    private StepUtil(Context _context ){

        this._context = _context;

    }

    /**
     * 单例
     * @param _context
     */
        public static StepUtil getIns(Context _context){

            if( _step_util==null ){

                _step_util = new StepUtil(_context);
            }

            return _step_util;
        }


    /**
     * 启动计步器
     *
     * 返回null,表示用户没有该传感器,返回_go_step,可以开始进行计步
     * 增加广播(名称:_step_manager)进行监听返回字段_step的值
     */
    public boolean _get_step(){

        _manager = (SensorManager) _context.getSystemService(_context.SENSOR_SERVICE);

        Sensor _senor = _manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(null==_senor){

            //该设备无运动传感器
            return false;
        }

        _sensor_listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                String _step = sensorEvent.values[0]+"";
                _intent.putExtra("_step",_step);
                _context.sendBroadcast(_intent);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

                System.out.println("sensor = [" + sensor + "], i = [" + i + "]");
            }
        };

        _manager.registerListener(_sensor_listener,_senor,Sensor.TYPE_STEP_COUNTER,SensorManager.SENSOR_DELAY_UI);

        return true;
    }

    /**
     *
     * 回收计步器
     */
    public void _destroy_step(){

        if( null!=_manager && null!=_sensor_listener ){

            _manager.unregisterListener(_sensor_listener);
        }
    }
}
