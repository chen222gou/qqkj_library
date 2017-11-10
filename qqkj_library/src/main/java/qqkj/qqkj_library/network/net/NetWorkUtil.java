package qqkj.qqkj_library.network.net;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/10.
 */

public class NetWorkUtil {


    private static NetWorkUtil _net_work_util = null;


    public static NetWorkUtil getIns(){

        if(_net_work_util == null){

            _net_work_util = new NetWorkUtil();
        }

        return _net_work_util;
    }


    /**
     * 检查网络是否可用,通过ping网址的方式
     * @return
     */
    public boolean _get_network_state() {

        //默认初始化状态是不可用
        boolean _net_work_state = false;

        Process _progress = null;

        try {

            //通过ping网址的方式,查看网络是否可用,,,这里选择百度,,因为稳定
            _progress = Runtime.getRuntime().exec("ping -c 1 www.baidu.com");

            //获取ping后的返回值
            int _result = _progress.waitFor();

            _net_work_state = (_result == 0);

        }catch (Exception e){

        }

        return _net_work_state;
    }
}
