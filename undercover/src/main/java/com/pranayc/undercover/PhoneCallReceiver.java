package com.pranayc.undercover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneCallReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        if(context!=null && intent!=null)
        {
            final String action = intent.getAction();
            final Bundle bundle = intent.getExtras();
            if(bundle!=null && action!=null)
            {
                if(action.equals(Intent.ACTION_NEW_OUTGOING_CALL) || action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED))
                {
                    System.out.println(action);
                    getExtras(bundle);
                    final String phoneNumber1 = bundle.getString(Intent.EXTRA_PHONE_NUMBER);
                    final String phoneNumber2 = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                    final String phoneNumber;
                    if(phoneNumber1!=null && phoneNumber1.length()>1)
                    {
                        phoneNumber = phoneNumber1;
                    }
                    else if(phoneNumber2!=null && phoneNumber2.length()>1)
                    {
                        phoneNumber = phoneNumber2;
                    }
                    else
                    {
                        phoneNumber = "Unknown Number";
                    }

                    String extraState = bundle.getString(TelephonyManager.EXTRA_STATE);
                    if (extraState != null)
                    {
                        if(extraState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK) ||
                                extraState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))
                        {
                            Intent callIntent = new Intent(context, RecordService.class);
                            callIntent.putExtra("Number", phoneNumber);
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

    private void getExtras(Bundle b)
    {
        for(String key: b.keySet())
        {
            System.out.println(key + "\t~\t" + b.get(key));
        }
    }
}
