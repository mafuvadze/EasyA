package mafuvadze.anesu.com.codedayapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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
    static SwipeMenuListView essayList;
    List<ParseObject> essays = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.essay_list, container, false);
        menu = (FloatingActionMenu) view.findViewById(R.id.menu);
        add_cam = (FloatingActionButton) view.findViewById(R.id.add_cam);
        add_copy = (FloatingActionButton) view.findViewById(R.id.add_copy);
        essayList = (SwipeMenuListView) view.findViewById(R.id.essay_list);
        setCamListener();
        setCopyListener();

        getEssays();
        createMenu();
        setListViewListener();

        return view;

    }

    private void getEssays()
    {
        ParseQuery query = new ParseQuery("essays");
        query.findInBackground(this);
    }

    private void setListViewListener() {
        essayList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Intent intent = new Intent(getActivity(), DisplayEssay.class);
                        intent.putExtra("essay", (String) essays.get(position).get("content"));
                        intent.putExtra("title", (String) essays.get(position).get("title"));
                        intent.putExtra("subject", (String) essays.get(position).get("subject"));
                        startActivity(intent);
                        break;
                    case 1:
                        // delete
                        final String essayTitle = (String) essays.get(position).get("title");
                        new AlertDialog.Builder(getActivity())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to delete " + essayTitle)
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ParseQuery<ParseObject> query = new ParseQuery("essays");
                                        query.whereEqualTo("title", essayTitle);
                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                                if(e==null) {


                                                    for (ParseObject delete : parseObjects) {
                                                        try {
                                                            delete.deleteInBackground().waitForCompletion();
                                                        } catch (InterruptedException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                        getEssays();
                                                        Snackbar.make((View) essayList.getParent(), essayTitle + " was deleted", Snackbar.LENGTH_SHORT).show();
                                                    }
                                                }else{
                                                    Toast.makeText(getActivity().getBaseContext(), "error in deleting", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                })
                                .setNegativeButton("Keep", null)
                                .show();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    private void createMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplication().getBaseContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                openItem.setWidth(essayList.getWidth()/4);
                openItem.setTitle("Open");
                openItem.setTitleSize(19);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplication().getBaseContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(essayList.getWidth()/4);
                deleteItem.setTitle("Delete");
                deleteItem.setTitleSize(19);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        essayList.setMenuCreator(creator);// Close Interpolator
        essayList.setCloseInterpolator(new OvershootInterpolator());
    }

    private void setCamListener() {
        add_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                Intent intent = new Intent(getActivity(), PictureActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setCopyListener() {
        add_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                Intent intent = new Intent(getActivity(), CopyEssay.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void done(final List<ParseObject> objects, ParseException e) {
        essays = objects;
        ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.single_row_listview, essays, menu);
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


