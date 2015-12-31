package com.example.troy.myapplication.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Troy on 15/12/23.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"fuck you",0).show();
    }


}
