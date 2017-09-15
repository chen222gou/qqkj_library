package qqkj.qqkj_library.network.param;

import android.util.Log;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/14.
 */

public class HttpParamUtil {

    private String _result_param = null;

    private static final String LOG_TAG = "qqkj_frame";

    private HttpParamUtil _http_param_util = null;


    public HttpParamUtil getIns() {

        _http_param_util = new HttpParamUtil();

        return _http_param_util;
    }


    /**
     * String 参数处理
     *
     * @param _string_param
     * @return
     */
    public String get_string_param(String _string_param, boolean _param_log) {

        if (null != _string_param) {

            try {

                _result_param = URLEncoder.encode(_string_param, "utf-8");

            } catch (Exception e) {

                if (_param_log) {

                    Log.e(LOG_TAG, "参数拼接返回如下错误...");
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }

        return _result_param;
    }


    /**
     * map参数传递拼接
     *
     * @param _condition_param
     * @return
     */
    public String get_map_param(Map<String, Object> _condition_param, boolean _param_log) {

        try {

            StringBuffer _string_buffer = new StringBuffer();

            if (_condition_param != null && _condition_param.size() > 0) {

                Iterator<String> _iterator = _condition_param.keySet().iterator();

                while (_iterator.hasNext()) {

                    String _map_key = _iterator.next();

                    String _map_value = _condition_param.get(_map_key).toString();

                    _string_buffer.append(_map_key + "=" + URLEncoder.encode(_map_value, "utf-8") + "&");
                }

                if (_string_buffer.length() != 0) {

                    _result_param = _string_buffer.substring(0, _string_buffer.length());
                }
            }
        } catch (Exception e) {

            if (_param_log) {

                Log.e(LOG_TAG, "参数拼接返回如下错误...");
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        return _result_param;
    }
}
