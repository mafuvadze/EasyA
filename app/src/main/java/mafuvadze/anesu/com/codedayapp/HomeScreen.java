package mafuvadze.anesu.com.codedayapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class HomeScreen extends AppCompatActivity {

    ImageView profile_pic;
    TextView username;
    Button essays_btn, stats_btn;
    Boolean essayFragShown = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        profile_pic = (ImageView) findViewById(R.id.profilePic);
        username = (TextView) findViewById(R.id.user_name);
        essays_btn = (Button) findViewById(R.id.essays);
        stats_btn = (Button) findViewById(R.id.statistics);
        essays_btn.setBackgroundColor(Color.argb(255, 51, 153, 255));
        essays_btn.setTextColor(Color.WHITE);
        essayFragShown = true;

        //check if it's first time seeing activity (just logged in)
        try {
            if (getIntent().getExtras().get("login") != null) {
                Snackbar.make((View) username.getParent(), "Welcome " + ParseUser.getCurrentUser().get("handle"), Snackbar.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {

        }

        setProfilePicListener();
        setUsername();
        setProfilePic();
        setEssaysListener();
        setStatsListener();
    }

    private void setStatsListener()
    {
        stats_btn.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        stats_btn.setBackgroundColor(Color.argb(255, 51, 153, 255));
                        stats_btn.setTextColor(Color.WHITE);

                        essays_btn.setBackgroundColor(Color.WHITE);
                        essays_btn.setTextColor(Color.argb(255, 51, 153, 255));

                        if(essayFragShown)
                        {
                            essayFragShown = false;
                            changeFragment(false);
                        }
                    }
                }
        );
    }

    private void setProfilePic()
    {
        ParseFile picFile = (ParseFile) ParseUser.getCurrentUser().get("profile_pic");
        if(picFile != null)
        {
            picFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bmp = BitmapFactory
                            .decodeByteArray(data, 0, data.length);
                    profile_pic.setImageBitmap(getRoundedShape(bmp));
                    profile_pic.setRotation(-90);
                }
            });
        }
        else
        {
            profile_pic.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public void setEssaysListener()
    {
        essays_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        essays_btn.setBackgroundColor(Color.argb(255, 51, 153, 255));
                        essays_btn.setTextColor(Color.WHITE);

                        stats_btn.setBackgroundColor(Color.WHITE);
                        stats_btn.setTextColor(Color.argb(255, 51, 153, 255));

                        if (!essayFragShown) {
                            changeFragment(true);
                            essayFragShown = true;
                        }
                    }
                }
        );
    }

    public void changeFragment(boolean value)
    {
        Fragment fragment;
        if(!value)
        {
            fragment = new PieChartFrag();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_option, fragment);
            ft.commit();
        }
        else
        {
            fragment = new Essays();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_option, fragment);
            ft.commit();
        }

    }

    public void setUsername()
    {
        String usern = (String) ParseUser.getCurrentUser().get("handle");
        username.setText(usern);
    }

    private void setProfilePicListener()
    {
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomeScreen.this)
                        .setTitle("")
                        .setMessage("Change Profile Picture?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camera_intent, 1);

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            Bitmap pp = getRoundedShape(bitmap);

            profile_pic.setImageBitmap(pp);
            profile_pic.setRotation(-90);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byte_data = stream.toByteArray();
            saveToParse(byte_data);


        }
        catch (Exception e)
        {

        }
    }

    private void saveToParse(byte[] byte_data)
    {
        ParseFile pf = new ParseFile("profile_pic.png", byte_data);
        pf.saveInBackground();
        ParseUser.getCurrentUser().put("profile_pic", pf);
        try {
            ParseUser.getCurrentUser().saveInBackground().waitForCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 120;
        int targetHeight = 120;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public File getFile()
    {
        File folder = new File("sdcard/CodeDay");

        if(!folder.exists())
        {
            folder.mkdir();
        }

        File imageFile = new File(folder,"profile_pic.jpg");
        return imageFile;
    }

}
