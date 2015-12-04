package mafuvadze.anesu.com.codedayapp;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Login extends AppCompatActivity {

    EditText email, pass;
    Button login;
    TextView sign_up;
    ProgressDialog progress;
    RelativeLayout holder;
    CheckBox remember;
    DBHelper database;
    TextInputLayout pass_inp, email_inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "eLdYeAiMGV0I4bb3zi8jJV2MyccqzYxozVA0ieT0", "QB5fo42Ybh5GvpoHBJXBZr9tXk2N6CpQqOUeadYn");

        getSupportActionBar().setHomeButtonEnabled(true);

        email = (EditText) findViewById(R.id.enter_email);
        pass = (EditText) findViewById(R.id.enter_pass);
        login = (Button) findViewById(R.id.login_btn);
        sign_up = (TextView) findViewById(R.id.signUp_txt);
        holder = (RelativeLayout) findViewById(R.id.rl);
        remember = (CheckBox) findViewById(R.id.remember);
        pass_inp = (TextInputLayout) findViewById(R.id.input_pass);
        email_inp = (TextInputLayout) findViewById(R.id.input_email);
        setSignUpListener();
        setLoginListener();
        setUpAutoLogin();
    }

    private void setUpAutoLogin()
    {
        if(checkIfLoginSaved())
        {

            remember.setChecked(true);
            String email_txt = database.getEmail();
            String pass_txt = database.getPass();

            email.setText(email_txt);
            pass.setText(pass_txt);
            email.setBackgroundColor(getResources().getColor(R.color.Hightlight));
            pass.setBackgroundColor(getResources().getColor(R.color.Hightlight));
            email_inp.setBackgroundColor(getResources().getColor(R.color.Hightlight));
            pass_inp.setBackgroundColor(getResources().getColor(R.color.Hightlight));

        }
    }

    private boolean checkIfLoginSaved()
    {
        database = new DBHelper(this);
        try{
            String email = database.getEmail();
            if(email.equals(""))
            {
                return false;
            }
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    private void setSignUpListener()
    {
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void setLoginListener()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(Login.this);
                progress.setMessage(getString(R.string.please_wait_message));
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setCancelable(false);
                progress.show();
                verifyLogin(true);
            }
        });
    }

    public void verifyLogin(boolean statement) {
        final boolean manual = statement;

        ParseUser.logInInBackground(email.getText().toString().toLowerCase(), pass.getText().toString(),
                new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, com.parse.ParseException e) {
                        if (parseUser != null) {
                            if (progress != null) {
                                progress.dismiss();
                                progress = null;
                            }

                            String p = pass.getText().toString();
                            String u = email.getText().toString();


                            try {
                                Intent intent = new Intent(Login.this, HomeScreen.class);
                                intent.putExtra("login", "yes");
                                if(remember.isChecked())
                                {
                                    database.addUser(email.getText().toString(), pass.getText().toString(), "user");
                                    database.updateUserEmail(email.getText().toString());
                                    database.updateUserPass(pass.getText().toString());
                                }
                                else
                                {
                                    database.addUser(email.getText().toString(), pass.getText().toString(), "user");
                                    database.updateUserEmail("");
                                    database.updateUserPass("");
                                }
                                startActivity(intent);
                            } catch (Exception error) {
                                Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                ObjectAnimator anim = ObjectAnimator.ofFloat(holder, "translationX", 100,0,-70,0,60,0,-60,0,60,0,-60,0);
                                anim.setDuration(500);
                                anim.start();
                            }

                        } else {
                            if (manual)
                            {
                                if (progress != null) {
                                    progress.dismiss();
                                    progress = null;
                                }

                                Toast.makeText(
                                        getApplicationContext(),
                                        "authentication failed",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                pass.clearComposingText();
                                email.clearComposingText();
                            }
                        }

                    }


                });
    }

}
