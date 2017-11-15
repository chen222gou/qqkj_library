package qqkj.qqkj_library.view.smart;

import android.content.Context;
import android.graphics.Color;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/15.
 */

public class SmartRefreshUtil {


    private static SmartRefreshUtil _smart_util = null;

    private Context _context = null;

    public SmartRefreshUtil(Context _context){

        this._context = _context;
    }

    public static SmartRefreshUtil getIns(Context _context){

        if(_smart_util == null){

            _smart_util = new SmartRefreshUtil(_context);
        }

        return _smart_util;
    }


    /**
     * 初始化颜色
     * @param _color
     */
    public void _get_init_header(final int... _color){

        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {

                MaterialHeader header = new MaterialHeader(context);

                header.setColorSchemeColors(_color);

                return header;
            }
        });
    }


    public void _get_init_footer(final int... _color){

        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {

                RefreshFooter footer = new BallPulseFooter(context);

                footer.setPrimaryColors(_color);

                return footer;
            }
        });
    }

}
