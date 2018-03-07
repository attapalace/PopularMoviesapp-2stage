package com.examples.apps.atta.popularmoviesapp.Tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.examples.apps.atta.popularmoviesapp.Adapters.TrailerAdapter;
import com.examples.apps.atta.popularmoviesapp.BuildConfig;
import com.examples.apps.atta.popularmoviesapp.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ahmedatta on 2/28/18.
 */

public class FetchTrailerTask extends AsyncTask<String , Void, ArrayList<Trailer>> {

    private final String JS_ID = "id";
    private final String JS_KEY = "key";
    private final String JS_NAME = "name";
    private final String JS_SITE = "site";

    private final String JSON_ARRAY_RESULTS = "results";

    private TrailerAdapter mTrailerAdapter;

    private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();

    public FetchTrailerTask(TrailerAdapter mTrailerAdapter) {
        this.mTrailerAdapter = mTrailerAdapter;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... strings) {
        if(strings.length == 0){
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailerJsonStr = null;


        try {
            String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String MOVIE_URL = MOVIE_BASE_URL + strings[0] + "/videos";
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
            trailerJsonStr = stringBuffer.toString();

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
            return getDataFromJson(trailerJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG , "error getting json String :" + e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        if (trailers != null){
            mTrailerAdapter.setTrailerList(trailers);
        }
    }

    private ArrayList<Trailer> getDataFromJson(String jsonString) throws JSONException {
        JSONObject object = null;
        if(jsonString != null){
            object = new JSONObject(jsonString);
        }
        JSONArray resultArray = null;
        if(object.has(JSON_ARRAY_RESULTS)){
            resultArray = object.getJSONArray(JSON_ARRAY_RESULTS);
        }

        ArrayList<Trailer> trailerList = new ArrayList<>();
        for(int i = 0 ; i < resultArray.length() ; i++){
            JSONObject jsonObject = resultArray.getJSONObject(i);
            Trailer trailer = new Trailer();
            trailer.id = jsonObject.optString(JS_ID);
            trailer.key = jsonObject.optString(JS_KEY);
            trailer.name = jsonObject.optString(JS_NAME);
            trailer.site = jsonObject.optString(JS_SITE);

            trailerList.add(trailer);
        }
        return trailerList;
    }
}
