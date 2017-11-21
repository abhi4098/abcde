package com.CobaltConnect1.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefUtils {
    private static final String TAG = LogUtils.makeLogTag(PrefUtils.class);
    /**
     * Keys of the shared preferences stored
     */
    public static String KEY_AUTH_TOKEN = "user_auth_token";
    public static String KEY_USER_LOGGED_IN = "user_logged_in";
    public static String LOGGED_IN_USER_NAME = "user_name";
    public static String KEY_USER_PASSWORD = "user_password";
    public static String KEY_USER_COBALT_ID = "user_cobalt_id";
    public static String LOGGED_IN_USER_EMAIL = "user_email";
    public static String LOGGED_IN_USER_CLOVER_ID = "user_clover_id";
    public static String LOGGED_IN_USER_CLOVER_TOKEN = "user_clover_token";
    public static String LOGGED_IN_USER_STATE_ID = "user_state_id";

    /**
     * Constant string for file name to store the SharedPreferences of the
     * application. This is required to get the same SharedPreferences object
     * regardless of the package name of the class
     */
    private static final String FILE_NAME = "default.prefs";

    /**
     * static preference manager that is returned when any class calls
     * for a preference manager
     */
    private static SharedPreferences mSharedPreferences;

    /**
     * returns same SharedPrefs
     * through out the application
     *
     * @param context context
     * @return SharedPreference object
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    /**
     * Stores the auth token in the shared preferences using the PrefUtil.KEY_AUTH_TOKEN key
     *
     * @param authToken Auth token of the user received from the user
     */
    public static void storeAuthToken(String authToken, Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        sharedPreferences.edit()
                .putString(PrefUtils.KEY_AUTH_TOKEN, authToken)
                .apply();
        LogUtils.LOGE(TAG, "Auth token has been saved+" + authToken);
    }

    /**
     * returns the auth token of the user
     * returns "" if no auth token is present
     *
     * @param context context
     * @return auth token
     */
    public static String getAuthToken(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        return sharedPreferences.getString(PrefUtils.KEY_AUTH_TOKEN, "");
    }

    public static void storeUserLoggedIn(Boolean isLoggedIn, Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        sharedPreferences.edit()
                .putBoolean(PrefUtils.KEY_USER_LOGGED_IN, isLoggedIn)
                .apply();
        LogUtils.LOGD(TAG, "User loggedIn true");
    }

    public static Boolean getUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        return sharedPreferences.getBoolean(PrefUtils.KEY_USER_LOGGED_IN, false);
    }


    public static void storeUserCobaltId(String loginId, Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        sharedPreferences.edit()
                .putString(PrefUtils.KEY_USER_COBALT_ID, loginId)
                .apply();
        LogUtils.LOGD(TAG, "doctor login id has been saved+" + loginId);
    }

    /**
     * returns the auth token of the user
     * returns "" if no auth token is present
     *
     * @param context context
     * @return auth token
     */
    public static String getUserCobaltId(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        return sharedPreferences.getString(PrefUtils.KEY_USER_COBALT_ID, "");
    }

    /**
     * Stores the auth token in the shared preferences using the PrefUtil.KEY_AUTH_TOKEN key
     *
     * @param password Auth token of the user received from the user
     */
    public static void storeUserPassword(String password, Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        sharedPreferences.edit()
                .putString(PrefUtils.KEY_USER_PASSWORD, password)
                .apply();
        LogUtils.LOGD(TAG, "doc password has been saved+" + password);
    }

    /**
     * returns the auth token of the user
     * returns "" if no auth token is present
     *
     * @param context context
     * @return auth token
     */
    public static String getUserPassword(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        return sharedPreferences.getString(PrefUtils.KEY_USER_PASSWORD, "");
    }


    public static void storeUserName(String name, Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        sharedPreferences.edit()
                .putString(LOGGED_IN_USER_NAME, name)
                .apply();

    }

    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        return sharedPreferences.getString(LOGGED_IN_USER_NAME, "");
    }

    public static void storeCloverId(String cloverId, Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        sharedPreferences.edit()
                .putString(LOGGED_IN_USER_CLOVER_ID, cloverId)
                .apply();

    }

    public static String getCloverId(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        return sharedPreferences.getString(LOGGED_IN_USER_CLOVER_ID, "");
    }


    public static void storeCloverToken(String cloverToken, Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        sharedPreferences.edit()
                .putString(LOGGED_IN_USER_CLOVER_TOKEN, cloverToken)
                .apply();

    }

    public static String getCloverToken(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        return sharedPreferences.getString(LOGGED_IN_USER_CLOVER_TOKEN, "");
    }

    public static void storeStateId(String stateId, Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        sharedPreferences.edit()
                .putString(LOGGED_IN_USER_STATE_ID, stateId)
                .apply();

    }

    public static String getStateId(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        return sharedPreferences.getString(LOGGED_IN_USER_STATE_ID, "");
    }


    public static void storeEmail(String email, Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        sharedPreferences.edit()
                .putString(LOGGED_IN_USER_EMAIL, email)
                .apply();

    }

    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPreferences(context);
        return sharedPreferences.getString(LOGGED_IN_USER_EMAIL, "");
    }






}
