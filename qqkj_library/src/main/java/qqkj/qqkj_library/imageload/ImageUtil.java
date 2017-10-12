package qqkj.qqkj_library.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 图片处理
 * <p>
 * Created by 陈二狗 on 2017/10/12.
 */

public class ImageUtil {

    private static ImageUtil _image_util = null;

    private Context _context = null;

    private DisplayImageOptions.Builder _image_build_none, _image_build_round, _image_build_circle;

    private ImageLoader _image_loader = null;

    private ImageLoaderConfiguration _image_loader_config = null;

    private ImageLoaderConfiguration.Builder _image_config_builder = null;

    private File _image_disk_cache_dir = null;

    private ImageLoadingListener _animation_first_listener = null;

    private DisplayImageOptions _display_image_options_none, _display_image_options_round, _display_image_options_circle;


    public static ImageUtil getIns(){

        if(null == _image_util){

            _image_util = new ImageUtil();
        }

        return _image_util;
    }


    /**
     * 初始化
     */
    public void _get_init(Context _context){

        _image_loader = ImageLoader.getInstance();

        _image_disk_cache_dir = StorageUtils.getCacheDirectory(_context,true);

        _image_config_builder = new ImageLoaderConfiguration.Builder(_context);

        //设置图片下载线程池大小
        _image_config_builder.threadPoolSize(8);

        //设置线程优先级 第四级别
        _image_config_builder.threadPriority(Thread.MIN_PRIORITY + 3);

        //设置二级缓存方式
        _image_config_builder.memoryCache(new WeakMemoryCache());

        //设置一级缓存方式
        _image_config_builder.diskCache(new UnlimitedDiskCache(_image_disk_cache_dir));

        //设置默认加载图片选项
        _image_config_builder.defaultDisplayImageOptions(DisplayImageOptions.createSimple());

        _image_loader_config = _image_config_builder.build();

        //初始化ImageLoader框架 这一步已经可以使用,下面配置是根据不同的项目进行配置
        _image_loader.init(_image_loader_config);

    }


    /**
     * 加载正常图片
     */
    private void _image_none_init(){

        //设置加载图片选项配置
        _image_build_none = new DisplayImageOptions.Builder();

        //设置二级缓存
        _image_build_none.cacheInMemory(true);

        //设置一级缓存
        _image_build_none.cacheOnDisk(true);

        //设置自动识别图片的方向
        _image_build_none.considerExifParams(true);

        //设置图像将完全按比例缩小的目标大小
        _image_build_none.imageScaleType(ImageScaleType.EXACTLY);

        //设置压缩格式
        _image_build_none.bitmapConfig(Bitmap.Config.ARGB_8888);

        //设置默认加载图片时显示的背景
        _image_build_none.showImageOnLoading(Color.parseColor("#00000000"));

        //设置默认加载失败的背景
        _image_build_none.showImageOnFail(Color.parseColor("#00000000"));

        //设置图片路径为空时加载的背景
        _image_build_none.showImageForEmptyUri(Color.parseColor("#00000000"));

        _display_image_options_none = _image_build_none.build();
    }



    /**
     * 加载带圆角图片
     */
    private void _image_round_init(int _round){

        //设置加载图片选项配置
        _image_build_round = new DisplayImageOptions.Builder();

        //设置二级缓存
        _image_build_round.cacheInMemory(true);

        //设置一级缓存
        _image_build_round.cacheOnDisk(true);

        //设置自动识别图片的方向
        _image_build_round.considerExifParams(true);

        //设置图像将完全按比例缩小的目标大小
        _image_build_round.imageScaleType(ImageScaleType.EXACTLY);

        //设置压缩格式
        _image_build_round.bitmapConfig(Bitmap.Config.ARGB_8888);

        //设置默认加载图片时显示的背景
        _image_build_round.showImageOnLoading(Color.parseColor("#00000000"));

        //设置默认加载失败的背景
        _image_build_round.showImageOnFail(Color.parseColor("#00000000"));

        //设置图片路径为空时加载的背景
        _image_build_round.showImageForEmptyUri(Color.parseColor("#00000000"));

        //设置图片圆角大小
        _image_build_round.displayer(new RoundedBitmapDisplayer(_round));

        _display_image_options_round = _image_build_round.build();
    }


    /**
     * 加载圆形
     */
    private void _image_circle_init(){

        //设置加载图片选项配置
        _image_build_circle = new DisplayImageOptions.Builder();

        //设置二级缓存
        _image_build_circle.cacheInMemory(true);

        //设置一级缓存
        _image_build_circle.cacheOnDisk(true);

        //设置自动识别图片的方向
        _image_build_circle.considerExifParams(true);

        //设置图像将完全按比例缩小的目标大小
        _image_build_circle.imageScaleType(ImageScaleType.EXACTLY);

        //设置压缩格式
        _image_build_circle.bitmapConfig(Bitmap.Config.ARGB_8888);

        //设置默认加载图片时显示的背景
        _image_build_circle.showImageOnLoading(Color.parseColor("#00000000"));

        //设置默认加载失败的背景
        _image_build_circle.showImageOnFail(Color.parseColor("#00000000"));

        //设置图片路径为空时加载的背景
        _image_build_circle.showImageForEmptyUri(Color.parseColor("#00000000"));

        //设置图片为圆形
        _image_build_circle.displayer(new CircleBitmapDisplayer());

        _display_image_options_circle= _image_build_circle.build();
    }


