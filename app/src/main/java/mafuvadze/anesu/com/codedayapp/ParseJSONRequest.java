package mafuvadze.anesu.com.codedayapp;

import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Angellar Manguvo on 11/17/2015.
 */
public class ParseJSONRequest
{
    Context context;
    public ParseJSONRequest(Context context)
    {
        this.context = context;
    }

    public String[] getWordsArray(String response)
    {
        try {
            JSONArray array = new JSONArray(response);
            JSONObject obj = array.getJSONObject(0);
            JSONArray words = obj.getJSONArray("words");

            String[] w = new String[words.length()];
            for (int i = 0; i < words.length(); i++) {
                w[i] = (String) words.get(i);
            }

            return w;

        } catch (Exception e) {
            Toast.makeText(context, "something went wrong", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
