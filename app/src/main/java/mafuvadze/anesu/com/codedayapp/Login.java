package mafuvadze.anesu.com.codedayapp;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
        setSignUpListener();
        setLoginListener();
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
