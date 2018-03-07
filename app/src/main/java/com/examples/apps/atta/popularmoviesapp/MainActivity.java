package com.examples.apps.atta.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.examples.apps.atta.popularmoviesapp.Adapters.MovieImageAdapter;
import com.examples.apps.atta.popularmoviesapp.Model.Movie;
import com.examples.apps.atta.popularmoviesapp.Tasks.FetchFavouriteMoviesTask;
import com.examples.apps.atta.popularmoviesapp.Tasks.FetchMoviesTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements MovieImageAdapter.MovieImageAdapterOnClickHandler{

    private MovieImageAdapter mMovieImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this,adjustGridColumns());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mMovieImageAdapter = new MovieImageAdapter(this , new ArrayList<Movie>() ,this);
        recyclerView.setAdapter(mMovieImageAdapter);

        updateMovies();
    }


    private int adjustGridColumns(){

        int gridColumns;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridColumns = 2;
        }else {
            gridColumns = 4;
        }
        return gridColumns;
    }

    private void updateMovies(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortByPref = sharedPreferences.getString(
                getString(R.string.sort_key),
                getString(R.string.sort_by_default_value));

        if (sortByPref.contentEquals(getResources().getString(R.string.sort_by_favourites))){
            new FetchFavouriteMoviesTask(this,mMovieImageAdapter).execute();
        }else {
            if (isNetworkAvailable()){
                new FetchMoviesTask(mMovieImageAdapter).execute(sortByPref);
            }else {
                Toast.makeText(this, R.string.no_connection_error , Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMovies();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.DETAIL_MOVIE,movie);
        startActivity(intent);
    }
}
