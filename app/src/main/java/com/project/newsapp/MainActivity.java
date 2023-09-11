package com.project.newsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RecylerViewInterface{

    //After making an app like book Listing I'll try my best to add ViewMode
    // + Alternate of AyscTask
    // + Prefernce Screen for modification. IA


    //Api variables
    public final String USGS_REQUEST_URL="https://content.guardianapis.com/search?page=40&q=debate&api-key=test";
    public static final String LOG_TAG = MainActivity.class.getName();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NewsAdatpter mAdapter;
    public RecyclerView mRecyclerView;
    public RecylerViewInterface recyclerInterface;
    ArrayList<NewsInfo> result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //JSON WORKONG HERE
        //Network Connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, display error
            TextView error = findViewById(R.id.error);
            error.setVisibility(View.VISIBLE);
            ProgressBar pg1= findViewById(R.id.progressBar1);
            pg1.setVisibility(View.INVISIBLE);
            TextView conn = findViewById(R.id.conect);
            conn.setVisibility(View.INVISIBLE);
        }

/*        NewsAsyncTask task = new NewsAsyncTask();
        task.execute(USGS_REQUEST_URL);*/
//        mAdapter = new NewsAdatpter(getApplicationContext(), result, this);
        doStuff(this::onItemClick);
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        // Create a new intent to view the earthquake URI

    }


    private void doStuff(RecylerViewInterface recyclerViews) {

        Handler handler = new Handler();
        Handler handler1 = new Handler();
        Log.e("Before the ", " Background");
        new Thread(new Runnable() {
            @Override
            public void run() {
//BackGround
                Log.e("HERE the ", " Background");

                Log.e("just Before the ", " Background");
//This is doIn Background
                if (USGS_REQUEST_URL.length() < 1 || USGS_REQUEST_URL == null) {
                    //return null;
                }
                Log.e("In the ", " Background");
                result = com.project.newsapp.NewsAsyncTask.fetchNewsData(USGS_REQUEST_URL);
//Post because only handler contact the UI
                handler1.post(new Runnable() {
            @Override
            public void run() {

                if (result != null && !result.isEmpty()) {
                    Log.e("In the "," Post");
                    Toast.makeText(MainActivity.this,result.get(0).getName(),Toast.LENGTH_SHORT).show();
                    mAdapter = new NewsAdatpter(getApplicationContext(), result, recyclerViews);

/*                    TextView textView = findViewById(R.id.text);
                    textView.setVisibility(View.VISIBLE);*/
                    ProgressBar pg = findViewById(R.id.progressBar1);
                    pg.setVisibility(View.INVISIBLE);
                    TextView connect = findViewById(R.id.conect);
                    connect.setVisibility(View.INVISIBLE);


                    mRecyclerView = findViewById(R.id.recycle);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(MainActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);
                }
                if(result == null)
                {
                    Toast.makeText(MainActivity.this,"Try",Toast.LENGTH_SHORT).show();

                    //Hide because to see internet connectivity
                    // mEmptyStateTextView.setText("No Data To Show");
                }
                //This is post
                    }
                });
            }
        }).start();
        }



    @Override
    public void onItemClick(int position) {

        NewsInfo books = mAdapter.getItem(position);
        Log.e("Now it work "," "+mAdapter.getItemCount());
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(books
                .getPreview()));

        // Send the intent to launch a new activity
        startActivity(websiteIntent);


    }

    //Prefernce Edit

    // Fetch the stored data in onResume()
    // Because this is what will be called
    // when the app opens again
    @Override
    protected void onResume() {
        super.onResume();

        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String s1 = sh.getString("name", "");
        // Setting the fetched data
        // in the EditTexts
        EditText textView = findViewById(R.id.text);
        textView.setVisibility(View.VISIBLE);
        textView.setText(s1);
    }

    // Store the data in the SharedPreference
    // in the onPause() method
    // When the user closes the application
    // onPause() will be called
    // and data will be stored
    @Override
    protected void onPause() {
        super.onPause();

        // Creating a shared pref object
        // with a file name "MySharedPref"
        // in private mode
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
        EditText textView = findViewById(R.id.text);
        textView.setVisibility(View.VISIBLE);
        myEdit.putString("name", textView.getText().toString());
        myEdit.apply();
    }



/*    private class NewsAsyncTask extends AsyncTask<String, Void, ArrayList<NewsInfo>> implements RecylerViewInterface{


        protected ArrayList<NewsInfo> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            Log.e("In the "," Background = "+ urls[0]);
            ArrayList<NewsInfo> result = com.project.newsapp.NewsAsyncTask.fetchNewsData(urls[0]);
            return result;
        }


        @Override
        public void onItemClick(int position) {

            NewsInfo books = mAdapter.getItem(position);
            Log.e("Now it work "," "+mAdapter.getItemCount());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(books
                    .getPreview()));

            // Send the intent to launch a new activity
            startActivity(websiteIntent);


        }
        protected void onPostExecute(ArrayList<NewsInfo> data) {
            // Clear the adapter of previous earthquake data
            //mAdapter.clear();


            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                Log.e("In the "," Post");

*//*
                Toast.makeText(MainActivity.this,data.get(0).getBookName(),Toast.LENGTH_SHORT).show();
*//*


                mAdapter = new NewsAdatpter(getApplicationContext(), data,this);

                TextView textView = findViewById(R.id.text);
                textView.setVisibility(View.VISIBLE);
                ProgressBar pg = findViewById(R.id.progressBar1);
                pg.setVisibility(View.INVISIBLE);
                TextView connect = findViewById(R.id.conect);
                connect.setVisibility(View.INVISIBLE);


                mRecyclerView = findViewById(R.id.recycle);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);

                RecyclerView.LayoutManager layoutManager =
                        new LinearLayoutManager(MainActivity.this);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(true);
            }
            if(data == null)
            {
                *//*       Toast.makeText(MainActivity.this,"Try",Toast.LENGTH_SHORT).show();*//*

                //Hide because to see internet connectivity
                // mEmptyStateTextView.setText("No Data To Show");

            }

        }
    }*/
}