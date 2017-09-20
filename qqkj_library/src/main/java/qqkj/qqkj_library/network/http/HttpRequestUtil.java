package qqkj.qqkj_library.network.http;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 这个类是用来做甚的
 * 网络请求
 * <p>
 * Created by 陈二狗 on 2017/9/11.
 */

public class HttpRequestUtil {

    private static HttpRequestUtil _get_http_request_util;

    private static final String REQUEST_CONNECT_ERROR = "qqkj_network_disconnect";

    private static final String LOG_TAG = "qqkj_frame";

    private static final String CHARSET = "utf-8";

    private static final String CONNECTION_KEEP = "keep-alive";

    private static final String CONNECTION_CLOSE = "close";

    private int CONNECTION_TIMEOUT = 10 * 1000;

    private int READ_TIMEOUT = 10 * 1000;

    private static final String REQUEST_METHOD_GET = "GET";

    private static final String REQUEST_METHOD_POST = "POST";

    private static final String UPLOAD_CONTENTTYPE = "multipart/form-data;boundary=*****";

    private static final String UPLOAD_END = "\r\n";

    private static final String UPLOAD_HYPHENS = "--";

    private static final String UPLOAD_BOUNDARY = "*****";

    private String _response_result = null;

    private int _response_code = 0;

    private InputStream _input_stream = null;

    private HttpURLConnection _connection = null;

    private URL _url = null;

    private Context _context = null;

    public static final String UPLOAD_PROGRESS_BR_NAME = "_upload_progress_br_name";

    public static final String UPLOAD_PREGRESS_BR_PARAM = "_upload_progress_value";

    private Intent _intent = new Intent(UPLOAD_PROGRESS_BR_NAME);

    private FileInputStream _file_input_stream = null;

    private long _file_length = 0l;

    private String UPLOAD_CONTENT_POSITION = null;

    private String UPLOAD_CONTENT_TYPE = null;

    private HttpResponseModel _response_model = new HttpResponseModel();


    /**
     * 单例
     *
     * @return
     */
    public static HttpRequestUtil getIns() {

        _get_http_request_util = new HttpRequestUtil();

        return _get_http_request_util;
    }


    /**
     * 设置连接超时时间
     *
     * @param _connect_time_out
     * @return
     */
    public HttpRequestUtil set_connect_time_out(int _connect_time_out) {

        CONNECTION_TIMEOUT = _connect_time_out;

        return _get_http_request_util;
    }


