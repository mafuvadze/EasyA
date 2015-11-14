package mafuvadze.anesu.com.codedayapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {

    Button create_account_btn;
    EditText username, email, pass_one, pass_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        create_account_btn = (Button) findViewById(R.id.creat_account_btn);
        username = (EditText) findViewById(R.id.enter_username);
        email = (EditText) findViewById(R.id.create_email);
        pass_one = (EditText) findViewById(R.id.create_pass);
        pass_two = (EditText) findViewById(R.id.confirm_pass);

        setOnCreateListener();
    }

    public void setOnCreateListener()
    {
        create_account_btn.setOnClickListener
                (
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    createNewAccount();
                                } catch (Exception error) {
                                    //causes crash, but still creates account
                                }
                            }
                        }
                );
    }


    public void createNewAccount()
    {
        //possible errors
        String usernameFormat = getString(R.string.usernameFormat);
        String incompleteForm = getString(R.string.incompleteFormat);
        String invalidEmail = getString(R.string.invalidEmail);
        String invalidPassword = getString(R.string.invalidPassword);
        String passwordShort = getString(R.string.passwordShort);

        //Make sure that the sign up form is completely filled out
        if(username.getText().toString().equals("") ||
                email.getText().toString().equals("") ||
                pass_one.getText().toString().equals("") ||
                pass_two.getText().toString().equals(""))
        {
            Toast.makeText(SignUp.this, incompleteForm, Toast.LENGTH_SHORT).show();
            return;
        }

        //validate username
        if(username.getText().toString().length() <= 3)
        {
            Toast.makeText(SignUp.this, usernameFormat, Toast.LENGTH_SHORT).show();
            return;
        }

        //validate email
        if(!email.getText().toString().contains("@") || !email.getText().toString().contains(".com"))
        {
            Toast.makeText(SignUp.this, invalidEmail, Toast.LENGTH_SHORT).show();
            return;
        }

        //validate password
        if(!pass_one.getText().toString().equals(pass_two.getText().toString()))
        {
            Toast.makeText(SignUp.this, invalidPassword, Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass_one.getText().toString().length() <= 6)
        {
            Toast.makeText(SignUp.this, passwordShort, Toast.LENGTH_SHORT).show();
            return;
        }

        //if everything is all alright
        // Save new user data into Parse.com Data Storage

        //username and email are intentionally switched
        ParseUser user = new ParseUser();
        user.setUsername(email.getText().toString().toLowerCase());
        user.setPassword(pass_one.getText().toString());
        user.put("handle", username.getText().toString());

        user.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(com.parse.ParseException e)
            {
                if (e == null)
                {
                    try
                    {
                        // Show a simple Toast message upon successful registration
                        Toast.makeText(getApplicationContext(),
                                "account created",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);
                        return;
                    }
                    catch (Exception error)
                    {
                        //left empty because of unexplained crash after sign up
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "account creating failed", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
            }
        });



    }

}
