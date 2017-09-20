package qqkj.qqkj_library.network.parser.xml;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/9/15.
 */

public class XmlUtil {

    private static XmlUtil _xml_util = null;

    private String LOG_TAG = "qqkj_frame";

    private String _xml_result_string = null;  //String返回结果

    private Object _xml_result_object = null;  //Object返回结果

    private List<Object> _xml_result_list = null;   //List返回结果


    public static XmlUtil getIns() {

        _xml_util = new XmlUtil();

        return _xml_util;
    }


    private List<String> _get_field(Object _object) {

        List<String> _field_list = new ArrayList<String>();

        java.lang.reflect.Field[] _fields = _object.getClass().getDeclaredFields();

        for (java.lang.reflect.Field f : _fields) {

            _field_list.add(f.getName());
        }

        return _field_list;
    }


    /**
     * 将xml解析为String
     * @param _xml
     * @param _xml_log
     * @return
     */
    public String _get_xml_to_string(String _xml, boolean _xml_log){

        try {

            byte[] _data = _xml.getBytes();

            XmlPullParser _xml_parser = XmlPullParserFactory.newInstance().newPullParser();

            ByteArrayInputStream _byte_array_input_stream = new ByteArrayInputStream(_data);

            _xml_parser.setInput(_byte_array_input_stream,"utf-8");

            int _event_type = _xml_parser.getEventType();

            String _tag_name = null;

            while (_event_type != XmlPullParser.END_DOCUMENT){

                if(_event_type == XmlPullParser.START_TAG){

                    _tag_name = _xml_parser.getName();

                    if("rt".equals(_tag_name)){

                        _xml_result_string = _xml_parser.nextText();
                    }
                }

                _event_type = _xml_parser.next();
            }
        }catch (Exception e){

            if(_xml_log){

                Log.e(LOG_TAG,"xml解析错误,返回如下...");
                Log.e(LOG_TAG,e.getMessage());
            }

        }

        return _xml_result_string;
    }


    /**
     * 将xml解析为Object
     * @param _xml
     * @param _xml_log
     * @return
     */
    public Object _get_xml_to_object(String _xml, Object _object, boolean _xml_log) {

        try {

            byte[] _data = _xml.getBytes();

            _xml_result_object = _object.getClass().newInstance();

            List<String> _field_list = _get_field(_object);

            Method[] _methods = _object.getClass().getDeclaredMethods();

            XmlPullParser _xml_parser = XmlPullParserFactory.newInstance().newPullParser();

            ByteArrayInputStream _byte_array_input_stream = new ByteArrayInputStream(_data);

            _xml_parser.setInput(_byte_array_input_stream, "utf-8");

            int _event_type = _xml_parser.getEventType();

            String _tag_name = null;

            while (_event_type != XmlPullParser.END_DOCUMENT) {

                if (_event_type == XmlPullParser.START_TAG) {

                    _tag_name = _xml_parser.getName();

                    for (String _field : _field_list) {

                        if (_field.toLowerCase().equals(_tag_name.toLowerCase())) {

                            String _method_name = "set" + _field;

                            for (Method _method : _methods) {

                                if (_method.getName().toLowerCase().equals(_method_name.toLowerCase())) {

                                    _method.invoke(_xml_result_object, _xml_parser.nextText());
                                }
                            }
                        }
                    }
                }

                _event_type = _xml_parser.next();
            }

        } catch (Exception e) {

            if (_xml_log) {

                Log.e(LOG_TAG, "xml解析错误,返回如下...");
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        return _xml_result_object;
    }


    /**
     * 将xml解析为List
     * @param _xml
     * @param _xml_log
     * @return
     */
    public Object _get_xml_to_list(String _xml, Object _object, boolean _xml_log) {

        try {

            byte[] _data = _xml.getBytes();

            _xml_result_list = new ArrayList<Object>();

            _xml_result_object = _object.getClass().newInstance();

            List<String> _field_list = _get_field(_object);

            Method[] _methods = _object.getClass().getDeclaredMethods();

            XmlPullParser _xml_parser = XmlPullParserFactory.newInstance().newPullParser();

            ByteArrayInputStream _byte_array_input_stream = new ByteArrayInputStream(_data);

            _xml_parser.setInput(_byte_array_input_stream, "utf-8");

            int _event_type = _xml_parser.getEventType();

            String _tag_name = null;

            while (_event_type != XmlPullParser.END_DOCUMENT) {

                switch (_event_type){

                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        _tag_name = _xml_parser.getName();

                        for (String _field : _field_list) {

                            if (_field.toLowerCase().equals(_tag_name.toLowerCase())) {

                                String methodName = "set" + _field;

                                for (Method _method : _methods) {

                                    if (_method.getName().toLowerCase().equals(methodName.toLowerCase())) {

                                        _method.invoke(_xml_result_object, _xml_parser.nextText());

                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //if (object.getClass().getName().equals(xmlParser.getName())) {
                        if ("item".equals(_xml_parser.getName().toLowerCase())) {

                            _xml_result_list.add(_xml_result_object);

                            _xml_result_object = _object.getClass().newInstance();
                        }
                        break;
                }

                _event_type = _xml_parser.next();
            }

        } catch (Exception e) {

            if (_xml_log) {

                Log.e(LOG_TAG, "xml解析错误,返回如下...");
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        return _xml_result_object;
    }

}
