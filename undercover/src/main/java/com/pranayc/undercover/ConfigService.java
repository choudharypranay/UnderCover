package com.pranayc.undercover;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class ConfigService extends Service implements AndroidHandler.HandlerIx
{
    private static final AndroidHandler<ConfigService> HANDLER = new AndroidHandler<ConfigService>();
    private static final Messenger messenger = new Messenger(HANDLER);

    @Override
    public IBinder onBind(final Intent intent)
    {
        HANDLER.setInstance(this);
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId)
    {
        HANDLER.setInstance(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void handleMessage(final Message msg)
    {
        switch (msg.what)
        {
            case Utility.WHAT_GET_STATUS:
                try
                {
                    if(msg.replyTo!=null)
                    {
                        String username =  LocalStorage.get(ConfigService.this, "UserName");
                        String password =  LocalStorage.get(ConfigService.this, "PassWord");
                        boolean configured = username!=null && password!=null;
                        Message reply = Message.obtain(null, configured?1:0);
                        msg.replyTo.send(reply);
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                break;
            case Utility.WHAT_SET_DATA:
                Bundle bundle = msg.getData();
                if(bundle!=null)
                {
                    String username =  bundle.getString("UserName");
                    String password = bundle.getString("PassWord");
                    LocalStorage.save(ConfigService.this, "UserName", username);
                    LocalStorage.save(ConfigService.this, "PassWord", password);
                    try
                    {
                        if(msg.replyTo!=null)
                        {
                            Message reply = Message.obtain(null, 1);
                            msg.replyTo.send(reply);
                        }
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
