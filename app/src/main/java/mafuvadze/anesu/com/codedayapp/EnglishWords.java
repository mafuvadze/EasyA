package mafuvadze.anesu.com.codedayapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by Angellar Manguvo on 11/23/2015.
 */
public class EnglishWords extends AsyncTask<Void, Void, Void>
{

    Context context;
    HashSet<String> words;
    ProgressDialog progress;
    public EnglishWords(Context context)
    {
        this.context = context;
        words = new HashSet<>();
        this.execute();
    }

    public boolean isWord(String word)
    {
        String word_ = word.toLowerCase().trim();
        if(word.trim().substring(word.length() - 3).equals("ing"))
        {
            word_ = word.substring(0, word.length() - 3);
        }

        if(word_.substring(word_.length() - 3).equals("ize"))
        {
            word_ = word_.substring(0, word_.length() -3);
        }

        if(word_.substring(word_.length() - 4).equals("izes"))
        {
            word_ = word_.substring(0, word_.length() -4);
        }

        if(word_.substring(word_.length() - 3).equals("ese"))
        {
            word_ = word_.substring(0, word_.length()  -3);
        }

        if(word_ == "I")
        {
            return true;
        }

        if(word_.substring(word.length() - 2).equals("\'s"))
        {
            word_ = word_.substring(0, word.length() -2);
        }
        if(words.contains(word_))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ObjectInputStream scan = null;
        try {
            scan = new ObjectInputStream(context.getResources().openRawResource(R.raw.words_bin));
            while(true)
            {
                words.add((String) scan.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("error", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progress.dismiss();
    }
}
