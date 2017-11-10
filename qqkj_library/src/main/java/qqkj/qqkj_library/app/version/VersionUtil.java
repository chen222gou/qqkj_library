package qqkj.qqkj_library.app.version;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/10.
 */

public class VersionUtil {


    private static VersionUtil _version_util = null;

    private Context _context = null;


    public VersionUtil(Context _context) {

        this._context = _context;
    }

    public static VersionUtil getIns(Context _context) {

        return new VersionUtil(_context);
    }


    /**
     * 获取APP版本相关信息(版本号,版本名称,包名)
     * @return
     */
    public VersionModel _get_version() {

        VersionModel _version_model = null;

        try {
            PackageManager _package_manager = _context.getPackageManager();

            PackageInfo _package_info = _package_manager.getPackageInfo(_context.getPackageName(), 0);

            //获取版本号
            _version_model._version_code = _package_info.versionCode + "";

            //获取版本名称
            _version_model._version_name = _package_info.versionName;

            //获取包名
            _version_model._version_code = _package_info.packageName;

        } catch (Exception e) {

        }

        return _version_model;
    }



}
