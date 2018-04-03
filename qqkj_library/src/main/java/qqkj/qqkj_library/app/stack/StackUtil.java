package qqkj.qqkj_library.app.stack;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2018/2/8.
 */

public final class StackUtil {

    private static Stack<Activity> _activity_stack;

    private static StackUtil _stack_util = null;


    public static StackUtil getIns() {

        if(null == _stack_util){

            _stack_util = new StackUtil();
        }

        return _stack_util;
    }



    /**
     * 获取当前Activity栈中元素个数
     */
    public int _get_count() {

        return _activity_stack.size();
    }



    /**
     * 添加Activity到栈
     */
    public void _add_activity(Activity _activity) {

        if (_activity_stack == null) {

            _activity_stack = new Stack<>();
        }

        _activity_stack.add(_activity);
    }



    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity _top_activity() {

        if (_activity_stack == null) {

            throw new NullPointerException(

                    "Activity stack is Null,your Activity must extend KJActivity");
        }
        if (_activity_stack.isEmpty()) {

            return null;
        }

        Activity _activity = _activity_stack.lastElement();

        return (Activity) _activity;
    }



    /**
     * 获取上一个Activity（栈顶Activity）
     */
    public Activity _before_activity() {

        if (_activity_stack == null) {

            throw new NullPointerException(

                    "Activity stack is Null,your Activity must extend KJActivity");
        }

        if (_activity_stack.isEmpty()) {

            return null;
        }

        Activity _activity = _activity_stack.get(_activity_stack.size() - 2);

        return (Activity) _activity;
    }



    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public Activity _find_activity(Class<?> _class) {

        Activity _activity = null;

        for (Activity _temp : _activity_stack) {

            if (_temp.getClass().equals(_class)) {

                _activity = _temp;

                break;
            }
        }

        return (Activity) _activity;
    }



    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void _finish_activity() {

        Activity _activity = _activity_stack.lastElement();

        _finish_activity((Activity) _activity);
    }



    /**
     * 结束指定的Activity(重载)
     */
    public void _finish_activity(Activity _activity) {

        if (_activity != null) {

            _activity_stack.remove(_activity);

            // activity.finish();//此处不用finish
            _activity = null;
        }
    }



    /**
     * 结束指定的Activity(重载)
     */
    public void _finish_activity(Class<?> _class) {

        for (Activity activity : _activity_stack) {

            if (activity.getClass().equals(_class)) {

                _finish_activity((Activity) activity);
            }
        }
    }



    public void finishAndRemoveActivity(Class<?> cls) {

        for (Activity activity : _activity_stack) {

            if (activity.getClass().equals(cls)) {

                _finish_activity((Activity) activity);

                _activity_stack.remove((Activity) activity);
            }
        }
    }



    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param _class
     */
    public void _finish_others_activity(Class<?> _class) {

        for (Activity _activity : _activity_stack) {

            if (!(_activity.getClass().equals(_class))) {

                _finish_activity((Activity) _activity);
            }
        }
    }



    /**
     * 结束所有Activity
     */
    public void _finish_all_activity() {

        if (null != _activity_stack) {

            for (int i = 0, size = _activity_stack.size(); i < size; i++) {

                if (null != _activity_stack.get(i)) {

                    ((Activity) _activity_stack.get(i)).finish();
                }
            }

            _activity_stack.clear();
        }
    }



    /**
     * 结束所有Activity
     */
    public void _finish_all_other_activity() {

        for (int i = 0, size = _activity_stack.size(); i < size - 1; i++) {

            if (null != _activity_stack.get(i)) {

                ((Activity) _activity_stack.get(i)).finish();
            }
        }

        _activity_stack.clear();
    }



    @Deprecated
    public void AppExit(Context _context) {

        _app_exit();
    }



    /**
     * 应用程序退出
     */
    public void _app_exit() {

        try {
            _finish_all_activity();

            Runtime.getRuntime().exit(0);

        } catch (Exception e) {

            Runtime.getRuntime().exit(-1);
        }

    }
}