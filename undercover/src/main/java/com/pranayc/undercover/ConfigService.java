package com.pranayc.undercover;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

public class ConfigService extends Service
{
    private final Handler HANDLER = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            System.out.println(msg.what);
            Toast.makeText(ConfigService.this, "Messgae: " + msg.what, Toast.LENGTH_LONG).show();

            try
            {
                Message reply = Message.obtain(null, 1);
                msg.replyTo.send(reply);
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    };

    private Messenger msg = new Messenger(HANDLER);

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return msg.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
        String networkOperator = telephonyManager.getNetworkOperator();
        String mcc = networkOperator.substring(0, 3);
        String mnc = networkOperator.substring(3);
        int cid = cellLocation.getCid();
        int lac = cellLocation.getLac();
        System.err.println(networkOperator);
        System.err.println(cid+","+lac+","+mcc+","+mnc);
        return super.onStartCommand(intent, flags, startId);
    }
}
