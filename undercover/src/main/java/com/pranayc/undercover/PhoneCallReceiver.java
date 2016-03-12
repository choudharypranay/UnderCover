package com.pranayc.undercover;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
                String phoneNumber = bundle.getString(Intent.EXTRA_PHONE_NUMBER);
                String extraState = bundle.getString(TelephonyManager.EXTRA_STATE);
                if (extraState != null)
                {
                    if(extraState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK) ||
                            extraState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))
                    {
                        Toast.makeText(context, "Incoming...", Toast.LENGTH_SHORT).show();
                        Intent callIntent = new Intent(context, RecordService.class);
                        context.startService(callIntent);
                    }
                    else if(extraState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE))
                    {
                        context.stopService(new Intent(context, RecordService.class));
                    }
                }
            }
        }
    }
}
