package qqkj.qqkj_library.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * 这个类是用来做甚的
 * <p>
 * Created by 陈二狗 on 2017/11/8.
 */

public class DialogUtil {

    private Context _context = null;

    public DialogUtil(Context _context) {

        this._context = _context;
    }


    public static DialogUtil getIns(Context _context) {

        return new DialogUtil(_context);
    }


    /**
     * 2个按钮(确定,取消)
     *
     * @param _title
     * @param _msg
     * @param _sure_text
     * @param _cancel_text
     * @param _sure_click
     * @param _cancel_click
     */
    public AlertDialog _get_dialog_two_btn(String _title, String _msg, String _sure_text, String _cancel_text,

                                           DialogInterface.OnClickListener _sure_click, DialogInterface.OnClickListener _cancel_click) {

        AlertDialog.Builder _dialog_builder = new AlertDialog.Builder(_context);

        _dialog_builder.setTitle(_title);

        _dialog_builder.setMessage(_msg);

        _dialog_builder.setPositiveButton(_sure_text, _sure_click);

        _dialog_builder.setNegativeButton(_cancel_text, _cancel_click);

        _dialog_builder.setCancelable(false);

        return _dialog_builder.show();
    }


    /**
     * 一个按钮(确定)
     *
     * @param _title
     * @param _msg
     * @param _sure_text
     * @param _sure_click
     */
    public AlertDialog _get_dialog_one_btn(String _title, String _msg, String _sure_text, DialogInterface.OnClickListener _sure_click) {

        AlertDialog.Builder _dialog_builder = new AlertDialog.Builder(_context);

        _dialog_builder.setTitle(_title);

        _dialog_builder.setMessage(_msg);

        _dialog_builder.setPositiveButton(_sure_text, _sure_click);

        _dialog_builder.setCancelable(false);

        return _dialog_builder.show();
    }


    /**
     * 正极按钮
     * @param _dialog
     * @return
     */
    public Button _get_dialog_button_positive(AlertDialog _dialog){

        return _dialog.getButton(AlertDialog.BUTTON_POSITIVE);
    }


    /**
     * 负极按钮
     * @param _dialog
     * @return
     */
    public Button _get_dialog_button_negative(AlertDialog _dialog){

        return _dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
    }


    /**
     * 反射获取AlertDialog中MessageView
     * @param _dialog
     * @return
     */
    public TextView _get_dialog_message_view(AlertDialog _dialog){

        try {
            //反射AlertDialog
            Field _alert = AlertDialog.class.getDeclaredField("mAlert");

            _alert.setAccessible(true);

            Object _controller = _alert.get(_dialog);

            Field _message = _controller.getClass().getDeclaredField("mMessageView");

            _message.setAccessible(true);

            return (TextView) _message.get(_controller);
        }catch (Exception e){

        }

        return null;
    }
}
