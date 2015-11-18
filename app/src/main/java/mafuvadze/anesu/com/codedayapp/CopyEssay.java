package mafuvadze.anesu.com.codedayapp;

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

    EditText essay_txt;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_essay);
        /*

        essay_txt = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.submit_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CopyEssay.this);
                dialog.setTitle("Upload Essay");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.create_essay);
                dialog.show();

                final EditText title  = (EditText) dialog.findViewById(R.id.title);
                final EditText subject  = (EditText) dialog.findViewById(R.id.subject);
                Button enter_btn = (Button) dialog.findViewById(R.id.submit);

                enter_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progress = new ProgressDialog(CopyEssay.this);
                        progress.setMessage(getString(R.string.please_wait_message));
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setCancelable(false);
                        progress.show();
                        dialog.dismiss();
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
                                }
                                else
                                {
                                    progress.dismiss();
                                    Toast.makeText(CopyEssay.this, "Essay uploaded", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(CopyEssay.this, HomeScreen.class);
                                    startActivity(intent);
                                }
                            }

                        });
                    }
                });
            }
        });
        */
    }
}
