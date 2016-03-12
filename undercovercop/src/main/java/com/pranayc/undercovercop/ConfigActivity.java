package com.pranayc.undercovercop;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Messenger;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfigActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        final Button configbtn = (Button) findViewById(R.id.configbtn);
        configbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                if(username!=null && username.length()>3 && password!=null && password.length()>4)
                {
                    Intent intent=new Intent();
                    intent.putExtra("UserName",username);
                    intent.putExtra("PassWord",password);
                    setResult(1,intent);
                    finish();
                }
            }
        });
    }
}
