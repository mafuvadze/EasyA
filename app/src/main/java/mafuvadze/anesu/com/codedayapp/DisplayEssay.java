package mafuvadze.anesu.com.codedayapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayEssay extends AppCompatActivity implements FindCallback<ParseObject>{

    TextView txt, title;
    List<String> transition_words = new ArrayList<>();
    int adv = 0, simple = 0, mid = 0, transitions = 0, total_words = 0;
    Button btn;
    String essay;
    SpannableString ss;
    Map<String, Integer> words = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_essay);

        transition_words.add("however");
        transition_words.add("although");
        transition_words.add("but");
        transition_words.add("such as");
        transition_words.add("furthermore");
        transition_words.add("also");
        transition_words.add("while");
        transition_words.add("albeit");
        transition_words.add("granted");
        transition_words.add("therefore");
        transition_words.add("thus");
        transition_words.add("overall");



        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(DisplayEssay.this, show.class);
                startActivity(intent);
            }
        });
        txt = (TextView) findViewById(R.id.display);
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setHighlightColor(Color.TRANSPARENT);

        String title = getIntent().getStringExtra("title");
        String subject = getIntent().getStringExtra("subject");
        ParseQuery query = new ParseQuery("essays");
        query.whereEqualTo("subject", subject);
        query.findInBackground(this);

    }

    private void scanEssay()
    {
        String mostUsedWord;
        int top = 0;
        String w = "";
        for(String word : words.keySet())
        {
            int i = words.get(word);
            if(i >= top)
            {
                w = word;
            }
        }

        mostUsedWord = w;

    }


    public void findAllWords()
    {
        int current = 0;
        int first = 0;
        Map<Integer, Integer> start_end = new HashMap<>();
        for(int i = 0; i < essay.length(); i++)
        {
            if(isValidCharacter(essay.charAt(current)))
            {
                current++;
            }
            else
            {
                total_words++;
                start_end.put(first, current + 1);
                String word = essay.substring(first, current);
                if(transition_words.contains(word))
                {
                    transitions++;
                }
                if(word.length() < 4)
                {
                    simple++;
                }
                else if(word.length() >= 4 && word.length() <= 6)
                {
                    mid++;
                }
                else
                {
                    adv++;
                }
                if(words.containsKey(word) && word.length() > 3)
                {
                    int n = words.get(word);
                    words.put(word, n++);
                }
                else if(word.length() > 3) {
                    words.put(word, 1);
                }
                first = current + 1;
                current++;
            }
        }

        for(int i : start_end.keySet())
        {
            ss.setSpan(new MyClickableSpan(essay, i, start_end.get(i)), i, start_end.get(i), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        txt.setText(ss);
    }

    public boolean isValidCharacter(char ch)
    {
        String str = ch + "";
        char c = str.toLowerCase().charAt(0);
        if(((int) 'a' <= (int) c) && (int) 'z' >= (int) c)
        {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        txt.setText((String) objects.get(0).get("content"));
        essay = (String) objects.get(0).get("content");
        ss = new SpannableString(essay);
        title = (TextView) findViewById(R.id.title);
        title.setText((String) objects.get(0).get("title"));
                findAllWords();
    }

    class MyClickableSpan extends ClickableSpan
    {
        String essay;
        int start, end;
        String word;
        MyClickableSpan(String essay, int start, int end)
        {
            this.essay = essay;
            this.start = start;
            this.end = end;
            word = essay.substring(start, end-1);

        }
        @Override
        public void onClick(View widget) {
            String url = "http://api.wordnik.com:80/v4/word.json/" + word.trim() + "/relatedWords?useCanonical=false&relationshipTypes=synonym&limitPerRelationshipType=10&api_key=7c6ee010f214172ad52050ab7c70d570e9140f374466d026f";
            RequestQueue queue = Volley.newRequestQueue(DisplayEssay.this);
            StringRequest request = new StringRequest(Request.Method.GET, url.toString().trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject obj = array.getJSONObject(0);
                        JSONArray words = obj.getJSONArray("words");
                        Dialog dialog = new Dialog(DisplayEssay.this);
                        dialog.setTitle("Suggestions");
                        dialog.setContentView(R.layout.word_suggestion_layout);
                        ListView list = (ListView) dialog.findViewById(R.id.wordList);
                        String[] w = new String[words.length()];
                        for(int i = 0; i < words.length(); i++)
                        {
                            w[i] = (String) words.get(i);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DisplayEssay.this, android.R.layout.simple_list_item_1, w);
                        list.setAdapter(adapter);
                        dialog.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DisplayEssay.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            queue.add(request);

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
