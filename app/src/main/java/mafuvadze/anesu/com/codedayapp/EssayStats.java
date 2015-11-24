package mafuvadze.anesu.com.codedayapp;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Angellar Manguvo on 11/24/2015.
 */
public class EssayStats
{
    String essay;
    Context context;
    Scanner scan_trans;
    String[] words;
    List<String> trans = new ArrayList<>();
    public EssayStats(String essay, Context context)
    {
        this.essay = essay;
        this.context = context;
        words = essay.split(" ");
        Arrays.sort(words);
        scan_trans = new Scanner(context.getResources().openRawResource(R.raw.transition_words));
        while(scan_trans.hasNextLine())
        {
            trans.add(scan_trans.nextLine().trim().toLowerCase());
        }
    }

    public int getWordCount()
    {
        return words.length;
    }

    public int getTransitioWords()
    {
        int count = 0;
        for(String word : words)
        {
            if(trans.contains(word.toLowerCase()))
            {
                count++;
            }
        }

        return count;
    }

    public List<String> mostUsedWords()
    {
        int count = 1;
        int highest = 0, highest2 = 0, highest3 = 0;
        String word = "", word2 = "", word3 = "";
        String prev = "";

        for(String w : words)
        {
           if(w == prev && w.length() > 3)
           {
               count++;
               prev = w;
               if(count >= highest3 && count < highest2)
               {
                   highest3 = count;
                   word3 = w;
               }
               else if(count >= highest3 && count < highest)
               {
                   highest2 = count;
                   word2 = w;
               }
               else if(count >= highest)
               {
                   highest = count;
                   word3 = w;
               }
           }
           else
           {
               count = 1;
           }
        }

        List<String> mostUsed = new ArrayList<>(Arrays.asList(word, word2, word2));
        return mostUsed;
    }
}
