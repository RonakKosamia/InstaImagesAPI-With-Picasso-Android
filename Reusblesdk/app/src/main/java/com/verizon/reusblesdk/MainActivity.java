package com.verizon.reusblesdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

//to work this http response ; add http library using project structure , inside gradle
// read this :
// http://stackoverflow.com/questions/32607257/cannot-resolve-symbol-httpget-httpclient-httpresponce-in-android-studio
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GridView mGridView;
    private mGridViewAdapter mGridAdapter;
    private ArrayList<mGridItem> mGridData;
    private ProgressBar mProgressBar;

    private String API_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Intent
        Intent intent = getIntent();

        //Get the API URL
        String sharedURL = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedURL != null) {
            //  UI will be updated
            //  it will reflect text being shared
            API_URL = sharedURL;
            // otherwise it will
            // use the go-wireless account(default one)
        }else{
            API_URL = "https://www.instagram.com/gowireless/media/";
        }


        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mGridData = new ArrayList<>();
        mGridAdapter = new mGridViewAdapter(this, R.layout.mgridlayout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Click on image will open a another activity to display image using >>> Gridview click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at specific position
                mGridItem item = (mGridItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, mDetailsActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url
                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView.getWidth()).
                        putExtra("height", imageView.getHeight()).
                        putExtra("title", item.getTitle()).
                        putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });

        //Start download
        new AsyncHttpTask().execute(API_URL);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    //android support only one thread so for background task
    // we can use Asynctask ; here we are Downloading
    // data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // Success
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1;
                    // Fail
                } else {

                    result = 0;
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setData(mGridData);
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    /**
     * step 1: Parse the instagram results
     * step 2: get the JSON to make our  data list(grid photos)
     * This method should be make mutable to suite different APIs,
     * different Json parsing
     */
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("items");
            mGridItem item;

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                JSONObject loc = post.getJSONObject("caption");
                String title = loc.optString("text");
                item = new mGridItem();
                item.setTitle(title);

                JSONObject images = post.getJSONObject("images");
                JSONObject attachments = images.getJSONObject("standard_resolution");
                item.setImage(attachments.getString("url"));

                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}