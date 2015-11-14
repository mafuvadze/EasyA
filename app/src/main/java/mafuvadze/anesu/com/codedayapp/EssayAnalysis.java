package mafuvadze.anesu.com.codedayapp;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EssayAnalysis extends AppCompatActivity {

    String[] titles = new String[]{"Synonyms", "Mistakes", "Report"};
    SlidingTabLayout slidingTabLayout;
    ViewPagerAdapter adapter;
    ViewPager pager;
    String essay = "";
    String title = "";
    String subject = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay_analysis);

        essay = (String) getIntent().getStringExtra("essay");
        title = (String) getIntent().getStringExtra("title");
        subject = (String) getIntent().getStringExtra("subject");
        VariableHolder holder = new VariableHolder();
        holder.title = title;
        holder.subject = subject;
        holder.essay = essay;


        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, 3);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.red);
            }
        });

        slidingTabLayout.setViewPager(pager);
    }
}
