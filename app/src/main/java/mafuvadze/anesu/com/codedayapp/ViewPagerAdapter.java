package mafuvadze.anesu.com.codedayapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Angellar Manguvo on 11/7/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter
{

    String[] titles = new String[3];
    int numTabs;
    public ViewPagerAdapter(FragmentManager fm, String[] titles, int numTabs) {
        super(fm);
        this.titles = titles;
        this.numTabs = numTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = titles[position];
        return title;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            return new Synonyms_Frag();
        }
        else if(position == 1)
        {
            return new Mistakes_Frag();
        }
        else
        {
            return new Report_Frag();
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
