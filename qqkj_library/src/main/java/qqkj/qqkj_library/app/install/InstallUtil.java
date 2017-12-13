package qqkj.qqkj_library.app.install;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.support.v4.BuildConfig;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/10.
 */

public class InstallUtil {

    private static InstallUtil _install_util = null;

    private Intent _intent = null;

    private Context _context = null;


    public InstallUtil(Context _context) {

        this._context = _context;
    }


    public static InstallUtil getIns(Context _context) {

        if (_install_util == null) {

            _install_util = new InstallUtil(_context);
        }

        return _install_util;
    }


    /**
     * 安装APK
     *
     * @param _apk_path APK文件存放路径
     */
    public boolean _get_install_apk(String _apk_path, String _application_id) {

        //如果路径为空,返回false
        if (null == _apk_path) {

            return false;
        }

        //如果文件不存在,返回false
        if (!new File(_apk_path).exists()) {

            return false;
        }

        _intent = new Intent(Intent.ACTION_VIEW);

        Uri _uri = null;

        if (Build.VERSION.SDK_INT >= 24) {

            _uri = FileProvider.getUriForFile(_context, _application_id + ".provider", new File(_apk_path));

            _intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else {

            _uri = Uri.fromFile(new File(_apk_path));
        }

        _intent.setDataAndType(_uri, "application/vnd.android.package-archive");

        _context.startActivity(_intent);

        //安装完成后显示打开按钮,如果不加,安装后没有打开按钮
        Process.killProcess(Process.myPid());

        return true;
    }
}
