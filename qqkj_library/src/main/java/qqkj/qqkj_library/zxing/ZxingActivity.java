package qqkj.qqkj_library.zxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import qqkj.qqkj_library.toast.ToastUtil;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/7.
 */

public class ZxingActivity extends AppCompatActivity{

    /**
     * 第三方页面传递参数集合
     */
    private Bundle _bundle = null;

    /**
     * 第三方参数类反射地址
     */
    private String _class_name = null;

    /**
     * 第三方参数反射类
     */
    private Class _class = null;

    /**
     * 用于Zxing初始化
     */
    private IntentIntegrator _intent_integrator = null;


    /**
     * 创建界面
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //接收第三方参数
        _get_intent();

        //初始化Zxing功能模块,一定要在接收第三方参数后,再调用该方法,否则反射会出现错误
        _init_scan();
    }


    /**
     * 接收第三方参数
     */
    private void _get_intent(){

        //接收第三方传递参数集合
        _bundle = getIntent().getExtras();

        //接收第三方传递反射类的全地址名称参数
        _class_name = _bundle.getString("_scan_class_name");

        try {

            //反射二维码扫描类,用于Zxing框架参数初始化
            _class = Class.forName(_class_name);

        }catch (Exception e){
        }

        if(null == _class){

            //参数传递错误时,提示,且关闭该界面
            ToastUtil.getIns(this).get_toast_short("_scan_class_name 参数错误...");
            finish();
        }
    }


    /**
     * 初始化Zxing功能
     */
    private void _init_scan(){

        _intent_integrator = new IntentIntegrator(this);

        //设置第三方传递的扫描识别二维码界面
        _intent_integrator.setCaptureActivity(_class);

        _intent_integrator.setBeepEnabled(false);

        _intent_integrator.setOrientationLocked(false);

        _intent_integrator.initiateScan();
    }


    /**
     * 扫描二维码/条形码结果回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //接收扫描结果回调
        IntentResult _result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(_result != null){

            //根据不同的返回值,发送不同的广播
            if(_result.getContents() == null){

                sendBroadcast(new Intent("_get_scan_result").putExtra("_scan_result",""));

            }else{

                sendBroadcast(new Intent("_get_scan_result").putExtra("_scan_result",_result.getContents()));
            }
        }else{

            super.onActivityResult(requestCode, resultCode, data);
        }

        finish();
    }

}
