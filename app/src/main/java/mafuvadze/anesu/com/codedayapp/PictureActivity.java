package mafuvadze.anesu.com.codedayapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class PictureActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    Button btn;
    ImageView img;
    Bitmap pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        btn = (Button) findViewById(R.id.convert);
        img = (ImageView) findViewById(R.id.image_spot);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byte_data = stream.toByteArray();
                ParseFile pf = new ParseFile("essay_pic.jpg", byte_data);
                pf.saveInBackground();
                ParseUser.getCurrentUser().put("essay_pic",pf);
                try {
                    ParseUser.getCurrentUser().saveInBackground().waitForCompletion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ParseFile file = ParseUser.getCurrentUser().getParseFile("essay_pic");
                makeRequest(file.getUrl());
            }
        });

        takePicture();
    }

    private void makeRequest(String url)
    {
        String path = "https://api.havenondemand.com/1/api/sync/ocrdocument/v1?url=" + url +"&apikey=60554d21-c589-4af8-af33-761cae860fab";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, path, this, this);
        request.setRetryPolicy(new DefaultRetryPolicy(
               5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void takePicture()
    {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(photo);
            pic = photo;
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("text_block");
            JSONObject o = array.getJSONObject(0);
            final String text = (String) o.get("text");

            final Dialog dialog = new Dialog(this);
            dialog.setTitle("Upload Essay");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.create_essay);
            dialog.show();

            final EditText title  = (EditText) dialog.findViewById(R.id.title);
            final EditText subject  = (EditText) dialog.findViewById(R.id.subject);
            final Button submit = (Button) dialog.findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progress = new ProgressDialog(PictureActivity.this);
                    progress.setMessage(getString(R.string.please_wait_message));
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setCancelable(false);
                    progress.show();
                    dialog.dismiss();
                    String t = title.getText().toString();
                    String s = subject.getText().toString();
                    ParseObject essay = new ParseObject("essays");
                    essay.put("title", t);
                    essay.put("subject", s);
                    essay.put("content", text);
                    essay.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e != null) {
                                e.printStackTrace();
                            } else {
                                progress.dismiss();
                                Toast.makeText(PictureActivity.this, "Essay uploaded", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(PictureActivity.this, HomeScreen.class);
                                startActivity(intent);
                            }
                        }

                    });
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
