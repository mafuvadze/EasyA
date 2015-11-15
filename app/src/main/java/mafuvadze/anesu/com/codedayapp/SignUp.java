package mafuvadze.anesu.com.codedayapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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
    TextInputLayout user_lay, email_lay, pass_one_lay, pass_two_lay;
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
        user_lay = (TextInputLayout) findViewById(R.id.input_username);
        email_lay = (TextInputLayout) findViewById(R.id.input_email);
        pass_one_lay = (TextInputLayout) findViewById(R.id.input_pass);
        pass_two_lay = (TextInputLayout) findViewById(R.id.input_confirm_pass);

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
        boolean error = false;
        //possible errors
        String usernameFormat = getString(R.string.usernameFormat);
        String incompleteForm = getString(R.string.incompleteFormat);
        String invalidEmail = getString(R.string.invalidEmail);
        String invalidPassword = getString(R.string.invalidPassword);
        String passwordShort = getString(R.string.passwordShort);

        //Make sure that the sign up form is completely filled out
        if(username.getText().toString().equals("") &&
                email.getText().toString().equals("") &&
                pass_one.getText().toString().equals("") &&
                pass_two.getText().toString().equals(""))
        {
            error = true;
        }


        //check if username is empty
        if(username.getText().toString().trim().equals(""))
        {
            user_lay.setError("Username cannot be empty");
            error = true;
        }
        else
        {
            user_lay.setErrorEnabled(false);
        }

        //check if email is empty
        if(email.getText().toString().trim().equals(""))
        {
            email_lay.setError("Email cannot be empty");
            error = true;
        }
        else
        {
            email_lay.setErrorEnabled(false);
        }

        //check if passwords are empty
        if(pass_one.getText().toString().trim().equals(""))
        {
            pass_one_lay.setError("Password cannot be empty");
            error = true;
        }
        else
        {
            pass_one_lay.setErrorEnabled(false);
        }

        if(pass_two.getText().toString().trim().equals(""))
        {
            pass_two_lay.setError("Password cannot be empty");
            error = true;
        }
        else
        {
            pass_two_lay.setErrorEnabled(false);
        }

        //validate username
        if(username.getText().toString().length() <= 3)
        {
            user_lay.setError(usernameFormat);
            error = true;
        }
        else
        {
            user_lay.setErrorEnabled(false);
        }

        //validate email
        if(!email.getText().toString().contains("@") || !email.getText().toString().contains(".com"))
        {
            email_lay.setError(invalidEmail);
            error = true;
        }
        else
        {
            email_lay.setErrorEnabled(false);
        }

        //validate password
        if(!pass_one.getText().toString().equals(pass_two.getText().toString()))
        {
            pass_one_lay.setError(invalidPassword);
            pass_two_lay.setError(invalidPassword);
            error = true;
        }
        else
        {
            pass_two_lay.setErrorEnabled(false);
            pass_one_lay.setErrorEnabled(false);
        }

        if(pass_one.getText().toString().length() <= 6)
        {
            pass_one_lay.setError(passwordShort);
            error = true;
        }
        else
        {
            pass_one_lay.setErrorEnabled(false);
        }

        //if everything is all alright
        // Save new user data into Parse.com Data Storage
        if(!error) {
            //username and email are intentionally switched
            ParseUser user = new ParseUser();
            user.setUsername(email.getText().toString().toLowerCase());
            user.setPassword(pass_one.getText().toString());
            user.put("handle", username.getText().toString());

            //logout any account that might still be logged in
            try
            {
                ParseUser.getCurrentUser().logOut();
            }
            catch(Exception e)
            {

            }

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        try {
                            // Show a simple Toast message upon successful registration
                            Toast.makeText(getApplicationContext(),
                                    "account created",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                            return;
                        } catch (Exception error) {
                            //left empty because of unexplained crash after sign up
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                e.toString(), Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                }
            });
        }



    }

}
