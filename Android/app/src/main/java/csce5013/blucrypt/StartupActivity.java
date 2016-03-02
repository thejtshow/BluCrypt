package csce5013.blucrypt;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartupActivity extends AppCompatActivity {

    public static final String TAG = "tingxiny";
    public static final int REQUEST_ENABLE_BT = 1;


    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        TextView message = (TextView) findViewById(R.id.message);
        Button   sendBtn = (Button)   findViewById(R.id.sendBtn);
        Button   pairBtn = (Button)   findViewById(R.id.pairBtn);

        initBluetooth();


    }

    /*
     * TODO: implement BT manager initialization.
     */
    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

            Log.i(TAG, "BT not available.");

            Toast.makeText(getApplicationContext(),
                    "Bluetooth device not available. Please check your BT setting.",
                    Toast.LENGTH_SHORT);
        }
        else {
            if (!mBluetoothAdapter.isEnabled()) {

                Log.i(TAG, "BT not enabled.");

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    /*
     * TODO: implement pairing with server.
     */
    public void onPair(View view) {

    }

    /*
     * TODO: implement sending message to server.
     */
    public void onSend(View view) {

    }
}
