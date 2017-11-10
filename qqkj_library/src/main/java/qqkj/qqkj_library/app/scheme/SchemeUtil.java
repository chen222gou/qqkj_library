package qqkj.qqkj_library.app.scheme;

import android.content.Intent;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/10.
 */

public class SchemeUtil {


    public static SchemeUtil getIns() {

        return new SchemeUtil();
    }


    /**
     * 获取Uri中的Scheme
     *
     * @param _intent
     * @return
     */
    public String _get_uri_scheme(Intent _intent) {

        if (null != _intent) {

            return _intent.getScheme();
        }

        return null;
    }


    /**
     * 比较2个Uri中的Scheme是否一致
     *
     * @param _intent
     * @param _scheme
     * @return
     */
    public boolean _get_scheme_equals(Intent _intent, String _scheme) {

        if (null != _intent && null != _scheme) {

            return _scheme.equals(_get_uri_scheme(_intent));
        }

        return false;
    }
}
