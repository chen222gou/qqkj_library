package qqkj.qqkj_library.network.parser.json;

import com.google.gson.Gson;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/13.
 */

public class JsonUtil {

    private static JsonUtil _json_util = null;

    private Gson _gson = null;

    private Object _object_result = null;


    public static JsonUtil getIns() {

        if (_json_util == null) {

            _json_util = new JsonUtil();
        }

        return _json_util;
    }


    /**
     * 字符串解析JSON
     *
     * @param _json_param
     * @param _object_param
     * @return
     */
    public Object _get_json_to_object(String _json_param, Object _object_param) {

        _gson = new Gson();

        _object_result = _gson.fromJson(_json_param, _object_param.getClass());

        return _object_result;
    }


    /**
     * 将Object解析为Json
     *
     * @param _object_param
     * @return
     */
    public String _get_object_to_json(Object _object_param) {

        return _gson.toJson(_object_param);
    }
}
