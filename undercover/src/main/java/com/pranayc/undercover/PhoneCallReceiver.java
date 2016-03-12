package com.pranayc.undercover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Set;

public class PhoneCallReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        if(context!=null && intent!=null)
        {
            final Bundle bundle = intent.getExtras();
            if(context!= null && bundle!=null)
            {
                Toast.makeText(context, getAllExtras(bundle), Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(context, "NO EXTRAS", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static String getAllExtras(final Bundle bundle)
    {
        final Set<String> keys = bundle.keySet();
        final StringBuilder sb = new StringBuilder();
        if(keys!=null)
        {
            for(final String key: keys)
            {
                sb.append(key + "=" + bundle.get(key));
                sb.append(Utility.NEW_LINE);
            }
        }
        return sb.toString();
    }
}
