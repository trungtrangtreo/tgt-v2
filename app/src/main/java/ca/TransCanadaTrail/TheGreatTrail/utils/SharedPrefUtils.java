package ca.TransCanadaTrail.TheGreatTrail.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    private static final String PREF_APP = "pref_tgt_app";
    private static final String IS_HOW_USED = "is_how_used";
    private static final String IS_SHOW_NOTIFICATION = "is_show_notification";

    private SharedPrefUtils() {
        throw new UnsupportedOperationException(
                "Should not create instance of Util class. Please use as static..");
    }


    /**
     * Save data.
     *
     * @param context the context
     * @param isHowUsed the key
     */
    static public void saveIsClickGetStarted(Context context, boolean isHowUsed) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(IS_HOW_USED, isHowUsed)
                .apply();
    }

    /**
     * Gets boolean data.
     *
     * @param context the context
     * @return the boolean is click button GET STARTED or not click
     */
    static public boolean isClickGetStarted(Context context) {

        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getBoolean(IS_HOW_USED, false);
    }

    /**
     * Save data.
     *
     * @param context the context
     * @param state the key
     */
    static public void saveStateNotification(Context context, boolean state) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(IS_SHOW_NOTIFICATION, state)
                .apply();
    }

    /**
     * Gets boolean data.
     *
     * @param context the context
     * @return the boolean is click button GET STARTED or not click
     */
    static public boolean isShowNotification(Context context) {
        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getBoolean(IS_SHOW_NOTIFICATION, true);
    }



    /**
     * Gets int data.
     *
     * @param context the context
     * @param key     the key
     * @return the int data
     */
    static public int getIntData(Context context, String key) {
        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getInt(key, 0);
    }

    /**
     * Gets string data.
     *
     * @param context the context
     * @param key     the key
     * @return the string data
     */
    // Get Data
    static public String getStringData(Context context, String key) {
        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getString(key, null);
    }

    /**
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
    // Save Data
    static public void saveData(Context context, String key, String val) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putString(key, val).apply();
    }

    /**
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
    static public void saveData(Context context, String key, int val) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putInt(key, val).apply();
    }

    static public SharedPreferences.Editor getSharedPrefEditor(Context context, String pref) {
        return context.getSharedPreferences(pref, Context.MODE_PRIVATE).edit();
    }

    static public void saveData(SharedPreferences.Editor editor) {
        editor.apply();
    }
}
