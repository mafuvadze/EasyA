package mafuvadze.anesu.com.codedayapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Angellar Manguvo on 11/7/2015.
 */
public class FlipperAdapter extends PagerAdapter {

Context context;
    String essay, title, subject;
    public FlipperAdapter(Context context, String title, String subject, String essay) {
        this.context = context;
        this.essay = essay;
        this.title = title;
        this.subject = subject;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.single_page_synonyms, null, false);
        TextView tv = (TextView) ll.findViewById(R.id.text);
        if(position == 0)
        {
            tv.setText(title + "\n" + subject);
        }

        if(position == 1)
        {
            tv.setText(essay);
        }

        container.addView(ll);
        return ll;
    }


}
