package qqkj.qqkj_library.sensor.step

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Created by chen2gou on 2017/8/24.
 *
 * 操作计步器方法
 */

class StepUtil {


    /**
     * 变量声明
     */
    var _context: Context? = null

    var _sensor_manager: SensorManager? = null

    var _sensor_listener: SensorEventListener? = null

    var _intent = Intent("_step_manager")

    var _sensor: Sensor? = null


    /**
     * 次构造函数声明
     */
    constructor(_context: Context?) {

        this._context = _context
    }


    /**
     * 静态块声明
     */
    companion object {

        var _step_util: StepUtil? = null

        fun getIns(_context: Context?): StepUtil {

            if (null == _step_util) {

                _step_util = StepUtil(_context)

            }

            return _step_util!!
        }
    }


    /**
     * 获取计步器步数方法
     */
    fun _get_step(): Boolean {

        _sensor_manager = _context!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        _sensor = _sensor_manager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (null == _sensor) {

            //当前设备不存在计步传感器
            return false
        }

        _sensor_listener = object : SensorEventListener {

            override fun onSensorChanged(p0: SensorEvent?) {

                //获取计步器返回的运动步数
                var _step = p0!!.values[0]

                //将获取到的步数广播出去
                _intent.putExtra("_step", _step)

                _context!!.sendBroadcast(_intent)
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }

        return true
    }


    /**
     * 回收计步传感器方法
     */
    fun _destroy_step() {

        if (null != _sensor_manager && null != _sensor_listener) {

            //取消注册计步器方法
            _sensor_manager!!.unregisterListener(_sensor_listener)
        }
    }
}