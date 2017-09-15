package qqkj.qqkj_library.toast;

import android.content.Context;
import android.widget.Toast;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/14.
 */

public class ToastUtil {

    private static ToastUtil _toast_util = null;

    private Context _context = null;

    private long _show_time = 0l;


    private ToastUtil(Context _context){

        this._context = _context;
    }


    public static ToastUtil getIns(Context _context){

        if(_toast_util == null){

            _toast_util = new ToastUtil(_context);
        }

        return _toast_util;
    }


    /**
     * 显示短提示
     * @param _content
     */
    public void get_toast_short(String _content){

        if(System.currentTimeMillis() - _show_time >= 2500){

            _show_time = System.currentTimeMillis();

            Toast.makeText(_context,_content,Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 显示长提示
     * @param _content
     */
    public void get_toast_long(String _content){

        if(System.currentTimeMillis() - _show_time >= 4000){

            _show_time = System.currentTimeMillis();

            Toast.makeText(_context,_content,Toast.LENGTH_LONG).show();
        }
    }
}
