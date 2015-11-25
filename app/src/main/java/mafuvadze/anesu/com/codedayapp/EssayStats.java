package mafuvadze.anesu.com.codedayapp;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Created by Angellar Manguvo on 11/24/2015.
 */
public class EssayStats
{
    String essay;
    Context context;
    Scanner scan_trans;
    List<String> words;
    List<String> trans = new ArrayList<>();
    HashSet<String> words_set;
    public EssayStats(String essay, Context context)
    {
        this.essay = essay;
        this.context = context;
        words = Arrays.asList(essay.split(" "));
        Collections.sort(words);
        words_set = new HashSet<>();
        for(String word : words)
        {
            if(!word.trim().equals("")) {
                words_set.add(word.toLowerCase());
            }
        }
        scan_trans = new Scanner(context.getResources().openRawResource(R.raw.transition_words));
        while(scan_trans.hasNextLine())
        {
            trans.add(scan_trans.nextLine().trim().toLowerCase());
        }
    }

    private String trimEnding(String word)
    {
        char last = word.charAt(word.length() - 1);
        if(last == ':' || last == ',' || last =='.')
        {
            return word.substring(0,word.length() - 1);
        }
        else
        {
            return word;
        }
    }

    public int getWordCount()
    {
        return words.size();
    }

    public int getTransitionWords()
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

    public List<SingleWord> mostUsedWords()
    {
        Comparator<SingleWord> comparator = new Comparator<SingleWord>() {
            @Override
            public int compare(SingleWord lhs, SingleWord rhs) {
                return Integer.compare(rhs.freq, lhs.freq);
            }
        };

        PriorityQueue<SingleWord> queue = new PriorityQueue<>(words.size(),comparator);
        for(String word : words_set)
        {
            if(word.length() > 3) {
                SingleWord w = new SingleWord(trimEnding(word), Collections.frequency(words, trimEnding(word)));
                queue.add(w);
            }
        }

        List<SingleWord> results = new ArrayList<>();
        for(int i = 0; i < 5; i++)
        {
            results.add(queue.poll());
        }

        return results;
    }
}
