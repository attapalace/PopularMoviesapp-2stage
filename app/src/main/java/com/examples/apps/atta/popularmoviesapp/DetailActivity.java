package com.examples.apps.atta.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.apps.atta.popularmoviesapp.Adapters.ReviewAdapter;
import com.examples.apps.atta.popularmoviesapp.Adapters.TrailerAdapter;
import com.examples.apps.atta.popularmoviesapp.Data.MoviesContract;
import com.examples.apps.atta.popularmoviesapp.Model.Movie;
import com.examples.apps.atta.popularmoviesapp.Model.Review;
import com.examples.apps.atta.popularmoviesapp.Model.Trailer;
import com.examples.apps.atta.popularmoviesapp.Tasks.FetchReviewTask;
import com.examples.apps.atta.popularmoviesapp.Tasks.FetchTrailerTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ItemHolderClicks{

    public static final String DETAIL_MOVIE = "detail_movie";
    private final Context mContext = this;

    @BindView(R.id.movie_title) TextView mMovieTitle;
    @BindView(R.id.release_date_value) TextView mReleaseDate;
    @BindView(R.id.user_ratings_value) TextView mUserRatings;
    @BindView(R.id.movie_overview) TextView mOverView;
    @BindView(R.id.movie_poster_image) ImageView mMoviePoster;
    @BindView(R.id.trailers_recycler_view) RecyclerView trailerRecyclerView;
    @BindView(R.id.trailer_headline) TextView trailerHeader;
    @BindView(R.id.reviews_recycler_view) RecyclerView reviewRecyclerView;
    @BindView(R.id.reviews_headline) TextView reviewsHeader;
    @BindView(R.id.empty_trailer_list) TextView emptyTrailerMessage;
    @BindView(R.id.empty_review_list) TextView emptyReviewMessage;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        mTrailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        mTrailerAdapter.setOnItemClickListener(this);

        mReviewAdapter = new ReviewAdapter(this,new ArrayList<Review>());

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);


        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        reviewRecyclerView.addItemDecoration(new DividerItemDecoration(reviewRecyclerView.getContext()
                ,DividerItemDecoration.VERTICAL));
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);

        trailerRecyclerView.setAdapter(mTrailerAdapter);
        reviewRecyclerView.setAdapter(mReviewAdapter);

        Intent intent = getIntent();
        if(intent.getParcelableExtra(DETAIL_MOVIE) != null){
            mMovie = intent.getParcelableExtra(DETAIL_MOVIE);
        }

        displayMovieInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMovie!= null){
            new FetchTrailerTask(mTrailerAdapter).execute(Integer.toString(mMovie.getId()));
            new FetchReviewTask(mReviewAdapter).execute(Integer.toString(mMovie.getId()));
        }
        showEmptyMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail,menu);

        MenuItem favouriteItem = menu.findItem(R.id.action_add_to_favourites);

        if (isFavourite(getApplicationContext(),mMovie.getId())){
            favouriteItem.setIcon(android.R.drawable.star_on);
        }else {
            favouriteItem.setIcon(android.R.drawable.star_off);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add_to_favourites:
                if (isFavourite(getApplicationContext(),mMovie.getId())){
                    deleteFromFavourites();
                    item.setIcon(android.R.drawable.star_off);
                }else {
                    addToFavourites();
                    item.setIcon(android.R.drawable.star_on);
                }
                return true;

            case R.id.action_share:
                if (mTrailerAdapter.getItemCount()>0){
                    createShareIntent();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayMovieInfo(){

        mMovieTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mUserRatings.setText(String.valueOf(mMovie.getUserRating()));
        mOverView.setText(mMovie.getOverView());

        String imageUrl = "http://image.tmdb.org/t/p/w185" + mMovie.getPosterImage();
        Picasso.with(mContext).load(imageUrl).into(mMoviePoster);

    }

    @Override
    public void onTrailerClick(Trailer trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        startActivity(intent);
    }

    public void showEmptyMessage(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTrailerAdapter.getItemCount() == 0){
                    emptyTrailerMessage.setText(getResources().getString(R.string.no_trailers_avaialble));
                }
                if (mReviewAdapter.getItemCount() == 0){
                    emptyReviewMessage.setText(getResources().getString(R.string.no_reviews_available));
                }
            }
        }, 1000);
    }

    private boolean isFavourite(Context context , int id ){
        Cursor cursor = context.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",
                new String[] {Integer.toString(id)},null);
        if (cursor.getCount()<= 0 ){
            cursor.close();
            return false;
            }
        cursor.close();
        return true;
    }

    private void addToFavourites(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();

                values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID , mMovie.getId());
                values.put(MoviesContract.MoviesEntry.COLUMN_TITLE , mMovie.getTitle());
                values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE,mMovie.getPosterImage());
                values.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW , mMovie.getOverView());
                values.put(MoviesContract.MoviesEntry.COLUMN_RATING,mMovie.getUserRating());
                values.put(MoviesContract.MoviesEntry.COLUMN_DATE,mMovie.getReleaseDate());

                getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI,values);

            }
        }).start();
        Toast.makeText(this, R.string.movie_add_to_favourites,Toast.LENGTH_SHORT).show();

    }

    private void deleteFromFavourites(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI,
                        MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{Integer.toString(mMovie.getId())});
            }
        }).start();
        Toast.makeText(this, R.string.movie_removed_from_favourites,Toast.LENGTH_SHORT).show();

    }

    private void createShareIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        intent.putExtra(Intent.EXTRA_SUBJECT,mMovie.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT,"http://www.youtube.com/watch?v=" + mTrailerAdapter.getTrailerKey());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share Trailer"));
    }
}
