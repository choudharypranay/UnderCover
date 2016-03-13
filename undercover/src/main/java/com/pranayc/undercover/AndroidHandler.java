package com.pranayc.undercover;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

class AndroidHandler<T extends AndroidHandler.HandlerIx> extends Handler
{
    private WeakReference<T> host;

    public void setInstance(final T caller)
    {
        host = new WeakReference<T>(caller);
    }

    @Override
    public void handleMessage(final Message msg)
    {
        if(host!=null)
        {
            final T caller = host.get();
            if(caller!=null)
            {
                caller.handleMessage(msg);
            }
        }
    }

    public interface HandlerIx
    {
        void handleMessage(final Message msg);
    }
}
