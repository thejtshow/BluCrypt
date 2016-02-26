package csce5013.blucrypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//This activity is launched at app start, and handles all the startup tasks

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }
}
