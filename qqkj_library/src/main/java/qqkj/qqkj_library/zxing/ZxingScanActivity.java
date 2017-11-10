package qqkj.qqkj_library.zxing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.zhy.autolayout.AutoLayoutActivity;

import qqkj.qqkj_library.R;
import qqkj.qqkj_library.broadcast.BroadCastUtil;
import qqkj.qqkj_library.sensor.light.LightUtil;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/7.
 */

public abstract class ZxingScanActivity extends AutoLayoutActivity {

    /**
     * 扫码识别管理类
     */
    protected CaptureManager _capture;

    /**
     * 扫描识别自定义View
     */
    protected DecoratedBarcodeView _barcode_scanner_view;

    /**
     * 是否自动开启光线感应器
     * 如果开启,当光线暗时,自动打开闪光灯
     * 如果不开启,请在继承类中设置该属性为false
     */
    protected boolean _open_auto_light = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置布局文件
        _set_content_view();

        //获取DecoratedBarCodeView对象
        _barcode_scanner_view = _find_decorated_bar_code_view();

        //初始化View
        _init_view();

        //初始化扫描对象
        _set_capture_decode(savedInstanceState);

        //注册广播,接受二维码/条形码扫描结果
        _get_scan_br_result();

        //开启光线感应器,根据光线强度自动打开闪光灯
        if (_open_auto_light) {

            //打开闪光灯
            _get_scan_light();
        }
    }

    /**
     * 设置布局文件ID
     */
    public abstract void _set_content_view();


    /**
     * 初始化View
     */
    public abstract void _init_view();


    /**
     * 接收扫描结果
     */
    public abstract void _get_scan_result(String _scan_result);


    /**
     * 光线传感器回调
     *
     * @param _light_sensor 手机是否存在光线传感器
     * @param _light        true,表示有光线,false 表示光线暗
     */
    public abstract void _get_light_result(boolean _light_sensor, boolean _light);


    /**
     * 获取扫描View
     *
     * @return
     */
    public DecoratedBarcodeView _find_decorated_bar_code_view() {

        return (DecoratedBarcodeView) findViewById(R.id._barcode_view);
    }


    /**
     * 初始化扫描
     *
     * @param savedInstanceState
     */
    private void _set_capture_decode(@Nullable Bundle savedInstanceState) {

        _capture = new CaptureManager(this, _barcode_scanner_view);

        _capture.initializeFromIntent(getIntent(), savedInstanceState);

        _capture.decode();
    }

    /**
     * Activity启动,二维码/条形码识别启动
     */
    @Override
    protected void onResume() {

        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onResume();

        _capture.onResume();
    }

    /**
     * Activity暂停,二维码/条形码识别暂停
     */
    @Override
    protected void onPause() {

        super.onPause();

        _capture.onPause();
    }


    /**
     * Activity销毁,二维码/条形码识别销毁
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();

        BroadCastUtil.getIns(this)._get_un_broadcast(_br);

        _get_scan_light_close();

        //关闭界面,同时要清理光感传感器
        LightUtil.getIns(getBaseContext())._destroy_light();

        _capture.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        _capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        _capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return _barcode_scanner_view.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    /**
     * 返回扫描管理对象,继承类可以根据需求自由操作
     *
     * @return
     */
    public CaptureManager _get_captruemanager() {

        return _capture;
    }


    /**
     * 暂停扫描
     */
    public void _get_scan_pause() {

        _capture.onPause();
    }

    /**
     * 恢复扫描
     */
    public void _get_scan_resume() {

        _capture.onResume();
    }


    /**
     * 打开闪光灯
     */
    public void _get_scan_light_open() {

        _find_decorated_bar_code_view().setTorchOn();
    }


    /**
     * 关闭闪光灯
     */
    public void _get_scan_light_close() {

        _find_decorated_bar_code_view().setTorchOff();
    }


    /**
     * 注册广播,用于接收扫描返回值
     */
    public void _get_scan_br_result() {

        BroadCastUtil.getIns(this)._get_broadcast("_get_scan_result", _br);
    }


    /**
     * 开启光线感应器,根据光线强度选择是否打开闪光灯
     */
    public void _get_scan_light() {

        boolean _light = LightUtil.getIns(getBaseContext())._get_light(new LightUtil.LightListener() {
            @Override
            public void get_light(boolean _light) {

                //_light = true 表示光线OK, _light = false 表示光线暗
                if (_light) {

                    _get_light_result(true, true);
                } else {

                    _get_light_result(true, false);
                }
            }
        });

        //手机没有传感器
        if (!_light) {

            _get_light_result(false, false);
        }
    }


    /**
     * 该广播用于接收扫描返回值
     */
    private BroadcastReceiver _br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String _scan_result = intent.getStringExtra("_scan_result");

            //将扫描结果通过函数返回给继承类
            _get_scan_result(_scan_result);
        }
    };
}
