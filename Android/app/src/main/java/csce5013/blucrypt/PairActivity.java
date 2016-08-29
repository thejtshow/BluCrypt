package csce5013.blucrypt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PairActivity extends AppCompatActivity {

    private static final String TAG = "PairActivity";

    private ArrayList<String>    mBTArrayList    = null;
    private ArrayAdapter<String> mBTArrayAdapter = null;

    private AdapterView.OnItemClickListener mBTonClickListener =
            new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String info = ((TextView) view).getText().toString();
//                  String address = info.substring(info.length() - 17);
                    Log.i(TAG, "OnItemClickListener: " + position);

                    connectDevice(position);
                }
            };

    // TODO: fill in BT connection protocol part.
    private void connectDevice(int id) {
        Intent intent = null;
        // placeholder implementation: SUCC/FAIL code based on device id.
        switch(id) {
            case 2:
                intent = new Intent(this, ConnectFailedActivity.class);
                break;
            default:
                intent = new Intent(this, ConnectSuccessActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);

        mBTArrayList    = new ArrayList<>();
        mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mBTArrayList);

        //TODO: remove placeholder list with actual BT device list.
        String[] dummyDevices = {"device foo", "device bar", "device fail"};
        for(String device : dummyDevices)
            mBTArrayList.add(device);

        ListView deviceListView = (ListView) findViewById(R.id.bt_device_list);
        deviceListView.setAdapter(mBTArrayAdapter);
        deviceListView.setOnItemClickListener(mBTonClickListener);

    }

    //TODO: state machine start -> logout
    public void onLogout(View view) {
        // Placeholder implementation: quit current activity
        this.finish();
    }

    //TODO: state machine logged in -> connected
    public void onConnSucc(View view) {
        // Placeholder implementation: start ConnectSuccessActivity
        Intent intent = new Intent(this, ConnectSuccessActivity.class);
        startActivity(intent);
    }

    //TODO: state machine logged in -> connect failed
    public void onConnFail(View view) {
        // Placeholder implementation: start ConnectFailedActivity
        Intent intent = new Intent(this, ConnectFailedActivity.class);
        startActivity(intent);
    }

}
