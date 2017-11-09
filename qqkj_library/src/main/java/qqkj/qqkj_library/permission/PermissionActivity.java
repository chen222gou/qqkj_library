package qqkj.qqkj_library.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhy.autolayout.AutoLayoutActivity;

/**
 * 这个类是用来做甚的
 * 权限封装类
 * <p>
 * Created by 陈二狗 on 2017/11/9.
 */

public abstract class PermissionActivity extends AutoLayoutActivity {

    /**
     * 权限集合
     */
    private String[] _permission = new String[]{};

    /**
     * 权限获取失败后是否跳转系统权限设置界面
     */
    private boolean _go_setting = true;

    /**
     * 初始化权限
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _init_permission();
    }


    /**
     * 1,接收子类中的请求权限数组
     * 2,请求权限
     */
    private void _init_permission() {

        //接收权限集合
        _permission = _set_permission_array();

        _go_setting = _set_permission_fail_to_setting();

        //请求权限

        if (_permission.length == 0) {

            _get_permission_fail("_set_permission_array()不能为空");
        } else {

            _req_permission();
        }
    }

    /**
     * 继承类传入权限数组
     *
     * @return
     */
    public abstract String[] _set_permission_array();

    /**
     * 请求权限失败后是否进入系统权限设置界面
     *
     * @return
     */
    public abstract boolean _set_permission_fail_to_setting();

    /**
     * 获取权限成功后进入该方法
     */
    public abstract void _get_permission_success();


    /**
     * 获取权限失败后,且设置_set_permission_fail_to_setting方法为 false 会进入该方法
     */
    public abstract void _get_permission_fail(String result);


    /**
     * 请求权限方法
     */
    public void _req_permission() {

        boolean _permission = PermissionUtil.getIns(this)._req_premission(_set_permission_array(), PermissionUtil.REQUEST_CODE);

        if (_permission) {

            _get_permission_success();
        }
    }


    /**
     * 请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case PermissionUtil.REQUEST_CODE:

                if (PermissionUtil.getIns(this)._req_premission_result(requestCode, permissions, grantResults, _go_setting)) {

                    _get_permission_success();
                } else {

                    _get_permission_fail("用户多次拒绝了该权限");
                }

                break;
            default:
                break;
        }
    }


    /**
     * 设置界面回调,设置_set_permission_fail_to_setting方法为 true 会进入该方法,否则只会走到 _get_permission_fail方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PermissionUtil.GO_SETTING_ACT:

                //从设置界面返回后,再次检查是否有权限
                _req_permission();

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
