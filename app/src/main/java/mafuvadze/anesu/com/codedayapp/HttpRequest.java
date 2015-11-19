package mafuvadze.anesu.com.codedayapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Angellar Manguvo on 11/17/2015.
 */
public class HttpRequest implements Response.Listener<String>, Response.ErrorListener
{
    RequestQueue queue;
    Context context;
    String response;
    public HttpRequest(Context context)
    {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void fetchStringResponse(String url)
    {
        StringRequest request = new StringRequest(Request.Method.GET, url, this, this);
        queue.add(request);
    }

    public void fetchStringResponseOCR(String url)
    {
        String path = "https://api.havenondemand.com/1/api/sync/ocrdocument/v1?url=" + url + "&apikey=60554d21-c589-4af8-af33-761cae860fab";
        StringRequest request = new StringRequest(Request.Method.GET, path, this, this);
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public String getStringResponse()
    {
        return response;
    }

    @Override
    public void onResponse(String response) {
        this.response = response;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
    }
}
