package qqkj.qqkj_library.snack;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/14.
 */

public class SnackUtil {

    private static SnackUtil _snack_util = null;

    private View _view = null;

    private long _show_time = 0l;


    private SnackUtil(View _view){

        this._view = _view;
    }


    public static SnackUtil getIns(View _view){

        if(_snack_util == null){

            _snack_util = new SnackUtil(_view);
        }

        return _snack_util;
    }


    /**
     * 显示短提示
     * @param _content
     */
    public void get_snack_short(String _content){

        if(System.currentTimeMillis() - _show_time >= 2500){

            _show_time = System.currentTimeMillis();

            Snackbar.make(_view,_content,Snackbar.LENGTH_SHORT).show();
        }
    }


    /**
     * 显示长提示
     * @param _content
     */
    public void get_snack_long(String _content){

        if(System.currentTimeMillis() - _show_time >= 2500){

            _show_time = System.currentTimeMillis();

            Snackbar.make(_view,_content,Snackbar.LENGTH_LONG).show();
        }
    }
}
