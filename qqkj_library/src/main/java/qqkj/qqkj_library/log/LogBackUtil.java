//package qqkj.qqkj_library.log;
//
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Looper;
//import android.util.Log;
//import android.widget.Toast;
//
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
//
////import java.io.ByteArrayInputStream;
////import java.io.InputStream;
////import com.xsj.crasheye.Crasheye;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.lang.reflect.Field;
////import java.nio.charset.Charset;
//import java.nio.charset.Charset;
//import java.util.HashMap;
//import java.util.Map;
//
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.joran.JoranConfigurator;
//
////import ch.qos.logback.classic.LoggerContext;
////import ch.qos.logback.classic.joran.JoranConfigurator;
//
//
///**
// * 这个类是用来做甚的
// * <p>
// * Created by 陈二狗 on 2017/9/22.
// */
//
//public class LogBackUtil implements Thread.UncaughtExceptionHandler {
//
//    private static LogBackUtil _log_util = null;
//
//    private static final String LOG_TAG = "qqkj_frame";
//
//    private Thread.UncaughtExceptionHandler _uncaught_exception = null;
//
//    private Map<String, String> _log_info_map = new HashMap<String, String>();
//
//    private Context _context = null;
//
//
//    private LogBackUtil(Context _context){
//
//        this._context = _context;
//    }
//
//    public static LogBackUtil getIns(Context _context) {
//
//        if (_log_util == null) {
//
//            _log_util = new LogBackUtil(_context);
//        }
//
//        return _log_util;
//    }
//
//    /**
//     * 发送日志到服务端
//     *
//     * @param _log
//     */
//    private void send_log_to_server(String _log) {
//
//        Logger _logger = LoggerFactory.getLogger("南京圈圈科技Android_Log");
//
////        _logger.error(_log);
//
//        _logger.error("我是测试");
//
//        _logger.error("我是测试 我是测试");
//
//        _logger.error("我是测试\n");
//    }
//
//
//    /**
//     * 配置日志
//     */
//    public boolean set_log_configure(Context _context, String _host, String _port, boolean _start_exception_listener) {
//
//        String _config_xml = null;
//
//        if (_start_exception_listener) {
//
//            //拦截程序崩溃异常
//            this._context = _context;
//
//            _uncaught_exception = Thread.getDefaultUncaughtExceptionHandler();
//
//            Thread.setDefaultUncaughtExceptionHandler(this);
//
//        }
//
//        _config_xml =   "<configuration debug='false'>" +
//                        "  <appender name='SOCKET' class='ch.qos.logback.classic.net.SocketAppender'>" +
//                        "    <lazy>true</lazy>" +
//                        "    <remoteHost>" + _host + "</remoteHost>" +
//                        "    <port>" + _port + "</port>" +
//                        "    <reconnectionDelay>10000</reconnectionDelay>" +
//                        "    <includeCallerData>false</includeCallerData>" +
//                        "    <encoder class='ch.qos.logback.classic.encoder.PatternLayoutEncoder'>" +
//                        "       <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg %n</pattern>" +
//                        "    </encoder>" +
//                        "  </appender>" +
//                        "  <appender name='ASYNC' class='ch.qos.logback.classic.AsyncAppender'>" +
//                        "    <appender-ref ref='SOCKET' />" +
//                        "  </appender>" +
//                        "  <root level='ERROR'>" +
//                        "    <appender-ref ref='ASYNC' />" +
//                        "  </root>" +
//                        "</configuration>";
//
//        Charset _c = Charset.defaultCharset();
//
//        Log.e("charset",_c.name());
//
//        LoggerContext _log_context = (LoggerContext) LoggerFactory.getILoggerFactory();
//
//        _log_context.reset();
//
//        JoranConfigurator _joran_config = new JoranConfigurator();
//
//        _joran_config.setContext(_log_context);
//
//        InputStream _input_stream = new ByteArrayInputStream(_config_xml.getBytes());
//
//        try {
//
//            _joran_config.doConfigure(_input_stream);
//
//        } catch (Exception e) {
//
////            e.printStackTrace();
//
//            return false;
//        }
//
//        return true;
//    }
//
//
//    /**
//     * 拦截程序错误处理
//     *
//     * @param thread
//     * @param ex
//     */
//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//
//        if (!handle_exception(ex) && _uncaught_exception != null) {
//
//            //如果用户没有处理则让系统默认的异常处理器来处理
//            _uncaught_exception.uncaughtException(thread, ex);
//        } else {
//
//            try {
//
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//
//            }
//            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
//        }
//    }
//
//
//    /**
//     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
//     *
//     * @param _ex
//     * @return true:如果处理了该异常信息;否则返回false.
//     */
//    private boolean handle_exception(Throwable _ex) {
//
//        if (_ex == null) {
//
//            return false;
//        }
//
//        //收集设备参数信息
////        get_device_info(_context);
//
//        //使用Toast来显示异常信息
//        new Thread() {
//            @Override
//            public void run() {
//
//                Looper.prepare();
//                Toast.makeText(_context, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }.start();
//
//        //发送错误信息到服务端
//
//        Log.e("南京圈圈科技", get_exception(_ex));
//
////        send_log_to_server(get_exception(_ex));
//
//        return true;
//    }
//
//
//    /**
//     * 收集设备参数信息
//     *
//     * @param _context
//     */
//    private void get_device_info(Context _context) {
//
//        try {
//            PackageManager _package_manager = _context.getPackageManager();
//
//            PackageInfo _package_info = _package_manager.getPackageInfo(_context.getPackageName(), PackageManager.GET_ACTIVITIES);
//
//            if (_package_info != null) {
//
//                String versionName = _package_info.versionName == null ? "null" : _package_info.versionName;
//
//                String versionCode = _package_info.versionCode + "";
//
//                _log_info_map.put("_version_name", versionName);
//
//                _log_info_map.put("_version_code", versionCode);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        }
//        Field[] _fields = Build.class.getDeclaredFields();
//
//        for (Field _field : _fields) {
//
//            try {
//
//                _field.setAccessible(true);
//
//                _log_info_map.put(_field.getName(), _field.get(null).toString());
//
//            } catch (Exception e) {
//
//            }
//        }
//    }
//
//
//    /**
//     * 异常信息收集
//     *
//     * @param _ex
//     * @return
//     */
//    private String get_exception(Throwable _ex) {
//
//        StringBuffer _string_buffer = new StringBuffer();
//
//        for (Map.Entry<String, String> _entry : _log_info_map.entrySet()) {
//
//            String _key = _entry.getKey();
//
//            String _value = _entry.getValue();
//
//            _string_buffer.append(_key + "=" + _value + "\n");
//        }
//
//        Writer _writer = new StringWriter();
//
//        PrintWriter _print_writer = new PrintWriter(_writer);
//
//        _ex.printStackTrace(_print_writer);
//
//        Throwable _cause = _ex.getCause();
//
//        while (_cause != null) {
//
//            _cause.printStackTrace(_print_writer);
//
//            _cause = _cause.getCause();
//        }
//        _print_writer.close();
//
//        try {
//
//            String _result = new String(_writer.toString().getBytes(),"utf-8");
//
//            _string_buffer.append(_result);
//
//            return _string_buffer.toString();
//        }catch (Exception e){
//
//            return null;
//        }
//
//    }
//
//
//
////    _config_xml =
////            "<configuration debug='false'>" +
////            "  <appender name='LOGCAT' class='ch.qos.logback.classic.android.LogcatAppender'>" +
////            "    <encoder><pattern>%-5relative [%thread][%file:%M:%line] - %msg%n</pattern></encoder>" +
////            "  </appender>" +
////            "  <appender name='SOCKET' class='ch.qos.logback.classic.net.SocketAppender'>" +
////            "    <lazy>true</lazy>" +
////            "    <remoteHost>" + _host + "</remoteHost>" +
////            "    <port>" + _port + "</port>" +
////            "    <reconnectionDelay>10000</reconnectionDelay>" +
////            "    <includeCallerData>false</includeCallerData>" +
////            "  </appender>" +
////            "  <appender name='ASYNC' class='ch.qos.logback.classic.AsyncAppender'>" +
////            "    <appender-ref ref='SOCKET' />" +
////            "  </appender>" +
////            "  <root level='ERROR'>" +
////            "    <appender-ref ref='ASYNC' />" +
////            "  </root>" +
////            "  <root level='TRACE'>" +
////            "    <appender-ref ref='LOGCAT' />" +
////            "  </root>" +
////            "</configuration>";
//}
