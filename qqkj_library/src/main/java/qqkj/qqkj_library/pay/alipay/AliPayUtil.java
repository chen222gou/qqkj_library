package qqkj.qqkj_library.pay.alipay;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;

import qqkj.qqkj_library.pay.alipay.model.PayResult;

/**
 * 支付宝支付
 * <p>
 * Created by 陈二狗 on 2017/11/1.
 */

public class AliPayUtil {

    private Activity _context = null;


    public AliPayUtil(Activity _context){

        this._context = _context;
    }


    public static AliPayUtil getIns(Activity _context){

        return new AliPayUtil(_context);
    }


    /**
     * 调用支付宝
     * @param _order_info
     * @return
     */
    public PayResult _get_alipay(String _order_info){

        PayTask _alipay = new PayTask(_context);

        PayResult _result = new PayResult(_alipay.payV2(_order_info, true));

        return _result;
    }

}
