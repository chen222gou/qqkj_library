package qqkj.qqkj_library.distance;

import android.content.Context;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/7.
 */

public class DistanceUtil {

    private static DistanceUtil _distance_util = null;

    private Context _context = null;

    private static final double EARTH_RADIUS = 6378.137;


    /**
     * 构造方法
     * @param _context
     */
    private DistanceUtil(Context _context){

        this._context = _context;
    }


    /**
     * 单例
     * @param _context
     * @return
     */
    public static DistanceUtil getIns(Context _context){

        if(null == _distance_util){

            _distance_util = new DistanceUtil(_context);
        }

        return _distance_util;
    }


    /**
     * 计算2个经纬度之间的距离
     * @param _lon1
     * @param _lat1
     * @param _lon2
     * @param _lat2
     * @return
     */
    public int _get_distance(double _lon1, double _lat1, double _lon2, double _lat2){

        double _lat_end, _lon_end, _distance, _sina, _sinb;

        _lat1 = radius(_lat1);

        _lat2 = radius(_lat2);

        _lat_end = _lat1 - _lat2;

        _lon_end = radius(_lon1 - _lon2);

        _sina = Math.sin(_lat_end / 2.0);

        _sinb = Math.sin(_lon_end / 2.0);

        _distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(_sina * _sina + Math.cos(_lat1) * Math.cos(_lat2) * _sinb * _sinb));

        return (int)(_distance*1000);
    }


    /**
     * 判断是否在这个范围中
     * @param _range
     * @return
     */
    public boolean _get_range(double _lon1, double _lat1, double _lon2, double _lat2, int _range){

        int _distance = _get_distance(_lon1, _lat1, _lon2, _lat2);

        if(_distance <= _range){

            return true;
        }

        return false;
    }


    /**
     * 计算包含的弧度
     * @param _d
     * @return
     */
    private double radius(double _d){

        return _d * Math.PI / 180.0;
    }
}
