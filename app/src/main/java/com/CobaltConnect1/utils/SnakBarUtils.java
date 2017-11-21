package com.CobaltConnect1.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

public class SnakBarUtils {

    public static void networkConnected(Context context) {
        SnackbarManager.show(Snackbar.with(context)
                .text("No Network Available")
                .textTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                .textColor(Color.WHITE)
                .animation(true)
                .color(Color.RED), (Activity) context);
    }

    public static void invalidLogin(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            SnackbarManager.show(Snackbar.with(context)
                    .text("Please enter valid mobile number")
                    .textTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                    .textColor(Color.WHITE)
                    .animation(true)
                    .color(Color.GREEN), (Activity) context);
        }
    }

    public static void invalidOTP(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            SnackbarManager.show(Snackbar.with(context)
                    .text("Please enter valid OTP")
                    .textTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                    .textColor(Color.WHITE)
                    .animation(true)
                    .color(Color.GREEN), (Activity) context);
        }
    }

    public static void invaildResponse(Context context, String response) {
        SnackbarManager.show(Snackbar.with(context)
                .text(response)
                .textTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                .textColor(Color.WHITE)
                .animation(true)
                .color(Color.RED), (Activity) context);
    }

    public static void OTPVerificationFailed(Context context) {
        SnackbarManager.show(Snackbar.with(context)
                .textTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                .text("OTP Verification Failed")
                .textColor(Color.WHITE)
                .animation(true)
                .color(Color.GREEN), (Activity) context);
    }
}
