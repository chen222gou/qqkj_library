package qqkj.qqkj_library.network;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/11.
 */

public class NetWorkUtil {

    private static NetWorkUtil _get_network_util;

    public static final String REQUEST_CONNECT_ERROR = "qqkj_network_disconnect";

    public static final String LOG_TAG = "qqkj_network";

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
     * http 网络请求
     * get方式
     */
    public String get_http_get(final String _request_url, final boolean _request_log) {

        try {

            _url = new URL(_request_url);

            _connection = (HttpURLConnection) _url.openConnection();

            if (null == _connection) {

                return REQUEST_CONNECT_ERROR;
            }

            _connection.setRequestMethod("GET");

            _connection.setUseCaches(false);            //不使用缓存

            _connection.setConnectTimeout(5 * 1000);    //连接超时时间

            _connection.setReadTimeout(5 * 1000);   //读取超时时间

            _connection.setInstanceFollowRedirects(true);   //设置是否自动处理重定向

            _connection.setRequestProperty("connection", "keep-alive");  //减少tcp连接次数

            _connection.connect();

            int _response_code = _connection.getResponseCode();

            if (HttpURLConnection.HTTP_OK == _response_code) {

                Log.e(LOG_TAG, "请求成功,返回内容如下...");

                _input_stream = _connection.getInputStream();

                _response_result = convert_stream_to_string(_input_stream);

                Log.e(LOG_TAG, _response_result);
            } else {

                if (_request_log) {

                    Log.e(LOG_TAG, "请求失败,返回Code:" + _response_code);
                }
            }

            _connection.disconnect();
        } catch (Exception e) {

            if (_request_log) {

                Log.e(LOG_TAG, "请求错误,返回如下异常...");

                Log.e(LOG_TAG, e.getMessage());
            }
        }

        if (null == _response_result) {

            _response_result = REQUEST_CONNECT_ERROR;
        }

        return _response_result;
    }


    /**
     * 将输入流转换成String
     *
     * @param _input_stream
     * @return
     */
    public String convert_stream_to_string(InputStream _input_stream) {

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
}
