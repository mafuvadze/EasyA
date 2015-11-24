package mafuvadze.anesu.com.codedayapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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
public class EnglishWords
{

    Context context;
    HashSet<String> words;
    ProgressDialog progress;
    public EnglishWords(Context context)
    {
        this.context = context;
        words = new HashSet<>();
        progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
        ObjectInputStream scan = null;
        try {
            scan = new ObjectInputStream(context.getResources().openRawResource(R.raw.words_bin));
            while(true)
            {
                words.add((String) scan.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        progress.dismiss();
    }

    public boolean isWord(String word)
    {
        if(words.contains(word.toLowerCase()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
