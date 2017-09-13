package qqkj.qqkj_library.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 本地数据存储
 * <p>
 * Created by 陈二狗 on 2017/8/30.
 */

public class LocalDataUtil {

    private static LocalDataUtil _local_data_util = null;

    private Context _context = null;

    private SharedPreferences _sharepreferences = null;


    public LocalDataUtil(Context _context) {

        this._context = _context;
    }


    public static LocalDataUtil getIns(Context _context) {

        if (null == _local_data_util) {

            _local_data_util = new LocalDataUtil(_context);
        }

        return _local_data_util;
    }


    public static LocalDataUtil getNew(Context _context){

        return new LocalDataUtil(_context);
    }


    /**
     * 存储对象到本地
     *
     * @param _object
     * @param _file_key
     * @param _key
     * @return
     */
    public boolean _save_object(Object _object, String _file_key, String _key) {

        try {

            if (_object instanceof Serializable) {

                if (null == _sharepreferences) {

                    _sharepreferences = _context.getSharedPreferences(_file_key, Context.MODE_PRIVATE);

                    SharedPreferences.Editor _editor = _sharepreferences.edit();

                    String _result = _object_to_string(_object);

                    _editor.putString(_key, _result);

                    _editor.apply();

                    return true;
                }
            }

        }catch (Exception e){

            e.printStackTrace();
        }

        return false;
    }


    /**
     * 获取存储的本地对象
     * @param _file_key
     * @param _key
     * @return
     */
    public Object _get_object(String _file_key, String _key){

        _sharepreferences = _context.getSharedPreferences(_file_key, Context.MODE_PRIVATE);

        String _result = _sharepreferences.getString(_key, null);

        if(null != _result){

            Object _object = _string_to_object(_result);

            return _object;
        }

        return null;
    }


    /**
     * 存储单个字符到本地
     *
     * @param _value
     * @param _file_key
     * @param _key
     * @return
     */
    public boolean _save_string(String _value, String _file_key, String _key) {

        try {

            if (null == _sharepreferences) {

                _sharepreferences = _context.getSharedPreferences(_file_key, Context.MODE_PRIVATE);
            }

            SharedPreferences.Editor _editor = _sharepreferences.edit();

            _editor.putString(_key, _value);

            _editor.apply();

            return true;

        }catch (Exception e){

            e.printStackTrace();
        }

        return false;
    }


    /**
     * 获取存储的本地字符串
     * @param _file_key
     * @param _key
     * @return
     */
    public String _get_string(String _file_key, String _key){

        _sharepreferences = _context.getSharedPreferences(_file_key, Context.MODE_PRIVATE);

        String _result = _sharepreferences.getString(_key, null);

        if(null != _result){

            return _result;
        }

        return null;
    }


    /**
     * 把Object转成String类型
     *
     * @param _object
     */
    private String _object_to_string(Object _object) {

        ByteArrayOutputStream _byte_out = new ByteArrayOutputStream();

        try {

            ObjectOutputStream _object_out = new ObjectOutputStream(_byte_out);

            _object_out.writeObject(_object);

            String _result = new String(Base64.encode(_byte_out.toByteArray(), Base64.DEFAULT));

            _object_out.close();

            _byte_out.close();

            return _result;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;

    }

    /**
     * 把String转成Object类型
     *
     * @param _result
     * @return
     */
    private Object _string_to_object(String _result) {

        byte[] _bytes = Base64.decode(_result.getBytes(), Base64.DEFAULT);

        ByteArrayInputStream _byte_in = new ByteArrayInputStream(_bytes);

        try {

            ObjectInputStream _object_in = new ObjectInputStream(_byte_in);

            Object _object = _object_in.readObject();

            _object_in.close();

            _byte_in.close();

            return _object;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}
