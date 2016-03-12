package com.pranayc.undercover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ConfigResultDialog extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "ConfigResultDialog", Toast.LENGTH_LONG).show();
        startService(new Intent(this, ConfigService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        finish();
    }
}
