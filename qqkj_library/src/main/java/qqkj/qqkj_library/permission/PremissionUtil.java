package qqkj.qqkj_library.permission;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * 5.0以上获取权限封装
 * <p>
 * Created by 陈二狗 on 2017/8/31.
 */

public class PremissionUtil {

    private static PremissionUtil _premission = null;

    public static final int GO_SETTING_ACT = 999;    //回调code

    private Activity _context = null;

    private boolean _premission_result = true;

    private String _intent_uri = Settings.ACTION_SETTINGS;

    private PremissionUtil(Activity _context) {

        this._context = _context;
    }

    public static PremissionUtil getIns(Activity _context) {

        if (null == _premission) {

            _premission = new PremissionUtil(_context);
        }

        return _premission;
    }


    /**
     * 请求权限
     *
     * @param _premissions
     * @param _req_code
     */
    public void _req_premission(String[] _premissions, int _req_code) {

        boolean _is_req = false;

        for (int i = 0; i < _premissions.length; i++) {

            if (ContextCompat.checkSelfPermission(_context,
                    _premissions[i])
                    != PackageManager.PERMISSION_GRANTED) {

                _is_req = true;

                break;
            }
        }
        if (_is_req) {

            ActivityCompat.requestPermissions(_context, _premissions,
                    _req_code);
        }
    }


    /**
     * 请求权限后的回调
     *
     * @param _req_code
     * @param _premissions
     * @param _grant_results
     * @param _go_setting
     * @return
     */
    public boolean _req_premission_result(int _req_code, String[] _premissions, int[] _grant_results, boolean _go_setting) {

        if (_premissions == null || _grant_results == null) {

            return false;
        }

        for (int i = 0; i < _grant_results.length; i++) {

            if (_grant_results[i] == PackageManager.PERMISSION_DENIED) {

                if (!ActivityCompat.shouldShowRequestPermissionRationale(_context,

                        _premissions[i])) {

                    if (_go_setting) {

                        Toast.makeText(_context, "请手动设置您的权限...", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            _intent_uri = Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
                        } else {

                            _intent_uri = Settings.ACTION_SETTINGS;
                        }

                        Intent _intent = new Intent(_intent_uri);

                        Uri _uri = Uri.fromParts("package", _context.getPackageName(), null);

                        _intent.setData(_uri);

                        _context.startActivityForResult(_intent, GO_SETTING_ACT);
                    }

                    _premission_result = false;

                    break;
                } else {

                    _req_premission(_premissions, _req_code);
                }
            } else if (_grant_results[i] == PackageManager.PERMISSION_GRANTED) {

                _premission_result = true;
            }
        }

        return _premission_result;
    }
}