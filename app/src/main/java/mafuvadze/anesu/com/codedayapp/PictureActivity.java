package mafuvadze.anesu.com.codedayapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
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
import java.io.IOException;

public class PictureActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    Bitmap pic;
    ProgressDialog progress;
    EditText title, subject, essay;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        initializeViews();
        takePicture();
    }

    private void initializeViews()
    {
        title = (EditText) findViewById(R.id.title);
        subject = (EditText) findViewById(R.id.subject);
        essay = (EditText) findViewById(R.id.essay);
    }

    private void compressImage()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap.createScaledBitmap(pic, 400, 600, false).compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byte_data = stream.toByteArray();
        saveToParse(byte_data);
    }

    private void saveToParse(byte[] byte_data)
    {
        ParseFile pf = new ParseFile("essay_pic.jpg", byte_data);
        pf.saveInBackground();
        ParseUser.getCurrentUser().put("essay_pic", pf);
        try {
            ParseUser.getCurrentUser().saveInBackground().waitForCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ParseFile file = ParseUser.getCurrentUser().getParseFile("essay_pic");
        fetchStringResponseOCR(file.getUrl());
    }

    private void parseResponse(String response)
    {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("text_block");
            String essay = array.getString(0);
            this.essay.setText(essay);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void fetchStringResponseOCR(String url)
    {
        String path = "https://api.havenondemand.com/1/api/sync/ocrdocument/v1?url=" + url + "&apikey=60554d21-c589-4af8-af33-761cae860fab";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, path, this, this);
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void takePicture() {
        new Background().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(
                  getContentResolver(), imageUri);
                pic = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
            compressImage();
        }
    }

    public String getRealPathFromUri(Uri contentUri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();;
        return cursor.getString(column_index);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        parseResponse(response);
    }

    private void displaySnackbar()
    {
        View parentView = (View) title.getParent();
        Snackbar.make(parentView
                , title.getText().toString() + " succesfully uploaded"
                , Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    private boolean isValidInput()
    {
        if(title.getText().toString() == ""
                || subject.getText().toString() == ""
                || essay.getText().toString() == "")
        {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.confirm)
        {
            if(isValidInput()) {
                UploadEssay uploadEssay = new UploadEssay(this);
                uploadEssay.upload(title.getText().toString()
                ,subject.getText().toString()
                ,essay.getText().toString());
                displaySnackbar();
                Intent intent = new Intent(PictureActivity.this, HomeScreen.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    class Background extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "OCR pic");
            values.put(MediaStore.Images.Media.DESCRIPTION, "to be send converted to text");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 1);
            return null;
        }
    }

}
