package com.example.medimate_java;

import android.content.Context;
import android.content.SharedPreferences;

public class UserUtils {

    private static final String PREFS_NAME = "prefs";
    private static final String KEY_USUARIO_ID = "usuario_id";

    public static int obtenerUsuarioId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USUARIO_ID, -1);
    }
}