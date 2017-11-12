package qqkj.qqkj_library.app.storage;

import android.os.Environment;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/12.
 */

public class StorageUtil {

    private static StorageUtil _storage_util = null;


    public static StorageUtil getIns(){

        if(null == _storage_util){

            _storage_util = new StorageUtil();
        }

        return _storage_util;
    }


    /**
     * 获取内置存储卡路径
     * @return
     */
    public String _get_storage_path(){

        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
