package qqkj.qqkj_library.permission;

import android.content.Context;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/8/31.
 */

public class PremissionUtil {

    private static PremissionUtil _premission = null;

    private Context _context;

    public PremissionUtil(Context _context){

        this._context = _context;
    }

    public static PremissionUtil getIns(Context _context){

        if(null == _premission){

            _premission = new PremissionUtil(_context);
        }

        return _premission;
    }

    public void req_premission(){}
}
