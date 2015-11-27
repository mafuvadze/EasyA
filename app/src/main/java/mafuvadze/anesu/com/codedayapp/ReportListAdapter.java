package mafuvadze.anesu.com.codedayapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Angellar Manguvo on 11/27/2015.
 */
public class ReportListAdapter extends ArrayAdapter<String>
{
    private final String SPELLING_ERROR = "spelling";
    private final String GRAMMAR_ERROR = "grammar";
    private final String PUNCTUATION_ERROR = "punctuation";
    private final String DETAILS = "detail";
    List<String> identifier;
    List<Object> error;
    Context context;
    public ReportListAdapter(Context context, int resource, HashMap<String, List<Object>> error) {
        super(context, resource);
        this.context = context;
        identifier = new ArrayList<String>();
        this.error = new ArrayList<Object>();
        for(String id : error.keySet())
        {
            identifier.add(id);
            this.error.add(error.get(id));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch ("spelling")
        {
            case "spelling":
            {
                if(convertView == null)
                {
                    convertView = LayoutInflater.from(context)
                            .inflate(R.layout.spelling_error_row, parent, false);
                }

                String err = (String) error.get(position);
                String txt_err = err + " may be misspelled";
                TextView err_view = (TextView) convertView.findViewById(R.id.mispelled);
                err_view.setText(txt_err);
                Log.i("worked", err + " dookie");
                return convertView;

            }
            case GRAMMAR_ERROR:
            {

            }
            case PUNCTUATION_ERROR:
            {

            }
            case DETAILS:
            {

            }
        }

        return null;

    }
}
