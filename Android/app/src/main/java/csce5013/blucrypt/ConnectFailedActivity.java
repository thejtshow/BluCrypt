package csce5013.blucrypt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ConnectFailedActivity extends AppCompatActivity {

    private static final String TAG = "ConnectFailedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_failed);

    }

    //TODO: implement state machine connect failed -> connect by calling sendCredential routine.
    public void onResend(View view) {
        sendCredentials();

    }
    private void sendCredentials() {

    }

    //TODO: implement state machine connect failed -> disconnect by calling disconnect routine.
    public void onDisconnect(View view) {
        disconnect();
    }
    private void disconnect() {
        // TODO: remove placeholder implementation.
        this.finish();
    }

    //TODO: implement state machine connect failed -> logged out by calling logout routine.
    public void onFailedLogout(View view) {
        logout();
    }
    private void logout() {
        // TODO: remove placeholder implementation.
        Intent intent = new Intent(this, UserLoginActivity.class);
        // go back to the log in activity without instantiate a new one.
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
