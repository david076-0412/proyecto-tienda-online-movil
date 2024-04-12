package com.aplication.appgestionrepartos.chat;


import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.aplication.appgestionrepartos.R;

class LoadingDialog{

    private AlertDialog dialog;

    private Activity activity;


    public LoadingDialog(Activity activity){
        this.activity = activity;
    }


    void showLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.alert_dialog, null));

        dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.show();

    }

    void disMiss(){
        dialog.dismiss();
    }



}