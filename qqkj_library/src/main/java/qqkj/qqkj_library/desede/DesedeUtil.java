package qqkj.qqkj_library.desede;

import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 这个类是用来做甚的
 * 3DES 加密字符串
 * <p>
 * Created by 陈二狗 on 2017/9/20.
 */

public class DesedeUtil {

    private static DesedeUtil _desede_util = null;

    private static final String _des_name = "desede";

    private static final String _des_cipher = "desede/CBC/PKCS5Padding";

    private static final char[] _legal_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    private Map<String, Object> _base_param = new HashMap<>();

    private JSONObject _json_object = null;

    private List<String> _list_param = null;


    public static DesedeUtil getIns() {

        if (_desede_util == null) {

            _desede_util = new DesedeUtil();
        }

        return _desede_util;
    }


    /**
     * 设置3DES默认参数
     *
     * @param _param_name
     * @param _param_value
     * @return
     */
    public DesedeUtil _init_3des_param(String[] _param_name, String[] _param_value) {

        _list_param = new ArrayList<>();

        if (_param_name.length > 0 && _param_value.length > 0 && _param_name.length == _param_value.length) {

            for (int _i = 0; _i < _param_name.length; _i++) {

                _list_param.add(_param_name[_i]);

                _base_param.put(_param_name[_i], _param_value[_i]);
            }
        }

        return _desede_util;
    }


    /**
     * @param _string_param
     * @param _encode       //与服务器端统一编码
     * @param _secret_key   //安全key
     * @param _iv           //向量值
     * @return
     */
    public String get_3des(String _string_param, String _encode, String _secret_key, String _iv) throws Exception {

        String _3des_result = null;

        Key _des_key = null;

        DESedeKeySpec _spec = new DESedeKeySpec(_secret_key.getBytes());

        SecretKeyFactory _key_factory = SecretKeyFactory.getInstance(_des_name);

        _des_key = _key_factory.generateSecret(_spec);

        Cipher _cipher = Cipher.getInstance(_des_cipher);

        IvParameterSpec _ips = new IvParameterSpec(_iv.getBytes());

        _cipher.init(Cipher.ENCRYPT_MODE, _des_key, _ips);

        byte[] _encrypt_data = _cipher.doFinal(_string_param.getBytes(_encode));

        _3des_result = encode(_encrypt_data);

        if (_base_param.size() > 0) {

            _base_param.put(_list_param.get(_list_param.size() - 1), _3des_result);

            _json_object = new JSONObject(_base_param);

            return _json_object.toString();
        }

        return _3des_result;
    }


    /**
     * byte数组转base64
     *
     * @param _data
     * @return
     */
    private String encode(byte[] _data) {

        int _start = 0;

        int _length = _data.length;

        StringBuffer _string_buffer = new StringBuffer(_data.length * 3 / 2);

        int _end = _length - 3;

        int _i = _start;

        int _n = 0;

        while (_i <= _end) {

            int _d = ((((int) _data[_i]) & 0x0ff) << 16) | ((((int) _data[_i + 1]) & 0x0ff) << 8) | (((int) _data[_i + 2]) & 0x0ff);

            _string_buffer.append(_legal_chars[(_d >> 18) & 63]);

            _string_buffer.append(_legal_chars[(_d >> 12) & 63]);

            _string_buffer.append(_legal_chars[(_d >> 6) & 63]);

            _string_buffer.append(_legal_chars[_d & 63]);

            _i += 3;

            if (_n++ >= 14) {

                _n = 0;

                _string_buffer.append(" ");
            }
        }

        if (_i == _start + _length - 2) {

            int _d = ((((int) _data[_i]) & 0x0ff) << 16) | ((((int) _data[_i + 1]) & 255) << 8);

            _string_buffer.append(_legal_chars[(_d >> 18) & 63]);

            _string_buffer.append(_legal_chars[(_d >> 12) & 63]);

            _string_buffer.append(_legal_chars[(_d >> 6) & 63]);

            _string_buffer.append("=");

        } else if (_i == _start + _length - 1) {

            int _d = (((int) _data[_i]) & 0x0ff) << 16;

            _string_buffer.append(_legal_chars[(_d >> 18) & 63]);

            _string_buffer.append(_legal_chars[(_d >> 12) & 63]);

            _string_buffer.append("==");
        }

        return _string_buffer.toString();
    }
}
