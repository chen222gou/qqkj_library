package qqkj.qqkj_library.view.statusbar;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/10/10.
 */

public class StatusBarUtil {

    private static StatusBarUtil _status_bar_util = null;

    private Activity _context;


    private StatusBarUtil(Activity _context) {

        this._context = _context;
    }

    public static StatusBarUtil getIns(Activity _context) {

        _status_bar_util = new StatusBarUtil(_context);

        return _status_bar_util;
    }


    /**
     * 设置statusbar透明(设置背景图片)
     */
    public void set_status_bar_translucent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window _window = _context.getWindow();

            _window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 设置statusbar背景色
     *
     * @param _color
     */
    public void set_status_bar_color(int _color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window _window = _context.getWindow();

            _window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            _window.setStatusBarColor(_color);
        }
    }
}
