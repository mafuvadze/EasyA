package mafuvadze.anesu.com.codedayapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angellar Manguvo on 11/7/2015.
 */

public class Essays extends Fragment implements FindCallback<ParseObject> {
    FloatingActionMenu menu;
    FloatingActionButton add_copy, add_cam;
    static ListView essayList;
    List<ParseObject> essays = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.essay_list, container, false);
        menu = (FloatingActionMenu) view.findViewById(R.id.menu);
        add_cam = (FloatingActionButton) view.findViewById(R.id.add_cam);
        add_copy = (FloatingActionButton) view.findViewById(R.id.add_copy);
        essayList = (ListView) view.findViewById(R.id.essay_list);
        setCamListener();
        setCopyListener();

        ParseQuery query = new ParseQuery("essays");
        query.findInBackground(this);

        return view;

    }

    private void setCamListener() {
        add_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PictureActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setCopyListener() {
        add_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CopyEssay.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void done(final List<ParseObject> objects, ParseException e) {
        essays = objects;
        ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.single_row_listview, essays);
        essayList.setAdapter(adapter);
        essayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), DisplayEssay.class);
                intent.putExtra("essay", (String) objects.get(position).get("content"));
                intent.putExtra("title", (String) objects.get(position).get("title"));
                intent.putExtra("subject", (String) objects.get(position).get("subject"));
                startActivity(intent);


            }
        });
    }
}


