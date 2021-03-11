package com.heuristify.mdu.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static String putKey(Context context, String Key, String Value) {
        sharedPreferences = context.getSharedPreferences("mduCache", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.commit();
        return Key;
    }

    public static String getKey(Context contextGetKey, String Key) {
        sharedPreferences = contextGetKey.getSharedPreferences("mduCache", Context.MODE_PRIVATE);
        String Value = sharedPreferences.getString(Key, "");
        return Value;

    }

    public static void deleteAllSharedPrefs(Context context){
        sharedPreferences = context.getSharedPreferences("mduCache", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
