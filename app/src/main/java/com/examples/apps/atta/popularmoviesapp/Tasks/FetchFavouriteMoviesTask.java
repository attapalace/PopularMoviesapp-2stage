package com.examples.apps.atta.popularmoviesapp.Tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.examples.apps.atta.popularmoviesapp.Adapters.MovieImageAdapter;
import com.examples.apps.atta.popularmoviesapp.Data.MoviesContract;
import com.examples.apps.atta.popularmoviesapp.Model.Movie;

import java.util.ArrayList;

/**
 * Created by ahmedatta on 3/5/18.
 */

public class FetchFavouriteMoviesTask extends AsyncTask<Void,Void,ArrayList<Movie>>{

    private final static String LOG_TAG = FetchFavouriteMoviesTask.class.getSimpleName();
    private Context context;
    private MovieImageAdapter adapter ;

    public FetchFavouriteMoviesTask(Context context, MovieImageAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    private static final String[] MOVIE_COLUMNS = {
            MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_TITLE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE,
            MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
            MoviesContract.MoviesEntry.COLUMN_RATING,
            MoviesContract.MoviesEntry.COLUMN_DATE
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_MOVIE_IMAGE = 3;
    public static final int COL_OVERVIEW = 4;
    public static final int COL_RATING = 5;
    public static final int COL_DATE = 6;

    private ArrayList<Movie> getDataFromCursor(Cursor cursor){
        ArrayList<Movie> results = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.id = cursor.getInt(COL_MOVIE_ID);
                movie.title = cursor.getString(COL_TITLE);
                movie.posterImage = cursor.getString(COL_MOVIE_IMAGE);
                movie.overView = cursor.getString(COL_OVERVIEW);
                movie.userRating = cursor.getDouble(COL_RATING);
                movie.releaseDate = cursor.getString(COL_DATE);
                results.add(movie);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }


    @Override
    protected ArrayList<Movie> doInBackground(Void... voids) {
        Cursor cursor = context.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                MOVIE_COLUMNS,null,null,null);
        return getDataFromCursor(cursor);
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if (movies != null){
            if (adapter != null){
                adapter.setMovieList(movies);
            }
        }
    }
}
