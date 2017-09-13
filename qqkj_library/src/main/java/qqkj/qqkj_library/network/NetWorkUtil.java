package qqkj.qqkj_library.network;


import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;


/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/11.
 */

public class NetWorkUtil {

    private static NetWorkUtil _get_network_util;

    public static final String REQUEST_CONNECT_ERROR = "qqkj_network_disconnect";

    public static final String LOG_TAG = "qqkj_frame";

    private String _response_result = null;

    private int _response_code = 0;

    private InputStream _input_stream = null;

    private HttpURLConnection _connection = null;

    private URL _url = null;

    /**
     * 单例
     *
     * @return
     */
    public static NetWorkUtil getIns() {

        _get_network_util = new NetWorkUtil();

        return _get_network_util;
    }


    /**
     * http请求 GET方式
     *
     * @param _request_url 请求url
     * @param _request_log 是否打开调试日志,建议开发的时候打开
     * @return
     */
    public String get_http_get(final String _request_url, final boolean _request_log) {

        try {

            _url = new URL(_request_url);

            _connection = (HttpURLConnection) _url.openConnection();

            if (null == _connection) {

                network_log(_request_log, "请求失败,HttpURLConnection为空,请检查您的URL...");

                return REQUEST_CONNECT_ERROR;
            }

            _connection.setRequestMethod("GET");

            _connection.setUseCaches(false);            //不使用缓存

            _connection.setConnectTimeout(5 * 1000);    //连接超时时间

            _connection.setReadTimeout(5 * 1000);   //读取超时时间

            _connection.setInstanceFollowRedirects(true);   //设置是否自动处理重定向

            _connection.setRequestProperty("connection", "keep-alive");  //减少tcp连接次数

            _connection.setRequestProperty("Charset", "utf-8");

            _connection.connect();

            int _response_code = _connection.getResponseCode();

            if (HttpURLConnection.HTTP_OK == _response_code) {

                network_log(_request_log, "请求成功,返回内容如下...");

                _input_stream = _connection.getInputStream();

                _response_result = convert_stream_to_string(_input_stream);

                network_log(_request_log, _response_result);
            } else {

                network_log(_request_log, "请求失败,返回Code:" + _response_code);
            }

            _connection.disconnect();
        } catch (Exception e) {

            network_log(_request_log, "请求错误,返回如下异常...");

            network_log(_request_log, e.getMessage());
        }

        if (null == _response_result) {

            _response_result = REQUEST_CONNECT_ERROR;
        }

        return _response_result;
    }


    /**
     * @param _request_url
     * @param _param_condition
     * @param _request_log
     * @return
     */
    public String get_http_post(final String _request_url, final Map<String, Object> _param_condition,final boolean _request_log) {

        try {

            _url = new URL(_request_url);

            _connection = (HttpURLConnection) _url.openConnection();

            if (_connection == null) {

                network_log(_request_log, "请求失败,HttpURLConnection为空,请检查您的URL...");

                return REQUEST_CONNECT_ERROR;
            }

            _connection.setRequestMethod("POST");

            _connection.setDoInput(true);

            _connection.setDoOutput(true);

            _connection.setUseCaches(false);

            _connection.setConnectTimeout(5 * 1000);

            _connection.setReadTimeout(5 * 1000);

            _connection.setInstanceFollowRedirects(true);   //设置是否自动处理重定向

            _connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            _connection.setRequestProperty("Charset", "utf-8");

            _connection.setRequestProperty("connection", "keep-alive");

            _connection.connect();

            //提交参数
            do_post_param(_connection.getOutputStream(), _param_condition);

            int _response_code = _connection.getResponseCode();

            if (HttpURLConnection.HTTP_OK == _response_code) {

                network_log(_request_log, "请求成功,返回内容如下...");

                _input_stream = _connection.getInputStream();

                _response_result = convert_stream_to_string(_input_stream);

                network_log(_request_log, _response_result);
            } else {

                network_log(_request_log, "请求失败,返回Code:" + _response_code);
            }
        } catch (Exception e) {

            network_log(_request_log, "请求错误,返回如下异常...");

            network_log(_request_log, e.getMessage());
        }

        if (_response_result == null) {

            _response_result = REQUEST_CONNECT_ERROR;
        }

        return _response_result;
    }


    /**
     * 处理post提交参数
     *
     * @param _out_stream_param
     * @param _param_condition
     * @throws Exception
     */
    private void do_post_param(OutputStream _out_stream_param, Map<String, Object> _param_condition) throws Exception {

        //处理参数
        DataOutputStream _out_stream = new DataOutputStream(_out_stream_param);

        StringBuffer sb = new StringBuffer();

        String str = "";

        if (_param_condition != null && _param_condition.size() > 0) {

            Iterator<String> it = _param_condition.keySet().iterator();

            while (it.hasNext()) {

                String key = it.next();

                String value = _param_condition.get(key).toString();

                sb.append(key + "=" + URLEncoder.encode(value, "utf-8") + "&");
            }

            if (sb.length() != 0) {

                str = sb.substring(0, sb.length());
            }
        }

        _out_stream.writeBytes(str);

        _out_stream.flush();

        _out_stream.close();
    }


    /**
     * 将输入流转换成String
     *
     * @param _input_stream
     * @return
     */
    private String convert_stream_to_string(InputStream _input_stream) {

        BufferedReader _buffer_reader = new BufferedReader(new InputStreamReader(_input_stream));

        StringBuilder _string_builder = new StringBuilder();

        String line = null;
        try {

            while ((line = _buffer_reader.readLine()) != null) {

                _string_builder.append(line + "\n");
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            try {

                _input_stream.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        return _string_builder.toString();
    }


    /**
     * 显示日志
     *
     * @param _is_log
     * @param log
     */
    private void network_log(boolean _is_log, String log) {

        if (_is_log) {

            Log.e(LOG_TAG, log);
        }
    }
}
