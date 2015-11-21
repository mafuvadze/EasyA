package mafuvadze.anesu.com.codedayapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angellar Manguvo on 11/7/2015.
 */
public class ListViewAdapter extends ArrayAdapter<String> {
    Context context;
    List<ParseObject> essays = new ArrayList<>();
    int prev = 0;
    FloatingActionMenu menu;

    public ListViewAdapter(Context context, int resource, List<ParseObject> essays, FloatingActionMenu menu) {
        super(context, resource);
        this.context = context;
        this.essays = essays;
        this.menu = menu;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_row_listview, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView subject = (TextView) convertView.findViewById(R.id.subject);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

        String grade = essays.get(position).getString("grade");
        if (grade == null) {
            grade = "B";
        }
        switch (grade) {
            case "A":
                icon.setImageResource(R.mipmap.a);
                break;
            case "B":
                icon.setImageResource(R.mipmap.b);
                break;
            case "C":
                icon.setImageResource(R.mipmap.c);
                break;
            default:
                break;
        }

        title.setText(essays.get(position).getString("title"));
        subject.setText(essays.get(position).getString("subject"));
        final String essay = essays.get(position).getString("content");

        ObjectAnimator anim = ObjectAnimator.ofFloat(convertView, "translationY", prev > position ? -250 : 250, 0);

        if (prev < position && position >= 4) {
            menu.hideMenu(true);
        } else {
            menu.showMenu(true);
        }

        anim.setDuration(400);
        anim.start();

        prev = position;

        return convertView;
    }

    @Override
    public int getCount() {
        return essays.size();
    }
}
