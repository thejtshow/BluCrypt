package csce5013.blucrypt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

//This activity is launched at app start, and handles all the startup tasks

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        //create the login activity
        Intent login = new Intent(this, LoginActivity.class);

        this.startActivity(login);
    }
}
