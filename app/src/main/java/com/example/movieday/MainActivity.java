package com.example.movieday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.movieday.adapter.ImageViewAdapter;
import com.example.movieday.utilities.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ArrayList<MovieDetails> movData;
    MovieDetails movieDetails;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageViewAdapter adapter;
    URL url;

    public final String DEFAULD_QUERY ="http://api.themoviedb.org/3/movie/popular?api_key=7ff17a25c7da1faef085e0a8406e9dc7";
    public final String TOP_RATED_QUERY ="http://api.themoviedb.org/3/movie/top_rated?api_key=7ff17a25c7da1faef085e0a8406e9dc7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, numberOfColumn());
        adapter = new ImageViewAdapter(movData, this);

        recyclerView.setLayoutManager(layoutManager);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        url = null;
        try {
            url = new URL(DEFAULD_QUERY);
        } catch (MalformedURLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        FetchData fetchData = new FetchData();
        fetchData.execute(url);
    }

    public class FetchData extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.INVISIBLE);

            if (s != null && !s.equals("")) {
                ParseJson(s);
            }
            else{
                View parentLayout = MainActivity.this.findViewById(R.id.root);
                Snackbar.make(parentLayout,"Please Check Your Internet Connection!!!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FetchData fetchData = new FetchData();
                                fetchData.execute(url);
                            }
                        }).show();

            }
        }
    }

    public void ParseJson(String s) {

        try {

            movData = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i< jsonArray.length(); i++) {
                JSONObject currentObject = jsonArray.getJSONObject(i);

                movieDetails = new MovieDetails();

                movieDetails.setMovieID(currentObject.getString("id"));
                movieDetails.setTitle(currentObject.getString("original_title"));
                movieDetails.setPosterPath(currentObject.getString("poster_path"));
                movieDetails.setReleaseDate(currentObject.getString("release_date"));
                movieDetails.setVoteAverage(currentObject.getString("vote_average"));
                movieDetails.setOverview(currentObject.getString("overview"));

                movData.add(movieDetails);
            }

//            adapter = new ImageViewAdapter(movData, this);
//            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumn()));
//            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private int numberOfColumn(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if(nColumns<2)return 2;
        return nColumns;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FetchData fetchData = new FetchData();
        URL url = null;
        if (item.getItemId() == R.id.most_popular) {
            try {
                url = new URL(DEFAULD_QUERY);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else if (item.getItemId() == R.id.top_rated) {
            try {
                url = new URL(TOP_RATED_QUERY);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }if (url !=null) {
            fetchData.execute(url);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
