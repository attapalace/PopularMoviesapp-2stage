package com.examples.apps.atta.popularmoviesapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examples.apps.atta.popularmoviesapp.Model.Review;
import com.examples.apps.atta.popularmoviesapp.R;

import java.util.ArrayList;

/**
 * Created by ahmedatta on 3/3/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Context mContext;
    private ArrayList<Review> mReviewList;

    public ReviewAdapter(Context mContext, ArrayList<Review> mReviewList) {
        this.mContext = mContext;
        this.mReviewList = mReviewList;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_review,parent,false);
        view.setFocusable(true);
        return new ReviewAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ReviewAdapterViewHolder holder, final int position) {
        if(mReviewList != null){
            Review mReview = mReviewList.get(position);
            holder.reviewAuthor.setText(mReview.getAuthor());
            holder.reviewBody.setText(mReview.getContent());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(mReviewList == null){
            return 0;
        }else {
            return mReviewList.size();
        }
    }

    public void setReviewList(ArrayList<Review> reviewList){
        if(reviewList != null){
            mReviewList = new ArrayList<>(reviewList);
        }
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView reviewAuthor;
        TextView reviewBody;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.review_author);
            reviewBody = itemView.findViewById(R.id.review_body);
        }

    }
}