    /**
     * 设置默认图片
     * @param _defalut
     */
    public void _set_default_image(int _type, int... _defalut){


        DisplayImageOptions.Builder _temp_build = null;

        DisplayImageOptions _temp_options = null;

        if(_type == 1){

            _temp_build = _image_build_none;

            _temp_options = _display_image_options_none;
        }else if(_type == 2){

            _temp_build = _image_build_round;

            _temp_options = _display_image_options_round;
        }else if(_type ==3){

            _temp_build = _image_build_circle;

            _temp_options = _display_image_options_circle;
        }

        if(null != _temp_build){

            if(_defalut.length == 1){

                _temp_build.showImageForEmptyUri(_defalut[0]);

                _temp_build.showImageOnFail(_defalut[0]);

                _temp_build.showImageOnLoading(_defalut[0]);
            }else if(_defalut.length == 2){

                _temp_build.showImageForEmptyUri(_defalut[0]);

                _temp_build.showImageOnFail(_defalut[0]);

                _temp_build.showImageOnLoading(_defalut[1]);
            }else if(_defalut.length == 3){

                _temp_build.showImageForEmptyUri(_defalut[0]);

                _temp_build.showImageOnFail(_defalut[1]);

                _temp_build.showImageOnLoading(_defalut[2]);
            }


            _temp_options = _temp_build.build();
        }
    }


    /**
     * (网络)加载正常图片
     * @param _url
     * @param _image
     * @param _default
     */
    public void _load_http_image_none(String _url, ImageView _image, int... _default){

        if(_default.length > 0){

            _set_default_image(1, _default);
        }

        _image_none_init();

        _animation_first_listener = new AnimateFirstDisplayListener();

        _image_loader.displayImage(_url, _image, _display_image_options_none, _animation_first_listener);
    }


    /**
     * (网络)加载圆角图片
     * @param _url
     * @param _image
     * @param _default
     */
    public void _load_http_image_round(String _url, ImageView _image, int _round, int... _default){

        if(_default.length > 0){

            _set_default_image(2, _default);
        }

        _image_round_init(_round);

        _animation_first_listener = new AnimateFirstDisplayListener();

        _image_loader.displayImage(_url, _image, _display_image_options_round, _animation_first_listener);
    }


    /**
     * (网络)加载圆形图片
     * @param _url
     * @param _image
     * @param _default
     */
    public void _load_http_image_circle(String _url, ImageView _image, int... _default){

        if(_default.length > 0){

            _set_default_image(3, _default);
        }

        _image_circle_init();

        _animation_first_listener = new AnimateFirstDisplayListener();

        _image_loader.displayImage(_url, _image, _display_image_options_circle, _animation_first_listener);
    }


    /**
     * (本地)加载图片
     * @param _url
     * @param _image
     * @param _default
     */
    public void _load_sdcard_image_none(String _url, ImageView _image, int... _default){

        if(_default.length > 0){

            _set_default_image(1, _default);
        }

        _image_none_init();

        _animation_first_listener = new AnimateFirstDisplayListener();

        _image_loader.displayImage(ImageDownloader.Scheme.FILE.wrap(_url), _image, _display_image_options_none, _animation_first_listener);
    }


    /**
     * (本地)加载圆角图片
     * @param _url
     * @param _image
     * @param _default
     */
    public void _load_sdcard_image_round(String _url, ImageView _image, int _round, int... _default){

        if(_default.length > 0){

            _set_default_image(2, _default);
        }

        _image_round_init(_round);

        _animation_first_listener = new AnimateFirstDisplayListener();

        _image_loader.displayImage(ImageDownloader.Scheme.FILE.wrap(_url), _image, _display_image_options_round, _animation_first_listener);
    }


    /**
     * (本地)加载圆形图片
     * @param _url
     * @param _image
     * @param _default
     */
    public void _load_sdcard_image_circle(String _url, ImageView _image, int... _default){

        if(_default.length > 0){

            _set_default_image(3, _default);
        }

        _image_circle_init();

        _animation_first_listener = new AnimateFirstDisplayListener();

        _image_loader.displayImage(ImageDownloader.Scheme.FILE.wrap(_url), _image, _display_image_options_circle, _animation_first_listener);
    }



    /**
     * 第一次加载监听
     */
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> _display_image = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String _image_uri, View _view, Bitmap _load_image) {

            if (_load_image != null) {

                ImageView _image = (ImageView) _view;

                boolean _first = !_display_image.contains(_image_uri);

                if (_first) {

                    FadeInBitmapDisplayer.animate(_image, 500);

                    _display_image.add(_image_uri);
                }
            }
        }
    }
}
