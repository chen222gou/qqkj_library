package qqkj.qqkj_library.log;

import android.content.Context;
import com.xsj.crasheye.Crasheye;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/22.
 */

public class LogUtil{

    private static LogUtil _log_util = null;

    private static final String BASE_LOG_KEY = "761ff220";

    private static final String TANGDAO_LOG_KEY = "95a1af20";

    private static final String AQUACITY_LOG_KEY = "d73196d0";

    private static final String JIUWU_LOG_KEY = "f7ce6f80";

    private static final String LUCKY_LOG_KEY = "190f2950";

    private static final String QUANQUAN_LOG_KEY = "394e0380";

    private Context _context = null;


    private LogUtil(Context _context){

        this._context = _context;
    }

    public static LogUtil getIns(Context _context) {

        if (_log_util == null) {

            _log_util = new LogUtil(_context);
        }

        return _log_util;
    }


    /**
     * 开启监听LOG
     */
    public void get_base_log(boolean _start_log){

        if(_start_log){

            Crasheye.setChannelID(BASE_LOG_KEY);

            Crasheye.init(_context, BASE_LOG_KEY);
        }

    }


    public void get_tangdao_log(boolean _start_log){

        if(_start_log){

            Crasheye.setChannelID(TANGDAO_LOG_KEY);

            Crasheye.init(_context, TANGDAO_LOG_KEY);
        }

    }


    public void get_aquacity_log(boolean _start_log){

        if(_start_log){

            Crasheye.setChannelID(AQUACITY_LOG_KEY);

            Crasheye.init(_context, AQUACITY_LOG_KEY);
        }

    }


    public void get_luckey_log(boolean _start_log){

        if(_start_log){

            Crasheye.setChannelID(LUCKY_LOG_KEY);

            Crasheye.init(_context, LUCKY_LOG_KEY);
        }

    }


    public void get_jw_log(boolean _start_log){

        if(_start_log){

            Crasheye.setChannelID(JIUWU_LOG_KEY);

            Crasheye.init(_context, JIUWU_LOG_KEY);
        }

    }


    public void get_quanquan_log(boolean _start_log){

        if(_start_log){

            Crasheye.setChannelID(QUANQUAN_LOG_KEY);

            Crasheye.init(_context, QUANQUAN_LOG_KEY);
        }

    }
}
