package cc.banzhi.runmonitor.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * @program: ZRunMonitor
 * @description: SP管理类
 * @author: zoufengli01
 * @create: 2022/3/11 5:08 下午
 **/
public class SpUtil {
    private static final String NAME = "RUNMONITOR_SP";

    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(NAME, MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static boolean clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return editor.commit();
    }
}
