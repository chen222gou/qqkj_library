package qqkj.qqkj_library.zxing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * 二维码扫描工具类
 * <p>
 * Created by 陈二狗 on 2017/11/8.
 */

public class ZxingUtil {

    private static ZxingUtil _zxing_util = null;

    private Context _context = null;

    public ZxingUtil(Activity _context){

        this._context = _context;
    }

    public static ZxingUtil getIns(Activity _context){

        return new ZxingUtil(_context);
    }

    /**
     * 开始扫描
     * @param _activity_path
     */
    public void _get_start_zxing(String _activity_path){

        _context.startActivity(new Intent(_context, ZxingActivity.class).putExtra("_scan_class_name",_activity_path));
    }
}
