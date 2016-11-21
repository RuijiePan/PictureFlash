package com.jiepier.pictureflash.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jiepier.pictureflash.R;

/**
 * Created by JiePier on 16/11/18.
 */

public class MyAlertDialog extends AlertDialog {

    private Context mContext;
    private EditText editText;
    private Button bt_certain;
    private Button bt_cancle;
    private OnSingleClickListener mListener;

    public MyAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public MyAlertDialog(@NonNull Context context) {
        this(context , R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        editText = (EditText) findViewById(R.id.edit_album);
        /*bt_certain = (Button) findViewById(R.id.bt_certain);
        bt_cancle = (Button) findViewById(R.id.bt_cancle);

        bt_certain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    if (editText.getText() != null) {
                        mListener.onCertainClick(editText.getText().toString());
                    } else {
                        mListener.onNullCallBack();
                    }
                }
            }
        });

        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null)
                mListener.onCancelClick();
            }
        });*/

    }

    interface OnSingleClickListener{
        void onCertainClick(String string);

        void onCancelClick();

        void onNullCallBack();
    }
}
