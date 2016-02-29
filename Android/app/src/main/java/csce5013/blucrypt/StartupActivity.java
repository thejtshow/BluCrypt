package csce5013.blucrypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Log.i("tingxiny", "Tingxiny branch.");
    }
}
