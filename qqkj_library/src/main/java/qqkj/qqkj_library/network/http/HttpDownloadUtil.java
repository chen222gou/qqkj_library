package qqkj.qqkj_library.network.http;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/21.
 */

public class HttpDownloadUtil {

    private static HttpDownloadUtil _download_util = null;

    private static final String REQUEST_METHOD = "POST";

    private static final String LOG_TAG = "qqkj_frame";

    private URL _url = null;

    private HttpURLConnection _connection = null;

    private InputStream _input_stream = null;

    private OutputStream _out_stream = null;

    private int _file_length = 0;


    public static HttpDownloadUtil getIns() {

        return new HttpDownloadUtil();
    }


    /**
     * 显示日志
     *
     * @param _show_log
     * @param _log
     */
    private void network_log(boolean _show_log, String _log) {

        if (_show_log) {

            Log.e(LOG_TAG, _log);
        }
    }


    /**
     * 下载文件
     *
     * @param _request_url
     * @param _file_dir_name
     * @param _file_name
     * @param _listener
     * @return
     */
    public HttpDownloadModel get_http_download(final String _request_url, final String _file_dir_name, final String _file_name, boolean _request_log, final HttpDownloadListener _listener) {

        HttpDownloadModel _response_model = new HttpDownloadModel();

        String _file_dir = Environment.getExternalStorageDirectory().getAbsolutePath();

        try {

            String _file_path = _file_dir + File.separator + _file_dir_name;

            File _parent_dir = new File(_file_path);

            File _file = null;

            boolean _create_dir = false;

            if (!_parent_dir.exists()) {

                //创建存放文件目录
                _create_dir = _parent_dir.mkdir();
            }

            //如果文件夹创建成功或者文件夹存在 继续执行
            if (_create_dir || _parent_dir.exists()) {

                _file = new File(_file_path + File.separator + _file_name);

                if (_file.exists()) {

                    _file.delete();
                }

                boolean _create_file = _file.createNewFile();

                if (_create_file) {

                    _url = new URL(_request_url);

                    _connection = (HttpURLConnection) _url.openConnection();

                    _connection.setRequestMethod("GET");

                    _connection.setConnectTimeout(10 * 1000);

                    _input_stream = _connection.getInputStream();

                    _file_length = _connection.getContentLength();

                    network_log(_request_log, "content-length: " + _file_length);

                    _out_stream = new FileOutputStream(_file);

                    byte[] _bytes = new byte[1024];

                    BufferedInputStream _in = new BufferedInputStream(_input_stream, 1024);

                    BufferedOutputStream _out = new BufferedOutputStream(_out_stream, 1024);

                    int _read = 0;

                    int _progress_temp = -1;

                    long _current_byte = 0;

                    while ((_read = _in.read(_bytes, 0, 1024)) != -1) {

                        _out.write(_bytes, 0, _read);

                        _current_byte = _current_byte + 1024;

                        if (_current_byte > _file_length) {

                            _current_byte = _file_length;
                        }

                        int _progress = (int) (((double) _current_byte / (double) _file_length) * 100);

                        if (_progress_temp != _progress && _progress < 100) {

                            _progress_temp = _progress;

                            _listener.get_progress(_progress);

                            network_log(_request_log, "下载文件中,当前下载进度: " + _progress + "%");
                        }

                        _out.flush();
                    }

                    _response_model._response_code = 200;

                    _response_model._response_content = _file.getAbsolutePath();

                    _out.close();

                    _in.close();

                    _out_stream.close();

                    _input_stream.close();
                } else {

                    _response_model._response_error = true;

                    _response_model._response_error_msg = "创建文件失败,请检查文件名是否合法, 例如 aaa.txt, nnn.zip, ccc.jpg";
                }
            } else {

                _response_model._response_error = true;

                _response_model._response_error_msg = "创建文件夹失败,请检查是否有sd卡,是否有文件写入权限,是否路径为空...";
            }

        } catch (Exception ex) {

            _response_model._response_error = true;

            _response_model._response_error_msg = get_exception(ex);
        }

        network_log(_request_log, "                                                  ");

        network_log(_request_log, "**************************************************");

        network_log(_request_log, "                                                  ");

        network_log(_request_log, "request_url: " + _request_url);

        network_log(_request_log, "response_error: " + _response_model._response_error);

        network_log(_request_log, "response_error_msg: " + _response_model._response_error_msg);

        network_log(_request_log, "response_code: " + _response_model._response_code);

        network_log(_request_log, "response_content: " + _response_model._response_content);

        if (!_response_model._response_error) {

            _listener.get_progress(100);

            network_log(_request_log, "下载文件中,当前下载进度: " + 100 + "%");
        }

        return _response_model;
    }


    /**
     * 读取异常信息
     *
     * @param _ex
     * @return
     */
    private String get_exception(Exception _ex) {

        ByteArrayOutputStream _out_stream = new ByteArrayOutputStream();

        PrintStream _print_stream = new PrintStream(_out_stream);

        _ex.printStackTrace(_print_stream);

        String _result = new String(_out_stream.toByteArray());

        try {

            _out_stream.close();

        } catch (Exception e) {
        }

        return _result;
    }


    /**
     * 下载实体类
     */
    public class HttpDownloadModel {

        public String _response_content = null;

        public int _response_code = 0;

        public boolean _response_error = false;

        public String _response_error_msg = "服务器请求接口数据正常....";
    }


    /**
     * 下载进度回调
     */
    public interface HttpDownloadListener {

        public void get_progress(int _progress);
    }
}
