package com.telvo.telvoterminaladmin.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class CustomAlertDialog {
    private Context context;
    private AlertDialog.Builder alertDialogBuilder;

    public CustomAlertDialog(Context context) {
        this.context = context;
    }

    public void showDialog(String message) {
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }
    public void showDialogWithYesNo(String title,String message,final ButtonClick yes, final ButtonClick no) {
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int arg1) {
                yes.Do();
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                no.Do();
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    public void showDialogWithYes(String message,final ButtonClick yes) {
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int arg1) {
                yes.Do();
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }
}
