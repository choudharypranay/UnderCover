package com.pranayc.undercovercop;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatusActivity extends Activity implements View.OnClickListener
{
    private ServiceConnection serviceConnection;
    private Messenger messenger;

    private final Handler HANDLER = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0:
                {
                    TextView tv = (TextView) findViewById(R.id.config_status);
                    Button btn = (Button) findViewById(R.id.config_button);
                    tv.setText(R.string.config_absent);
                    btn.setText(R.string.configbtn_absent);
                    btn.setOnClickListener(StatusActivity.this);
                }
                    break;
                case 1:
                {
                    TextView tv = (TextView) findViewById(R.id.config_status);
                    Button btn = (Button) findViewById(R.id.config_button);
                    tv.setText(R.string.config_present);
                    btn.setText(R.string.configbtn_present);
                    btn.setOnClickListener(StatusActivity.this);
                }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        //Intent intent = new Intent();
        //intent.setComponent(new ComponentName("com.pranayc.undercover", "com.pranayc.undercover.ConfigResultDialog"));
        //startActivity(intent);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(checkInstallStatus())
        {
            // Check Config Status
            serviceConnection = new ServiceConnection()
            {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service)
                {
                    messenger = new Messenger(service);

                    Message msg = Message.obtain(null, 64);
                    msg.replyTo = new Messenger(HANDLER);

                    try
                    {
                        messenger.send(msg);
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name)
                {
                    messenger = null;

                }
            };

            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.pranayc.undercover", "com.pranayc.undercover.ConfigService"));
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private boolean checkInstallStatus()
    {
        final boolean isUndercoverAvailable = Utility.isAppInstalled(this, Utility.UNDERCOVER_PACKAGE);
        final TextView installedStatus = (TextView) findViewById(R.id.installed_status);
        final Button installButton = (Button) findViewById(R.id.install_button);
        if(isUndercoverAvailable)
        {
            installedStatus.setText(R.string.undercover_present);
            installButton.setVisibility(View.GONE);
            return true;
        }
        else
        {
            installedStatus.setText(R.string.undercover_absent);
            installButton.setVisibility(View.VISIBLE);
            return false;
        }
    }

    @Override
    public void onClick(View v)
    {
        startActivityForResult(new Intent(this, ConfigActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==100 && resultCode==1)
        {
            String username = data.getStringExtra("UserName");
            String password = data.getStringExtra("PassWord");

            final Message msg = Message.obtain(null, 786);

            final Bundle bundle = new Bundle();
            bundle.putString("UserName", username);
            bundle.putString("PassWord", password);
            msg.setData(bundle);
            msg.replyTo = new Messenger(HANDLER);

            try
            {
                messenger.send(msg);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }
}
