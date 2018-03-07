package com.examples.apps.atta.popularmoviesapp.Tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.examples.apps.atta.popularmoviesapp.Adapters.ReviewAdapter;
import com.examples.apps.atta.popularmoviesapp.BuildConfig;
import com.examples.apps.atta.popularmoviesapp.Model.Review;

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
 * Created by ahmedatta on 3/3/18.
 */

public class FetchReviewTask extends AsyncTask<String,Void,ArrayList<Review>> {

    private final String JS_ID = "id";
    private final String JS_AUTHOR = "author";
    private final String JS_CONTENT = "content";
    private final String JS_URL = "url";

    private final String JSON_ARRAY_RESULTS = "results";

    private final String LOG_TAG = FetchReviewTask.class.getSimpleName();

    private ReviewAdapter mReviewAdapter;

    public FetchReviewTask(ReviewAdapter mReviewAdapter) {
        this.mReviewAdapter = mReviewAdapter;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... strings) {
        if(strings.length == 0){
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String reviewJsonStr = null;


        try {
            String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String MOVIE_URL = MOVIE_BASE_URL + strings[0] + "/reviews";
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

            reviewJsonStr = stringBuffer.toString();

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
            return getDataFromJson(reviewJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG , "error getting json String :" + e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        if (reviews != null){
            mReviewAdapter.setReviewList(reviews);
        }
    }

    private ArrayList<Review> getDataFromJson(String jsonString) throws JSONException {
        JSONObject object = null;
        if(jsonString != null){
            object = new JSONObject(jsonString);
        }
        JSONArray resultArray = null;
        if(object.has(JSON_ARRAY_RESULTS)){
            resultArray = object.getJSONArray(JSON_ARRAY_RESULTS);
        }

        ArrayList<Review> reviewList = new ArrayList<>();
        for(int i = 0 ; i < resultArray.length() ; i++){
            JSONObject jsonObject = resultArray.getJSONObject(i);
            Review review = new Review();
            review.id = jsonObject.optString(JS_ID);
            review.author = jsonObject.optString(JS_AUTHOR);
            review.content = jsonObject.optString(JS_CONTENT);
            review.url = jsonObject.optString(JS_URL);

            reviewList.add(review);
        }
        return reviewList;
    }
}
