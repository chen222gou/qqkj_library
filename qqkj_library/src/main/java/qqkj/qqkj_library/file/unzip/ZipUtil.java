package qqkj.qqkj_library.file.unzip;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/21.
 */

public class ZipUtil {

    private static ZipUtil _zip_util = null;

    private long _zip_length = 0l;

    private long _cur_length = 0l;

    private int _temp_progress = -1;

    private static final String LOG_TAG = "qqkj_frame";

    private String _un_file_dir = null;

    private boolean _do_unzip = false;


    public static ZipUtil getIns() {

        _zip_util = new ZipUtil();

        return _zip_util;
    }


    /**
     * 解压zip包
     *
     * @return
     */
    public boolean get_unzip(String _zip_file_path, String _unzip_path_name, boolean _zip_log, ZipListener _listener) {

        qqkj_log(_zip_log, "                                                  ");

        qqkj_log(_zip_log, "**************************************************");

        qqkj_log(_zip_log, "                                                  ");

        try {

            File _file = new File(_zip_file_path);

            if (!_file.exists()) {

                qqkj_log(_zip_log, "解压异常,错误信息: ZIP包 路径错误...");

                return false;
            }

            _zip_length = _file.length();

            String _root_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

            _un_file_dir = _root_dir + _unzip_path_name;

            File _un_dir_file = new File(_un_file_dir);

            if (!_un_dir_file.exists()) {

                boolean _create_dir = _un_dir_file.mkdir();

                if (!_create_dir) {

                    qqkj_log(_zip_log, "解压异常,错误信息: 缺少文件读取和写入权限...");

                    return false;
                }
            }

            String _entry_name = null;

            FileInputStream _file_input_stream = new FileInputStream(_file);

            ZipInputStream _zip_input_stream = new ZipInputStream(new BufferedInputStream(_file_input_stream));

            ZipEntry _zip_entry = null;

            while ((_zip_entry = _zip_input_stream.getNextEntry()) != null) {

                _do_unzip = true;   //标记该压缩包不为空,并且可以解析

                int _count = 0;

                byte[] _bytes = new byte[1024];

                _entry_name = _zip_entry.getName();

                File _entry_file = new File(_un_file_dir + File.separator + _entry_name);

                File _entry_dir = new File(_entry_file.getParent());

                if (!_entry_dir.exists()) {

                    boolean _create = _entry_dir.mkdir();

                    if (!_create) {

                        qqkj_log(_zip_log, "解压异常,错误信息: 缺少文件读取和写入权限...");

                        return false;
                    }
                }

                FileOutputStream _file_out_stream = new FileOutputStream(_entry_file);

                BufferedOutputStream _buffer_out_stream = new BufferedOutputStream(_file_out_stream, 1024);

                while ((_count = _zip_input_stream.read(_bytes, 0, 1024)) != -1) {

                    _buffer_out_stream.write(_bytes, 0, _count);

                    _cur_length = _cur_length + 1024;

                    if (_cur_length > _zip_length) {

                        _cur_length = _zip_length;
                    }

                    int _progress = (int) (((double) _cur_length / (double) _zip_length) * 100);

                    if (_temp_progress != _progress && _progress < 100) {

                        _listener.get_progress(_progress);

                        _temp_progress = _progress;

                        qqkj_log(_zip_log, "解压进度: " + _progress + "%");
                    }
                }

                _buffer_out_stream.flush();

                _buffer_out_stream.close();
            }

            _zip_input_stream.close();

            _listener.get_progress(100);

        } catch (Exception e) {

            qqkj_log(_zip_log, "解压异常,错误信息: ");

            qqkj_log(_zip_log, get_exception(e));

            return false;
        }

        if(_do_unzip){

            qqkj_log(_zip_log, "解压进度: 100%");

            qqkj_log(_zip_log, "解压成功,解压位置: " + _un_file_dir);

            return true;
        }else{

            qqkj_log(_zip_log, "解压异常,错误信息: zip包为空,或者该文件不是zip包格式...");

            return false;
        }
    }


    /**
     * 日志
     *
     * @param _show_log
     * @param _log
     */
    private void qqkj_log(boolean _show_log, String _log) {

        if (_show_log) {

            Log.e(LOG_TAG, _log);
        }
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
     * 解压进度回调
     */
    public interface ZipListener {

        public void get_progress(int _progress);
    }
}
