package qqkj.qqkj_library.desede;

import android.util.Base64;

import java.security.Key;

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


    public static DesedeUtil getIns() {

        if (_desede_util == null) {

            _desede_util = new DesedeUtil();
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

        return _3des_result;
    }


    /**
     * byte数组转base64
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
