package com.pranayc.undercover;

import android.content.Context;

class LocalStorage
{
    private static final String NAME = "APPPREF";

    public static void save(Context context, String key, String value)
    {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static String get(Context context, String key)
    {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(key, null);
    }
}