    /**
     * 设置读取超时时间
     *
     * @param set_read_time_out
     * @return
     */
    public HttpRequestUtil set_read_time_out(int set_read_time_out) {

        READ_TIMEOUT = set_read_time_out;

        return _get_http_request_util;
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

            _response_model._response_error_msg = e.getMessage();
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

            _response_model._response_error = true;

            _response_model._response_error_msg = e.getMessage();
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
     * 根据文件路径上传文件
     *
     * @param _request_url
     * @param _file_path
     * @param _request_log
     * @return
     */
    public HttpResponseModel _get_http_upload(Context _context, final String _request_url, final String _file_key, final String _file_name, final String _file_path, final boolean _request_log, UploadProgressListener _listener) {

        this._context = _context;

        try {

            File _file = new File(_file_path);

            _file_input_stream = new FileInputStream(_file);

            _file_length = _file.length();

            _url = new URL(_request_url);

            UPLOAD_CONTENT_POSITION = "Content-Disposition: form-data; name=\"" + _file_key + "\";filename=\"" + _file_name + "\"" + UPLOAD_END;

            UPLOAD_CONTENT_TYPE = "Content-Type: application/octet-stream; charset=" + CHARSET + UPLOAD_END + UPLOAD_END;

            _connection = (HttpURLConnection) _url.openConnection();

            _connection.setFixedLengthStreamingMode(_file_length +
                    UPLOAD_CONTENT_POSITION.getBytes().length + UPLOAD_CONTENT_TYPE.getBytes().length +
                    3 * UPLOAD_HYPHENS.getBytes().length + 3 * UPLOAD_END.getBytes().length +
                    2 * UPLOAD_BOUNDARY.getBytes().length);

            _connection.setRequestMethod(REQUEST_METHOD_POST);

            _connection.setDoInput(true);

            _connection.setDoOutput(true);

            _connection.setConnectTimeout(CONNECTION_TIMEOUT);

            _connection.setReadTimeout(READ_TIMEOUT);

            _connection.setUseCaches(false);

            _connection.setInstanceFollowRedirects(true);   //设置是否自动处理重定向

            _connection.setRequestProperty("Connection", CONNECTION_CLOSE);

            _connection.setRequestProperty("Charset", CHARSET);

            _connection.setRequestProperty("Content-Type", UPLOAD_CONTENTTYPE);

            do_upload_param(_connection.getOutputStream(), _request_log, _listener);

            int _response_code = _connection.getResponseCode();

            _response_model._response_code = _response_code;

            if (HttpURLConnection.HTTP_OK == _response_code) {

                network_log(_request_log, "请求成功,返回内容如下...");

                _input_stream = _connection.getInputStream();

                _response_result = convert_stream_to_string(_input_stream);

                _response_model._response_content = _response_result;

                _set_progress(_request_log, 100, _listener);
            } else {

                _response_model._response_error = true;

                _response_model._response_error_msg = "服务器连接异常....";
            }

        } catch (Exception e) {

            _response_model._response_error = true;

            _response_model._response_error_msg = e.getMessage();
        }

        network_log(_request_log, "                                                  ");

        network_log(_request_log, "**************************************************");

        network_log(_request_log, "                                                  ");

        network_log(_request_log, "request_url: " + _request_url);

        network_log(_request_log, "file_path: " + _file_path);

        network_log(_request_log, "response_error: " + _response_model._response_error);

        network_log(_request_log, "response_error_msg: " + _response_model._response_error_msg);

        network_log(_request_log, "response_code: " + _response_model._response_code);

        network_log(_request_log, "response_content: " + _response_model._response_content);

        return _response_model;
    }


    /**
     * 设置progress
     *
     * @param _progress
     */
    private void _set_progress(boolean _request_log, int _progress, UploadProgressListener _listener) {

        _intent.putExtra(UPLOAD_PREGRESS_BR_PARAM, _progress + "");

        _context.sendBroadcast(_intent);

        _listener.get_progress(_progress);

        network_log(_request_log, _progress + "");
    }


    /**
     * 上传文件写入文件流
     *
     * @param _out_stream_param
     * @throws Exception
     */
    private void do_upload_param(OutputStream _out_stream_param, boolean _request_log, UploadProgressListener _listener) throws Exception {

        DataOutputStream _out_stream = new DataOutputStream(_out_stream_param);

        _out_stream.writeBytes(UPLOAD_HYPHENS + UPLOAD_BOUNDARY + UPLOAD_END);

        _out_stream.writeBytes(UPLOAD_CONTENT_POSITION);

        _out_stream.writeBytes(UPLOAD_CONTENT_TYPE);

        byte[] _byte = new byte[1024];

        int _length = 0;

        int _temp_progress = -1;

        long _current_byte = 0;

        while ((_length = _file_input_stream.read(_byte)) != -1) {

            _out_stream.write(_byte, 0, _length);

            _current_byte = _current_byte + 1024;

            double _progress_double = (double) ((double) _current_byte / (double) _file_length);

            int _progress = (int) (_progress_double * 100);

            if (_temp_progress != _progress && _progress < 100) {

                _temp_progress = _progress;

                _set_progress(_request_log, _progress, _listener);

            }
        }

        _out_stream.writeBytes(UPLOAD_END);

        _out_stream.writeBytes(UPLOAD_HYPHENS + UPLOAD_BOUNDARY + UPLOAD_HYPHENS + UPLOAD_END);

        _file_input_stream.close();

        _out_stream.close();
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
     * @param log
     */
    private void network_log(boolean _is_log, String log) {

        if (_is_log) {

            Log.e(LOG_TAG, log);
        }
    }


    /**
     * 返回类
     */
    public class HttpResponseModel {

        public String _response_content = null;

        public int _response_code = 0;

        public boolean _response_error = false;

        public String _response_error_msg = "服务器请求接口数据正常....";
    }


    /**
     * 上传/下载回调接口
     */
    public interface UploadProgressListener {

        void get_progress(int _progress);
    }
}
