package com.jsoftkenya.apps.weatherstats;


import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

    Button btnSignIn,btnSignUp;
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        // Get The Refference Of Buttons
        btnSignIn = (Button) findViewById(R.id.buttonSignIn);
        btnSignUp = (Button) findViewById(R.id.buttonSignUp);



        // Set OnClick Listener on SignUp button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity
                Intent intentSignUP = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intentSignUP);
            }
        });//


        // Set On ClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            // get the Refferences of views
            EditText editTextUserName = (EditText) findViewById(R.id.editTextUserNameToLogin);
            EditText editTextPassword = (EditText) findViewById(R.id.editTextPasswordToLogin);

            public void onClick(View v) {
                // get The User name and Password


                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                // fetch the Password form database for respective user name
                String storedPassword = loginDataBaseAdapter.getSinlgeEntry(userName);
                Toast.makeText(Login.this, "Pass:"+storedPassword, Toast.LENGTH_LONG).show();
                // check if the Stored password matches with  Password entered by user
                if (password.equals(storedPassword)) {
                    Toast.makeText(Login.this, "Logged in to Weather App..", Toast.LENGTH_LONG).show();
                   Intent intentMain = new Intent(Login.this, SendStats.class);
                   startActivity(intentMain);

                } else {
                    Toast.makeText(Login.this, "Error:User Name or Password does not match!", Toast.LENGTH_LONG).show();
                }
            }
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }
}
