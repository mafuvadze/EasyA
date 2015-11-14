package mafuvadze.anesu.com.codedayapp;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Angellar Manguvo on 11/7/2015.
 */
public class Mistakes_Frag extends Fragment
{
    @Nullable
    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.mistakes_page, null, false);
        return v;
    }
}
