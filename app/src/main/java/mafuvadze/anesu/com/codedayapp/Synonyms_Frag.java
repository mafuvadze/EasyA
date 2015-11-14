package mafuvadze.anesu.com.codedayapp;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.bartoszlipinski.flippablestackview.FlippableStackView;

/**
 * Created by Angellar Manguvo on 11/7/2015.
 */
public class Synonyms_Frag extends Fragment
{
    FlippableStackView flipper;
    @Nullable
    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.synonyms_page, null, false);
        /*
        flipper = (FlippableStackView) v.findViewById(R.id.stack);
        flipper.initStack(2);
        FlipperAdapter adapter = new FlipperAdapter(getContext());
        flipper.setAdapter(adapter);
        */
        return v;

    }


}
