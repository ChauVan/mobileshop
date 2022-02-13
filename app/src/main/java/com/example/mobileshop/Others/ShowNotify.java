package com.example.mobileshop.Others;

import android.app.ProgressDialog;
import android.content.Context;

public class ShowNotify {

    //Hiên tắt thông báo chờ

    private static ProgressDialog mProgressDialog;

    public static  void showProgressDialog(Context context, String message){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public static void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }
}
