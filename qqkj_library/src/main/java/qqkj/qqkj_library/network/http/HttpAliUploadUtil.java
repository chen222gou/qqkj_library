package qqkj.qqkj_library.network.http;

import android.content.Context;
import android.system.Os;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 阿里OSS上传
 * <p>
 * Created by 陈二狗 on 2017/11/13.
 */

public class HttpAliUploadUtil {


    private Context _context = null;

    /**
     * 阿里OSS上传客户端对象
     */
    private OSSClient _oss_client = null;

    /**
     * 当前上传文件个数,默认没有
     */
    private int _upload_index = 0;

    /**
     * 上传文件总个数
     */
    private int _upload_sum = 0;


    /**
     * 上传成功后文件路径集合
     */
    private List<String> _response_list = new ArrayList<>();


    private static HttpAliUploadUtil _http_aliupload_util = null;


    /**
     * 单例
     *
     * @param _context
     */
    public HttpAliUploadUtil(Context _context) {

        this._context = _context;

    }


    /**
     * 初始化获取上传对象
     *
     * @param _context
     * @return
     */
    public static HttpAliUploadUtil getIns(Context _context) {

        if (_http_aliupload_util == null) {

            _http_aliupload_util = new HttpAliUploadUtil(_context);
        }
        return _http_aliupload_util;
    }


    /**
     * 初始化阿里OSS配置
     *
     * @param _end_point
     * @param _ak
     * @parask
     */
    public OSSClient _get_init(String _end_point, String _ak, String _sk) {

        // 在移动端建议使用STS的方式初始化OSSClient，更多信息参考：[访问控制]
        OSSCredentialProvider _credential_provider = new OSSPlainTextAKSKCredentialProvider(_ak, _sk);

        ClientConfiguration _conf = new ClientConfiguration();

        // 连接超时，
        _conf.setConnectionTimeout(15 * 1000);

        // socket超时，默认15秒
        _conf.setSocketTimeout(15 * 1000);

        // 最大并发请求书，默认1个
        _conf.setMaxConcurrentRequest(1);

        // 失败后最大重试次数，默认1次
        _conf.setMaxErrorRetry(0);

        _oss_client = new OSSClient(_context, _end_point, _credential_provider, _conf);

        return _oss_client;
    }


    /**
     * 阿里OSS上传文件
     *
     * @param _bucket_name
     * @param _server_file_path
     * @param _file_path
     * @param _listener
     */
    public void _get_upload_file(final OSSClient _oss_client, final String _bucket_name, final String _server_file_path,

                                 final String _last_file_name, final String[] _file_path, final HttpAliUploadListener _listener) {

        //用于检查每个文件路径是否正确
        boolean _check_file_success = true;

        //用于存储上传后的文件路径
        final List<String> _response_files = new ArrayList<>();

        //检查文件路径是否为空
        if (_file_path == null || _file_path.length == 0) {

            _listener._upload_fail("文件路径不能为空...");

            return;
        }

        //检查每个文件是否存在
        for (int _k = 0; _k < _file_path.length; _k++) {

            if (!new File(_file_path[_k]).exists()) {

                _listener._upload_fail("第" + (_k + 1) + "个文件路径错误,找不到该文件...");

                _check_file_success = false;

                break;
            }
        }

        //如果不存在,直接return
        if (!_check_file_success) {

            return;
        }

        //获取上传文件总数
        _upload_sum = _file_path.length;

        //表示第一张开始上传
        _upload_index = 1;

        _listener._upload_index(1);

        //如果存在,上传多张文件
        _get_upload(_oss_client, _bucket_name, _server_file_path, _last_file_name, _upload_index, _file_path, _listener);
    }


    public void _get_upload(final OSSClient _oss_client, final String _buck_name, final String _server_file_path, final String _last_name,
                            final int _index, final String[] _upload_file_path, final HttpAliUploadListener _listener) {


        String _file_path = _server_file_path + "/" + System.currentTimeMillis() + "." + _last_name;

        PutObjectRequest _put_object_request = new PutObjectRequest(_buck_name, _file_path, _upload_file_path[_index - 1]);

        _put_object_request.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

                //计算当前进度
                int _progress = (int) (((double) currentSize / (double) totalSize) * 100);

                //回调进度
                _listener._upload_progress(_progress);
            }
        });

        OSSAsyncTask _task = _oss_client.asyncPutObject(_put_object_request, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {


                _response_list.add(request.getObjectKey());

                if (_upload_index < _upload_file_path.length) {

                    //表示还有图片需要上传

                    _upload_index = _upload_index + 1;

                    _listener._upload_index(_upload_index);

                    _get_upload(_oss_client,_buck_name, _server_file_path, _last_name, _upload_index, _upload_file_path, _listener);
                } else {

                    //表示这是最后一张上传成功了,发送回调
                    _listener._upload_success(_response_list);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    _listener._upload_fail("请检查您的网络...");
                }
                if (serviceException != null) {
                    // 服务异常
                    _listener._upload_fail("服务器异常...");
                }
            }
        });
    }


    public interface HttpAliUploadListener {

        public void _upload_progress(int _progress);

        public void _upload_success(List<String> _file_path);

        public void _upload_fail(String _fail_message);

        public void _upload_index(int _index);
    }
}

