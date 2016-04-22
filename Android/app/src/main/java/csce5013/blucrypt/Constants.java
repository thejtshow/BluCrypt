package csce5013.blucrypt;

/**
 * Defines several constants used between {@link BluetoothService} and the UI.
 */
public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    String DEVICE_NAME = "device_name";
    String TOAST = "toast";

    // timers
    int SESSION_LENGTH    = 1000*60*11; // 11 minutes
    int HEART_BEAT_LENGTH = 1000*60*5;  // 5 minutes
    int UPDATE_INTERVAL   = 1000;       // 1 second
    int TEST_LENGTH = 5000;             // 5 seconds

    // JSON KEYS
    String DEVICE_KEY = "DEVICE_KEY";
    String USER_KEY = "USER_KEY";
}

