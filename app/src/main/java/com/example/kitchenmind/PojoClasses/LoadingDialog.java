package com.example.kitchenmind.PojoClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.kitchenmind.R;

public class LoadingDialog
{
    private Activity activity;
    private AlertDialog dialog;
    public LoadingDialog(Activity myActivity)
    {
        activity=myActivity;
    }

    void startLoadingDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(false);

        dialog =builder.create();
        dialog.show();
    }

    void dismissDialog()
    {
        dialog.dismiss();
    }
}
