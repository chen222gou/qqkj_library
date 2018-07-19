package qqkj.qqkj_library.network.http;


import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import qqkj.qqkj_library.network.model.HttpResponseModel;


/**
 * 这个类是用来做甚的
 * 网络请求
 * <p>
 * Created by 陈二狗 on 2017/9/11.
 */

public class HttpRequestUtil {

//    private static HttpRequestUtil _get_http_request_util;

    private static final String LOG_TAG = "qqkj_frame";

    private static final String CHARSET = "utf-8";

    private static final String CONNECTION_KEEP = "keep-alive";

    private int CONNECTION_TIMEOUT = 10 * 1000;

    private int READ_TIMEOUT = 10 * 1000;

    private static final String REQUEST_METHOD_GET = "GET";

    private static final String REQUEST_METHOD_POST = "POST";

    private String _response_result = null;

    private InputStream _input_stream = null;

    private HttpURLConnection _connection = null;

    private URL _url = null;

    private HttpResponseModel _response_model = new HttpResponseModel();


    /**
     * 单例
     */
    public static HttpRequestUtil getIns() {

//        _get_http_request_util =

        return new HttpRequestUtil();
    }


    /**
     * 设置连接超时时间
     *
     * @param _connect_time_out
     * @return
     */
    public HttpRequestUtil set_connect_time_out(int _connect_time_out) {

        CONNECTION_TIMEOUT = _connect_time_out;

        return this;
    }


    /**
     * 设置读取超时时间
     *
     * @param set_read_time_out
     * @return
     */
    public HttpRequestUtil set_read_time_out(int set_read_time_out) {

        READ_TIMEOUT = set_read_time_out;

        return this;
    }


    /**
     * http请求 GET方式
     *
     * @param _request_url 请求url
     * @param _request_log 是否打开调试日志,建议开发的时候打开
     * @return
     */
    public HttpResponseModel get_http_get(final String _request_url, final boolean _request_log) {

        try {

            _url = new URL(_request_url);

            _connection = (HttpURLConnection) _url.openConnection();

            if (null == _connection) {

                _response_model._response_error = true;

                _response_model._response_error_msg = "请求失败,HttpURLConnection为空,请检查您的URL...";
            }

            _connection.setRequestMethod(REQUEST_METHOD_GET);

            _connection.setUseCaches(false);            //不使用缓存

            _connection.setConnectTimeout(CONNECTION_TIMEOUT);    //连接超时时间

            _connection.setReadTimeout(READ_TIMEOUT);   //读取超时时间

            _connection.setInstanceFollowRedirects(true);   //设置是否自动处理重定向

            _connection.setRequestProperty("connection", CONNECTION_KEEP);  //减少tcp连接次数

            _connection.setRequestProperty("Charset", CHARSET);

            int _response_code = _connection.getResponseCode();

            _response_model._response_code = _response_code;

            if (HttpURLConnection.HTTP_OK == _response_code) {

                _input_stream = _connection.getInputStream();

                _response_result = convert_stream_to_string(_input_stream);

                _response_model._response_content = _response_result;
            } else {

                _response_model._response_error = true;

                _response_model._response_error_msg = "服务器连接异常....";
            }

            _connection.disconnect();
        } catch (Exception e) {

            _response_model._response_error = true;

            if(null != e.getCause()){

                _response_model._response_error_msg = e.getMessage() + "\n" + e.getCause().getMessage();
            }
        }

        network_log(_request_log, "                                                  ");

        network_log(_request_log, "**************************************************");

        network_log(_request_log, "                                                  ");

        network_log(_request_log, "request_url: " + _request_url);

        network_log(_request_log, "response_error: " + _response_model._response_error);

        network_log(_request_log, "response_error_msg: " + _response_model._response_error_msg);

        network_log(_request_log, "response_code: " + _response_model._response_code);

        network_log(_request_log, "response_content: " + _response_model._response_content);

        return _response_model;
    }


