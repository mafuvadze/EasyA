package mafuvadze.anesu.com.codedayapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CopyEssay extends AppCompatActivity {

    EditText essay_txt, title, subject;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_essay);

        initializeViews();
    }

    private void submitEssay()
    {
        final ProgressDialog progress = new ProgressDialog(CopyEssay.this);
        progress.setMessage(getString(R.string.please_wait_message));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
        String t = title.getText().toString();
        String s = subject.getText().toString();
        ParseObject essay = new ParseObject("essays");
        essay.put("title", t);
        essay.put("subject", s);
        essay.put("content", essay_txt.getText().toString());
        essay.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    progress.dismiss();
                    displaySnackbar();
                    Intent intent = new Intent(CopyEssay.this, HomeScreen.class);
                    startActivity(intent);
                }
            }

        });
    }

    private void displaySnackbar()
    {
        View parentView = (View) title.getParent();
        Snackbar.make(parentView
                , title.getText().toString() + " succesfully uploaded"
                , Snackbar.LENGTH_LONG)
                .show();
    }

    private void initializeViews()
    {
        essay_txt =(EditText) findViewById(R.id.essay);
        title =(EditText) findViewById(R.id.title);
        subject =(EditText) findViewById(R.id.subject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_copy_essay, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.confirm)
        {
            submitEssay();
        }

        return super.onOptionsItemSelected(item);
    }
}
