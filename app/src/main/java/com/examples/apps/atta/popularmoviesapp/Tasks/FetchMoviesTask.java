package com.examples.apps.atta.popularmoviesapp.Tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.examples.apps.atta.popularmoviesapp.BuildConfig;
import com.examples.apps.atta.popularmoviesapp.Model.Movie;
import com.examples.apps.atta.popularmoviesapp.Adapters.MovieImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ahmedatta on 2/26/18.
 */

public class FetchMoviesTask extends AsyncTask<String , Void , ArrayList<Movie>> {

    private final String JSON_ID = "id";
    private final String JSON_TITLE = "title";
    private final String JSON_POSTER = "poster_path";
    private final String JSON_OVERVIEW = "overview";
    private final String JSON_USER_RATING = "vote_average";
    private final String JSON_RELEASE_DATE = "release_date";

    private final String JSON_ARRAY_RESULTS = "results";

    private MovieImageAdapter mMovieImageAdapter;

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    public FetchMoviesTask(MovieImageAdapter mMovieImageAdapter) {
        this.mMovieImageAdapter = mMovieImageAdapter;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... strings) {
        if(strings.length == 0){
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;


        try {
            String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String MOVIE_URL = MOVIE_BASE_URL + strings[0];
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                    .appendQueryParameter(API_KEY , BuildConfig.THE_MOVIE_DB_API_KEY).build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if (inputStream == null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null ){
                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0){
                return null;
            }

            movieJsonStr = stringBuffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG , "error :" + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG , "error :" + e);
        }
        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG , "error :" + e);
                }
            }
        }

        try {
            return getDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG , "error getting json String :" + e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movieList) {
        if(movieList != null){
            mMovieImageAdapter.setMovieList(movieList);
        }
        ArrayList<Movie> moviesList = new ArrayList<>();
        moviesList.addAll(movieList);

    }

    private ArrayList<Movie> getDataFromJson (String jsonStr) throws JSONException {

        JSONObject object = null;
        if(jsonStr != null){
            object = new JSONObject(jsonStr);
        }
        JSONArray resultArray = null;
        if(object.has(JSON_ARRAY_RESULTS)){
            resultArray = object.getJSONArray(JSON_ARRAY_RESULTS);
        }

        ArrayList<Movie> moviesList = new ArrayList<>();

        for (int i = 0 ; i < resultArray.length() ; i++){
            JSONObject movieObject = resultArray.getJSONObject(i);
            Movie movie = new Movie();
            movie.id = movieObject.optInt(JSON_ID);
            movie.title = movieObject.optString(JSON_TITLE);
            movie.posterImage = movieObject.optString(JSON_POSTER);
            movie.overView = movieObject.optString(JSON_OVERVIEW);
            movie.userRating = movieObject.optDouble(JSON_USER_RATING);
            movie.releaseDate = movieObject.optString(JSON_RELEASE_DATE);

            moviesList.add(movie);
        }
        return moviesList;
    }
}
