package com.pranayc.undercovercop;

import android.content.Context;
import android.content.pm.PackageManager;

public class Utility
{
    public static final String UNDERCOVER_PACKAGE = "com.pranayc.undercover";

    public static boolean isAppInstalled(final Context context, final String packageName) {
        try
        {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    public static boolean isRecorderConfigured(final Context context, final String packageName) {
        try
        {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}