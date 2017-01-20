package com.verizon.appsdkutilization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText url;
    Button buttn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the activity view layout
        setContentView(R.layout.activity_main);
        //declare required components
        url = (EditText) findViewById(R.id.editText);
        buttn = (Button) findViewById(R.id.button);
        //add onclick listener for the button
        buttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Implicit Intent to share the API URL to our sdk utilization code
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                // send Instagram URL
                sendIntent.putExtra(Intent.EXTRA_TEXT, url.getText().toString());
                //set MIMEtype for the call
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
    }
}
