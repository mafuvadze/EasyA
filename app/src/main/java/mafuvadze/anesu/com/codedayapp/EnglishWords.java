package mafuvadze.anesu.com.codedayapp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by Angellar Manguvo on 11/23/2015.
 */
public class EnglishWords
{

    Context context;
    HashSet<String> words;
    public EnglishWords(Context context)
    {
        this.context = context;
        words = new HashSet<>();
        Scanner scanner  = new Scanner(context.getResources().openRawResource(R.raw.words));
        while(scanner.hasNextLine())
        {
            words.add(scanner.nextLine().trim().toLowerCase());
        }
        scanner.close();
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
