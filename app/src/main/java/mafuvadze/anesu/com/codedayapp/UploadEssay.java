package mafuvadze.anesu.com.codedayapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.Date;

/**
 * Created by Angellar Manguvo on 11/18/2015.
 */
public class UploadEssay
{
    Context context;
    public UploadEssay(Context context)
    {
        this.context = context;
    }

    public void upload(String title, String subject, String essay_txt)
    {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Uploading essay...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
        ParseObject essay = new ParseObject("essays");
        essay.put("title", title);
        essay.put("subject", subject);
        essay.put("content", essay_txt);
        essay.put("date", new Date().toString());
        essay.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    progress.dismiss();
                }
            }

        });
    }
}
