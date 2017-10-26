package qqkj.qqkj_library.view.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import qqkj.qqkj_library.R;


/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/10/26.
 */

public class WebFragment extends Fragment {

    public WebView _web_view = null;

    public WebSettings _web_setting = null;

    public View _group_view = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        _group_view = inflater.inflate(R.layout.webview_layout,container);

        return _group_view;
    }


    /**
     * 设置webview路径
     * @param _web_url  请求路径
     * @param _load_local  在浏览器中打开还是在view中打开
     */
    public void set_web_url(final String _web_url, final String _web_error_url, boolean _load_local){

        if(null == _group_view){

            return;
        }

        _web_view = (WebView) _group_view.findViewById(R.id.qqkj_web_view);

        //添加websetting设置
        set_web_setting();

        if(_load_local){

            //在webview中打开网页
            _web_view.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                    //使用重定向
                    return false;
                }
            });
        }

        if(null != _web_error_url){

            //加载错误网页
            _web_view.setWebViewClient(new WebViewClient(){

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                    _web_view.loadUrl(_web_error_url);
                }
            });
        }

        _web_view.loadUrl(_web_url);
    }


    public boolean onKeyDown(int _key_code, KeyEvent _key_event){

        if(_key_code == _key_event.KEYCODE_BACK && _web_view.canGoBack()){

            _web_view.goBack();
        }else{

            getActivity().finish();
        }

        return true;
    }


    /**
     * 设置WebView属性
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void set_web_setting(){

        _web_setting = _web_view.getSettings();

        // 支持javascript
        _web_setting.setJavaScriptEnabled(true);

        // 支持使用localStorage(H5页面的支持)
        _web_setting.setDomStorageEnabled(true);

        // 支持数据库
        _web_setting.setDatabaseEnabled(true);

        // 设置可以支持缩放
        _web_setting.setUseWideViewPort(true);

        // 扩大比例的缩放
        _web_setting.setSupportZoom(true);

        _web_setting.setBuiltInZoomControls(true);

        // 隐藏缩放按钮
        _web_setting.setDisplayZoomControls(false);

        // 自适应屏幕
        _web_setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        _web_setting.setLoadWithOverviewMode(true);

        // 隐藏滚动条
        _web_view.setHorizontalScrollBarEnabled(false);

        _web_view.setVerticalScrollBarEnabled(false);

        _web_setting.setAllowFileAccess(true);

        _web_setting.setAllowFileAccessFromFileURLs(true);

        _web_setting.setCacheMode(WebSettings.LOAD_NO_CACHE);

    }


    @Override
    public void onDestroy() {

        //销毁webview
        if(null != _web_view){

            _web_view.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            _web_view.clearHistory();
            _web_view.destroy();
            _web_view = null;
        }
        super.onDestroy();
    }
}
