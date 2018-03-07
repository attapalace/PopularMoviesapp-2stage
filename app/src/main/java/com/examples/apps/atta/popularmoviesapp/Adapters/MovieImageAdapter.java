package com.examples.apps.atta.popularmoviesapp.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.examples.apps.atta.popularmoviesapp.Model.Movie;
import com.examples.apps.atta.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmedatta on 2/19/18.
 */

public class MovieImageAdapter extends RecyclerView.Adapter<MovieImageAdapter.MovieImageAdapterViewHolder>{

    private Context  mContext;
    private Movie mMovie = new Movie();
    private ArrayList<Movie> mMoviesList;

    private Cursor mCursor;

    private final MovieImageAdapterOnClickHandler onClickHandler;


    public MovieImageAdapter(Context context , ArrayList<Movie> moviesList, MovieImageAdapterOnClickHandler onClickHandler ) {
        this.mContext = context;
        this.mMoviesList = moviesList;
        this.onClickHandler = onClickHandler;
    }

    public interface MovieImageAdapterOnClickHandler{
        void onClick(Movie movie);
    }


    @Override
    public MovieImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie,parent,false);
        view.setFocusable(true);
        return new MovieImageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieImageAdapterViewHolder holder, int position) {
        if(mMoviesList != null){
            mMovie = mMoviesList.get(position);
            String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
            String image_url = IMAGE_BASE_URL + mMovie.getPosterImage();
            Picasso.with(mContext)
                    .load(image_url)
                    .into(holder.mImageView);
        }
    }

//    public Cursor swapCursor(Cursor newCursor){
//        if (mCursor == newCursor ){
//            return null;
//        }
//        Cursor tempCusror = mCursor;
//        mCursor = newCursor;
//        notifyDataSetChanged();
//
//        return tempCusror;
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(mMoviesList == null){
            return 0;
        }else {
            return mMoviesList.size();
        }
    }

    public void setMovieList(ArrayList<Movie> movieList){
        if(movieList != null){
            mMoviesList = new ArrayList<>(movieList);
        }
        notifyDataSetChanged();
    }

    public class MovieImageAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        ImageView mImageView;

        public MovieImageAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.list_item_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mMovie = mMoviesList.get(adapterPosition);
            onClickHandler.onClick(mMovie);
        }
    }
}
