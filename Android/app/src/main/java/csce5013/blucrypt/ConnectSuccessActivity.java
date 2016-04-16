package csce5013.blucrypt;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ConnectSuccessActivity extends AppCompatActivity {

    private static final String TAG = "ConnectSuccessActivity";

    private TextView mTimerView;
    SessionTimer mSessionTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_success);

        Log.i(TAG, "ConnectSuccessActivity");
        mTimerView = (TextView) findViewById(R.id.timer);
        mTimerView.setText(String.valueOf(Constants.TEST_LENGTH));
        mSessionTimer = new SessionTimer(Constants.TEST_LENGTH, Constants.UPDATE_INTERVAL);
        mSessionTimer.start();
    }

    //TODO: implement state machine connect success -> connect success by calling sendCredential routine.
    public void onRenew(View view) {
        renew();
    }
    private void renew() {
        Log.i(TAG, "renew session.");
        mSessionTimer.cancel();
        mSessionTimer.start();
    }

    public void onDisconnect(View view) {
        disconnect();
    }
    private void disconnect() {
        mSessionTimer.cancel();
        this.finish();
    }

    public void onSuccessLogout(View view) {
        mSessionTimer.cancel();
        logout();
    }
    private void logout() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        // go back to the log in activity without instantiate a new one.
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private class SessionTimer extends CountDownTimer {

        public SessionTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mTimerView.setText("Session expired!");
            mTimerView.setTextSize(36);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i(TAG, "remaining time: " + millisUntilFinished / 1000);
            mTimerView.setText(String.valueOf(millisUntilFinished / 1000));
        }
    }

}
