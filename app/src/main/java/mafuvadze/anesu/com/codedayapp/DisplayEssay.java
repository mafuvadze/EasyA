package mafuvadze.anesu.com.codedayapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class DisplayEssay extends AppCompatActivity implements FindCallback<ParseObject>, SpellCheckerSession.SpellCheckerSessionListener {

    ImageView spell, read, edit, syn, analyze;
    SpannableString ss, spelling_span;
    EditText essay_edit;
    TextView mode_indicator, essay;
    String essay_txt;
    String[] spellSuggestions;
    Mode current_mode = Mode.reading;
    private SpellCheckerSession mScs;
    private static final String TAG = DisplayEssay.class.getSimpleName();
    private static final int NOT_A_LENGTH = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_essay);

        essay = (TextView) findViewById(R.id.essay);
        essay_edit = (EditText) findViewById(R.id.essay_edit);

        initializeModeViews();
        recieveIntent();

        final TextServicesManager tsm = (TextServicesManager) getSystemService(
                Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = tsm.newSpellCheckerSession(null, null, this, true);

    }


    private void recieveIntent() {
        String subject = getIntent().getStringExtra("subject");
        ParseQuery query = new ParseQuery("essays");
        query.whereEqualTo("subject", subject);
        query.findInBackground(this);
    }

    private void initializeModeViews() {
        mode_indicator = (TextView) findViewById(R.id.mode_indicator);
        spell = (ImageView) findViewById(R.id.spell);
        read = (ImageView) findViewById(R.id.read);
        read.setBackground(getResources().getDrawable(R.drawable.rounded_image_view));
        edit = (ImageView) findViewById(R.id.edit);
        syn = (ImageView) findViewById(R.id.syn);
        analyze = (ImageView) findViewById(R.id.analyze);

        spell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_mode = Mode.spelling;
                mode_indicator.setText("Spelling Mode");

                spell.setBackground(getResources().getDrawable(R.drawable.rounded_image_view));
                read.setBackground(getResources().getDrawable(R.drawable.no_border));
                edit.setBackground(getResources().getDrawable(R.drawable.no_border));
                syn.setBackground(getResources().getDrawable(R.drawable.no_border));
                analyze.setBackground(getResources().getDrawable(R.drawable.no_border));

                essay.setText(essay_edit.getText().toString());
                essay.setVisibility(View.VISIBLE);
                essay_edit.setVisibility(View.GONE);
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_mode = Mode.reading;
                mode_indicator.setText("Reading Mode");

                read.setBackground(getResources().getDrawable(R.drawable.rounded_image_view));
                spell.setBackground(getResources().getDrawable(R.drawable.no_border));
                edit.setBackground(getResources().getDrawable(R.drawable.no_border));
                syn.setBackground(getResources().getDrawable(R.drawable.no_border));
                analyze.setBackground(getResources().getDrawable(R.drawable.no_border));

                essay.setText(essay_edit.getText().toString());
                essay.setVisibility(View.VISIBLE);
                essay_edit.setVisibility(View.GONE);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_mode = Mode.editing;
                mode_indicator.setText("Editing Mode");

                edit.setBackground(getResources().getDrawable(R.drawable.rounded_image_view));
                read.setBackground(getResources().getDrawable(R.drawable.no_border));
                spell.setBackground(getResources().getDrawable(R.drawable.no_border));
                syn.setBackground(getResources().getDrawable(R.drawable.no_border));
                analyze.setBackground(getResources().getDrawable(R.drawable.no_border));

                essay_edit.setText(essay.getText().toString());
                essay.setVisibility(View.GONE);
                essay_edit.setVisibility(View.VISIBLE);
            }
        });
        syn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_mode = Mode.synonym;
                mode_indicator.setText("Synonym Mode");

                syn.setBackground(getResources().getDrawable(R.drawable.rounded_image_view));
                read.setBackground(getResources().getDrawable(R.drawable.no_border));
                edit.setBackground(getResources().getDrawable(R.drawable.no_border));
                spell.setBackground(getResources().getDrawable(R.drawable.no_border));
                analyze.setBackground(getResources().getDrawable(R.drawable.no_border));

                essay.setText(essay_edit.getText().toString());
                essay.setVisibility(View.VISIBLE);
                essay_edit.setVisibility(View.GONE);
            }
        });

        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_mode = Mode.analyzing;
                mode_indicator.setText("Analyze Mode");

                analyze.setBackground(getResources().getDrawable(R.drawable.rounded_image_view));
                read.setBackground(getResources().getDrawable(R.drawable.no_border));
                edit.setBackground(getResources().getDrawable(R.drawable.no_border));
                syn.setBackground(getResources().getDrawable(R.drawable.no_border));
                spell.setBackground(getResources().getDrawable(R.drawable.no_border));

                essay.setText(essay_edit.getText().toString());
                essay.setVisibility(View.VISIBLE);
                essay_edit.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        essay.setText((String) objects.get(0).get("content"));
        essay.setMovementMethod(new ScrollingMovementMethod());
        getSupportActionBar().setTitle((String) objects.get(0).get("title"));
        essay_txt = essay.getText().toString();
        essay_edit.setText(essay.getText().toString());
        ss = new SpannableString(essay_txt);
        spelling_span = new SpannableString(essay_txt);
    }

    public void findAllWords() {
        int current = 0;
        int first = 0;
        Map<Integer, Integer> start_end = new HashMap<>();

        for (int i = 0; i < essay.getText().toString().length(); i++)
        {
            current = i;
            if (!isValidCharacter(essay.getText().toString().charAt(i)))
            {
                start_end.put(first, current + 1);
                //String word = essay_txt.substring(first, current);
                first = current + 1;
            }
        }

        for (int i : start_end.keySet()) {

            if(essay.getText().toString() == null)
            {
                Toast.makeText(this, "essay is null", Toast.LENGTH_LONG).show();
            }
                ss.setSpan(new MyClickableSpan(essay.getText().toString(), i, start_end.get(i)), i, start_end.get(i), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                fetchSuggestions(essay.getText().toString().substring(i, start_end.get(i)));
                spelling_span.setSpan(new SpellCheckSpan(essay_txt, i, start_end.get(i), spellSuggestions), i, start_end.get(i), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        }

    }

    public boolean isValidCharacter(char ch) {
        String str = ch + "";
        char c = str.toLowerCase().charAt(0);
        if (((int) 'a' <= (int) c) && (int) 'z' >= (int) c) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onGetSuggestions(SuggestionsInfo[] arg0) {
        Log.d(TAG, "onGetSuggestions");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arg0.length; ++i) {
            dumpSuggestionsInfoInternal(sb, arg0[i], 0, NOT_A_LENGTH);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //will write code later
            }
        });
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] arg0) {
        if (!isSentenceSpellCheckSupported()) {
            Log.e(TAG, "Sentence spell check is not supported on this platform, "
                    + "but accidentially called.");
            return;
        }
        Log.d(TAG, "onGetSentenceSuggestions");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arg0.length; ++i) {
            final SentenceSuggestionsInfo ssi = arg0[i];
            for (int j = 0; j < ssi.getSuggestionsCount(); ++j) {
                dumpSuggestionsInfoInternal(
                        sb, ssi.getSuggestionsInfoAt(j), ssi.getOffsetAt(j), ssi.getLengthAt(j));
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] suggestedWords = sb.toString().split(", ");
                spellSuggestions = suggestedWords;
            }
        });
    }


    private boolean isSentenceSpellCheckSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    @Override
    public void onResume() {
        super.onResume();
        final TextServicesManager tsm = (TextServicesManager) getSystemService(
                Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = tsm.newSpellCheckerSession(null, null, this, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScs != null) {
            mScs.close();
        }
    }

    private void dumpSuggestionsInfoInternal(
            final StringBuilder sb, final SuggestionsInfo si, final int length, final int offset) {
        // Returned suggestions are contained in SuggestionsInfo
        final int len = si.getSuggestionsCount();
        sb.append('\n');
        for (int j = 0; j < len; ++j) {
            if (j != 0) {
                sb.append(", ");
            }
            sb.append(si.getSuggestionAt(j));
        }
        sb.append(" (" + len + ")");
        if (length != NOT_A_LENGTH) {
            //sb.append(" length = " + length + ", offset = " + offset);
        }
    }

    public void fetchSuggestions(String word) {
        try {
            mScs.getSentenceSuggestions(new TextInfo[]{new TextInfo(word)}, 6);
        }catch(Exception e)
        {
            Toast.makeText(this, word + "not searcheable", Toast.LENGTH_LONG).show();
        }
    }

    enum Mode {
        spelling, reading, editing, synonym, analyzing;

        Mode() {
        }
    }

    class MyClickableSpan extends ClickableSpan {
        String essay, response;
        int start, end;
        String word;

        MyClickableSpan(String essay, int start, int end) {
            this.essay = essay;
            this.start = start;
            this.end = end;
            word = essay.substring(start, end);

        }

        @Override
        public void onClick(View widget) {
            HttpRequest request = new HttpRequest(DisplayEssay.this);
            String url = "http://api.wordnik.com:80/v4/word.json/" + word.trim() + "/relatedWords?useCanonical=false&relationshipTypes=synonym&limitPerRelationshipType=10&api_key=7c6ee010f214172ad52050ab7c70d570e9140f374466d026f";
            request.fetchStringResponse(url);
            response = request.getStringResponse();

            Dialog dialog = new Dialog(DisplayEssay.this);
            dialog.setTitle("Suggestions");
            dialog.setContentView(R.layout.word_suggestion_layout);

            ListView list = (ListView) dialog.findViewById(R.id.wordList);

            ParseJSONRequest parseJSONRequest = new ParseJSONRequest(DisplayEssay.this);
            String[] suggestedWords = parseJSONRequest.getWordsArray(response);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(DisplayEssay.this, android.R.layout.simple_list_item_1, suggestedWords);
            list.setAdapter(adapter);
            dialog.show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    class SpellCheckSpan extends ClickableSpan {
        String essay, word;
        int start, end;
        String[] suggestions;

        SpellCheckSpan(String essay, int start, int end, String[] suggestions) {
            this.essay = essay;
            this.start = start;
            this.end = end;
            this.suggestions = suggestions;
            word = essay.substring(start, end);
        }

        @Override
        public void onClick(View widget) {
            if (suggestions != null && suggestions.toString() != "[]") {
                Dialog dialog = new Dialog(DisplayEssay.this);
                dialog.setTitle("Did you mean...");
                dialog.setContentView(R.layout.word_suggestion_layout);

                ListView list = (ListView) dialog.findViewById(R.id.wordList);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DisplayEssay.this, android.R.layout.simple_list_item_1, suggestions);
                list.setAdapter(adapter);
                dialog.show();
            }
        }
    }
}

