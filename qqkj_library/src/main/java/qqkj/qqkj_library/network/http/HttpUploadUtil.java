package qqkj.qqkj_library.network.http;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import qqkj.qqkj_library.network.model.HttpUploadModel;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/21.
 */

public class HttpUploadUtil {

    private HttpURLConnection _connection = null;

    private static final String UPLOAD_CONTENTTYPE = "multipart/form-data;boundary=*****";

    private static final String UPLOAD_END = "\r\n";

    private static final String UPLOAD_HYPHENS = "--";

    private static final String UPLOAD_BOUNDARY = "*****";

    private Context _context = null;

    public static final String UPLOAD_PROGRESS_BR_NAME = "_upload_progress_br_name";

    public static final String UPLOAD_PREGRESS_BR_PARAM = "_upload_progress_value";

    private Intent _intent = new Intent(UPLOAD_PROGRESS_BR_NAME);

    private FileInputStream _file_input_stream = null;

    private long _file_length = 0l;

    private String UPLOAD_CONTENT_POSITION = null;

    private String UPLOAD_CONTENT_TYPE = null;

    private int _upload_file_index = 1;

    private static final String LOG_TAG = "qqkj_frame";

    private static final String CHARSET = "utf-8";

    private static final String CONNECTION_CLOSE = "close";

    private static final String REQUEST_METHOD_POST = "POST";

    private int CONNECTION_TIMEOUT = 10 * 1000;

    private int READ_TIMEOUT = 10 * 1000;


    public static HttpUploadUtil getIns() {

        return new HttpUploadUtil();
    }


    /**
     * 根据文件路径上传文件
     *
     * @param _request_url
     * @param _file_path
     * @param _request_log
     * @return
     */
    public HttpUploadModel _get_http_upload(Context _context, final String _request_url, final String _file_key, final String _file_path, final boolean _request_log, HttpUploadListener _listener) {

        this._context = _context;

        HttpUploadModel _response_model = new HttpUploadModel();

        try {

            File _file = new File(_file_path);

            String _file_name_last = _file.getName().substring(_file.getName().lastIndexOf(".") + 1);

            String _file_name_temp = "qqkj_android_" + System.currentTimeMillis();

            String _file_name = get_file_name(_file_name_temp) + "." + _file_name_last;

            _file_input_stream = new FileInputStream(_file);

            _file_length = _file.length();

            URL _url = new URL(_request_url);

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

                InputStream _input_stream = _connection.getInputStream();

                String _response_result = convert_stream_to_string(_input_stream);

                _response_model._response_content = _response_result;

                _set_progress(_request_log, 100, _upload_file_index, _listener);
            } else {

                _response_model._response_error = true;

                _response_model._response_error_msg = "服务器连接异常....";
            }

        } catch (Exception e) {


            _response_model._response_error = true;

            _response_model._response_error_msg = get_exception(e);
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
     * 上传多文件
     *
     * @param _context
     * @param _request_url
     * @param _file_key
     * @param _file_path
     * @param _request_log
     * @param _listener
     * @return
     */
    public List<HttpUploadModel> _get_http_upload_more(Context _context, final String _request_url, final String[] _file_key,

                                                       final String[] _file_path, final boolean _request_log, HttpUploadListener _listener) {

        List<HttpUploadModel> _list = new ArrayList<>();

        HttpUploadModel _model = null;

        if (null != _file_key && null != _file_path && _file_path.length == _file_key.length) {

            for (int _i = 0; _i < _file_key.length; _i++) {

                _upload_file_index = _i + 1;

                _model = _get_http_upload(_context, _request_url, _file_key[_i], _file_path[_i], _request_log, _listener);

                _list.add(_model);
            }
        } else {

            network_log(_request_log, "上传多文件参数传递错误,请检查参数_file_key[], _file_name[], _file_paht[]");
        }

        return _list;
    }


    /**
     * 设置progress
     *
     * @param _progress
     */
    private void _set_progress(boolean _request_log, int _progress, int _index, HttpUploadListener _listener) {

        _intent.putExtra(UPLOAD_PREGRESS_BR_PARAM, _progress + "");

        _context.sendBroadcast(_intent);

        _listener.get_progress(_progress, _index);

        network_log(_request_log, "上传第" + _index + "个文件,上传进度: " + _progress + "%");
    }


    /**
     * 上传文件写入文件流
     *
     * @param _out_stream_param
     * @throws Exception
     */
    private void do_upload_param(OutputStream _out_stream_param, boolean _request_log, HttpUploadListener _listener) throws Exception {

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

                _set_progress(_request_log, _progress, _upload_file_index, _listener);

            }
        }

        _out_stream.writeBytes(UPLOAD_END);

        _out_stream.writeBytes(UPLOAD_HYPHENS + UPLOAD_BOUNDARY + UPLOAD_HYPHENS + UPLOAD_END);

        _file_input_stream.close();

        _out_stream.close();
    }





    /**
     * 上传回调接口
     */
    public interface HttpUploadListener {

        void get_progress(int _progress, int _index);
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
     *
     * @param ex
     * @return
     */
    private String get_exception(Exception ex) {

        ByteArrayOutputStream _out_put_stream = new ByteArrayOutputStream();

        PrintStream _print_stream = new PrintStream(_out_put_stream);

        ex.printStackTrace(_print_stream);

        String _ex_content = new String(_out_put_stream.toByteArray());

        try {

            _out_put_stream.close();

        } catch (Exception e) {

        }

        return _ex_content;
    }


    /**
     * 生成文件名
     *
     * @param _message
     * @return
     */
    private String get_file_name(String _message) throws Exception {

        MessageDigest _md = MessageDigest.getInstance("MD5");

        byte[] _input = _message.getBytes();

        byte[] _buff = _md.digest(_input);

        String _md5_str = bytes_to_hex(_buff).toLowerCase();

        return _md5_str;
    }


    private String bytes_to_hex(byte[] _bytes) {

        StringBuffer _md5_str = new StringBuffer();

        int _digital = 0;

        for (int i = 0; i < _bytes.length; i++) {

            _digital = _bytes[i];

            if (_digital < 0) {

                _digital += 256;
            }
            if (_digital < 16) {

                _md5_str.append("0");
            }

            _md5_str.append(Integer.toHexString(_digital));
        }

        return _md5_str.toString().toUpperCase();
    }
}
