package com.genius.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;


public class Utils_Alert {

    public static AlertDialog.Builder getDialogBuilder(Context context, int title, int message, boolean cancleable, int positive, OnClickListener positive_ls, int negtive, OnClickListener negtive_ls) {

        if (context instanceof Activity) {
            UtilsLog.v("is Instance Activity");
        } else if (context instanceof Application) {
            UtilsLog.v("is Instance Application");
        }
        if (context == null)
            return null;
        AlertDialog.Builder alertDialogbuilder = null;
        alertDialogbuilder = new AlertDialog.Builder(context);
        if (title != 0)
            alertDialogbuilder.setTitle(title);
        if (message != 0)
            alertDialogbuilder.setMessage(message);
        alertDialogbuilder.setCancelable(cancleable);
        if (positive != 0)
            alertDialogbuilder.setPositiveButton(positive, positive_ls);
        if (negtive != 0)
            alertDialogbuilder.setNegativeButton(negtive, negtive_ls);
        return alertDialogbuilder;
    }


    public static void showAlertDialog_ad(Context context, int title, int message, boolean cancleable, int positive, OnClickListener positive_ls, int negtive, OnClickListener negtive_ls, OnKeyListener onKeyListener) {

        if (context == null)
            return;

        if (context instanceof Activity) {
            UtilsLog.v("is Instance Activity");
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (context.isRestricted()) {
            return;
        }


        try {
            AlertDialog.Builder alertDialogbuilder = null;
            alertDialogbuilder = new AlertDialog.Builder(context);
            if (title != 0)
                alertDialogbuilder.setTitle(title);
            if (message != 0)
                alertDialogbuilder.setMessage(message);
            alertDialogbuilder.setCancelable(cancleable);
            if (positive != 0)
                alertDialogbuilder.setPositiveButton(positive, positive_ls);
            if (negtive != 0)
                alertDialogbuilder.setNegativeButton(negtive, negtive_ls);
            if (onKeyListener != null)
                alertDialogbuilder.setOnKeyListener(onKeyListener);
            alertDialogbuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void showAlertDialog(Context context, int title, int message, boolean cancleable, int positive, OnClickListener positive_ls, int negtive, OnClickListener negtive_ls, OnKeyListener onKeyListener) {
        if (context == null)
            return;

        if (context instanceof Activity) {
            UtilsLog.v("is Instance Activity");
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (context.isRestricted()) {
            return;
        }


        try {
            AlertDialog.Builder alertDialogbuilder = null;
            alertDialogbuilder = new AlertDialog.Builder(context);
            if (title != 0)
                alertDialogbuilder.setTitle(title);
            if (message != 0)
                alertDialogbuilder.setMessage(message);
            alertDialogbuilder.setCancelable(cancleable);
            if (positive != 0)
                alertDialogbuilder.setPositiveButton(positive, positive_ls);
            if (negtive != 0)
                alertDialogbuilder.setNegativeButton(negtive, negtive_ls);
            if (onKeyListener != null)
                alertDialogbuilder.setOnKeyListener(onKeyListener);
            alertDialogbuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertDialog(Context context, String title, int message, boolean cancleable, int positive, OnClickListener positive_ls, int negtive, OnClickListener negtive_ls, OnKeyListener onKeyListener) {
        if (context == null)
            return;

        if (context instanceof Activity) {
            UtilsLog.v("is Instance Activity");
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (context.isRestricted()) {
            return;
        }


        try {
            AlertDialog.Builder alertDialogbuilder = null;
            alertDialogbuilder = new AlertDialog.Builder(context);
            if (!TextUtils.isEmpty(title))
                alertDialogbuilder.setTitle(title);
            if (message != 0)
                alertDialogbuilder.setMessage(message);
            alertDialogbuilder.setCancelable(cancleable);
            if (positive != 0)
                alertDialogbuilder.setPositiveButton(positive, positive_ls);
            if (negtive != 0)
                alertDialogbuilder.setNegativeButton(negtive, negtive_ls);
            if (onKeyListener != null)
                alertDialogbuilder.setOnKeyListener(onKeyListener);
            alertDialogbuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertDialog(Context context, int title, String message, boolean cancleable, int positive, OnClickListener positive_ls, int negtive, OnClickListener negtive_ls, OnKeyListener onKeyListener) {
        if (context == null)
            return;

        if (context instanceof Activity) {
            UtilsLog.v("is Instance Activity");
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (context.isRestricted()) {
            return;
        }


        try {
            AlertDialog.Builder alertDialogbuilder = null;
            alertDialogbuilder = new AlertDialog.Builder(context);
            if (title != 0)
                alertDialogbuilder.setTitle(title);
            if (!TextUtils.isEmpty(message))
                alertDialogbuilder.setMessage(message);
            alertDialogbuilder.setCancelable(cancleable);
            if (positive != 0)
                alertDialogbuilder.setPositiveButton(positive, positive_ls);
            if (negtive != 0)
                alertDialogbuilder.setNegativeButton(negtive, negtive_ls);
            if (onKeyListener != null)
                alertDialogbuilder.setOnKeyListener(onKeyListener);
            alertDialogbuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
