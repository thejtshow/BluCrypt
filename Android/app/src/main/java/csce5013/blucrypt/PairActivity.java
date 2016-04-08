package csce5013.blucrypt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class PairActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);

        byte[] hash = getIntent().getByteArrayExtra("hash");
        byte[] key = getIntent().getByteArrayExtra("key");

        mMessageTextView = (TextView) findViewById(R.id.message);
        ListView deviceListView = (ListView) findViewById(R.id.deviceList);
        mBTArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(mBTArrayAdapter);
        deviceListView.setOnItemClickListener(mBTonClickListener);

        initBluetooth();
    }

    /**
     * Initialize and enable Bluetooth device.
     *
     */
    private void initBluetooth() {
        /*** Before Jelly bean ***/
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        /*** Jelly bean and after ***/
//      mBluetoothAdapter = ((BluetoothManager) getSystemService(BLUETOOTH_SERVICE)).getAdapter();
        mDeviceName       = mBluetoothAdapter.getName();

        if (mBluetoothAdapter == null) {
            Log.i(TAG, "BT not available.");
            finish();
        }
        else {
            if (!mBluetoothAdapter.isEnabled()) {
                Log.i(TAG, "BT not enabled.");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        Log.i(TAG, "BT enabled.");
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mBluetoothService == null) {
            mBluetoothService = new BluetoothService(this, mHandler);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
                mBluetoothService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBluetoothService != null)
            mBluetoothService.stop();
        if(mBluetoothAdapter != null)
            mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(btReceiver);
    }



    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.i(TAG, "BluetoothService: STATE_CONNECTED.");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.i(TAG, "BluetoothService: STATE_CONNECTING.");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.i(TAG, "BluetoothService: STATE_NOT_CONNECTED");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    Log.i(TAG, "write message: " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.i(TAG, "read message: " + readMessage);
                    showMessage(readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    String connectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Log.i(TAG, "connectedDeviceName: " + connectedDeviceName);
                    break;
                case Constants.MESSAGE_TOAST:
                    Log.i(TAG, "MESSAGE_TOAST");
                    break;
                default:
                    break;
            }
        }
    };

    private void showMessage(String readMessage) {
        mMessageTextView.setText(readMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT) {
            if(mBluetoothAdapter.isEnabled()) {
                Log.i(TAG, "BT is on.");
            } else {
                Log.i(TAG, "BT is off.");
            }
        }
    }

    public void onScan(View view) {
        scan();
    }

    private void scan() {
        Log.i(TAG, "scanning...");
        mBTArrayAdapter.clear();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        if(mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
        registerReceiver(btReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    public void onSend(View view) {
        String msg = "hello from " + mDeviceName;
        sendMessage(msg);
    }

    public void onClear(View view) {
        mMessageTextView.setText(R.string.messagee_hint);
    }

    /**
     * send a message to the connected BT device.
     *
     * @param msg message to the connected BT device.
     */
    private void sendMessage(String msg) {
        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            Log.i(TAG, "Not Connected.");
            return;
        }

        // Check that there's actually something to send
        if (msg.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = msg.getBytes();
            mBluetoothService.write(send);
        }
    }

    private void sendMessage(byte[] msg) {
        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            Log.i(TAG, "Not Connected.");
            return;
        }

        // Check that there's actually something to send
        if (msg.length > 0) {
            mBluetoothService.write(msg);
        }
    }

    public static final String TAG = "StartupActivity";
    public static final int REQUEST_ENABLE_BT = 1;

    private BluetoothDevice mConnectedDevice = null;
    private String mDeviceName = null;
    private boolean mSecurity = true;

    private BluetoothService mBluetoothService = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private ArrayAdapter<String> mBTArrayAdapter = null;

    TextView mMessageTextView;

    /**
     * broadcast receiver for BT discovery.
     * onReceive: add found devices to array adapter.
     */
    private final BroadcastReceiver btReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    Log.i(TAG, "device found: " + device.getName());
                    mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }
    };

    private AdapterView.OnItemClickListener  mBTonClickListener =
            new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mBluetoothAdapter.cancelDiscovery();
                    String info = ((TextView) view).getText().toString();
                    String address = info.substring(info.length() - 17);

                    Log.i(TAG, "OnItemClickListener: " + address);

                    connectDevice(address, mSecurity);
                }
            };

    private void connectDevice(String address, boolean secure) {
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device, secure);
        mConnectedDevice = device;
    }
}