    /**
     * http post 请求
     *
     * @param _request_url
     * @param _request_param
     * @param _request_log
     * @return
     */
    public HttpResponseModel get_http_post(final String _request_url, final String _request_param, final String _content_type, final boolean _request_log) {

        try {

            _url = new URL(_request_url);

            _connection = (HttpURLConnection) _url.openConnection();

            if (_connection == null) {

                _response_model._response_error = true;

                _response_model._response_error_msg = "请求失败,HttpURLConnection为空,请检查您的URL...";
            }

            _connection.setRequestMethod(REQUEST_METHOD_POST);

            _connection.setDoInput(true);

            _connection.setDoOutput(true);

            _connection.setUseCaches(false);

            _connection.setConnectTimeout(CONNECTION_TIMEOUT);

            _connection.setReadTimeout(READ_TIMEOUT);

            _connection.setInstanceFollowRedirects(true);   //设置是否自动处理重定向

            _connection.setRequestProperty("Content-Type", _content_type);

            _connection.setRequestProperty("Charset", CHARSET);

            _connection.setRequestProperty("connection", CONNECTION_KEEP);

            //提交参数
            do_post_param(_connection.getOutputStream(), _request_param);

            int _response_code = _connection.getResponseCode();

            _response_model._response_code = _response_code;

            if (HttpURLConnection.HTTP_OK == _response_code) {

                _input_stream = _connection.getInputStream();

                _response_result = convert_stream_to_string(_input_stream);

                _response_model._response_content = _response_result;

            } else {

                _response_model._response_error = true;

                _response_model._response_error_msg = "服务器连接异常....";
            }
        } catch (Exception e) {

            e.printStackTrace();

            _response_model._response_error = true;

            if(null != e.getCause()){

                _response_model._response_error_msg = e.getMessage() + "\n" + e.getCause().getMessage();
            }
        }

        network_log(_request_log, "                                                  ");

        network_log(_request_log, "**************************************************");

        network_log(_request_log, "                                                  ");

        network_log(_request_log, "request_url: " + _request_url);

        network_log(_request_log, "request_param: " + _request_param);

        network_log(_request_log, "response_error: " + _response_model._response_error);

        network_log(_request_log, "response_error_msg: " + _response_model._response_error_msg);

        network_log(_request_log, "response_code: " + _response_model._response_code);

        network_log(_request_log, "response_content: " + _response_model._response_content);

        return _response_model;
    }


    /**
     * 处理post提交参数
     *
     * @param _out_stream_param
     * @param _string_param
     * @throws Exception
     */
    private void do_post_param(OutputStream _out_stream_param, String _string_param) throws Exception {

        //处理参数
        DataOutputStream _out_stream = new DataOutputStream(_out_stream_param);

        _out_stream.writeBytes(_string_param);

        _out_stream.flush();

        _out_stream.close();
    }


    /**
     * 将输入流转换成String
     *
     * @param _input_stream
     * @return
     */
    private String convert_stream_to_string(InputStream _input_stream) throws Exception {

        BufferedReader _buffer_reader = new BufferedReader(new InputStreamReader(_input_stream, "utf-8"));

        StringBuilder _string_builder = new StringBuilder();

        String line = null;

        while ((line = _buffer_reader.readLine()) != null) {

            _string_builder.append(line + "\n");
        }

        _input_stream.close();

        return _string_builder.toString();
    }


    /**
     * 显示日志
     *
     * @param _is_log
     * @param _log
     */
    private void network_log(boolean _is_log, String _log) {

        if (_is_log) {

            Log.e(LOG_TAG, _log);
        }
    }


    /**
     * 获取异常信息
     * @param ex
     * @return
     */
    private String get_exception(Exception ex){

        ByteArrayOutputStream _out_put_stream = new ByteArrayOutputStream();

        PrintStream _print_stream = new PrintStream(_out_put_stream);

        ex.printStackTrace(_print_stream);

        String _ex_content = new String(_out_put_stream.toByteArray());

        try {

            _out_put_stream.close();

        }catch (Exception e){

        }

        return _ex_content;
    }

}
