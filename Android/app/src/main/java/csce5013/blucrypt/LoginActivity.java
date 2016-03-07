package csce5013.blucrypt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    private CredentialStore credentials;
    private TextView infoText, infoText2;
    private EditText PINField;
    private Button clearMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //instantiate credentials library, pass boolean if keystore exists
        credentials = new CredentialStore((new File(this.getFilesDir() + "keystore").exists() && !(new File("keystore").isDirectory())), this);

        PINField = (EditText) findViewById(R.id.PINField);
        infoText = (TextView) findViewById(R.id.infoText);
        infoText2 = (TextView) findViewById(R.id.infoText2);
        clearMemory = (Button) findViewById(R.id.clearMemory);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        PINField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() == 6)
                {
                    final int PIN = Integer.valueOf(s.toString());

                    //clear the text field here regardless -- aesthetics
                    s.clear();

                    if(credentials.HasCredentials())
                    {
                        if(credentials.CheckCredential(PIN))
                        {
                            //log in
                            Login();

                            infoText2.setText("");
                        }
                        else
                        {
                            //do not log in. Must log in to add a new credential
                            infoText.setText("Failed!!!");
                        }
                    }
                    else
                    {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //add PIN to the credential store
                                        credentials.AddCredential(PIN);

                                        //display a debug message
                                        infoText2.setText("Added PIN to credential store");

                                        //now log the user in
                                        Login();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };

                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                }
            }
        });

        clearMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                credentials.ClearMemory();
            }
        });
    }

    private void Login()
    {
        infoText.setText("Success!");
    }
}
