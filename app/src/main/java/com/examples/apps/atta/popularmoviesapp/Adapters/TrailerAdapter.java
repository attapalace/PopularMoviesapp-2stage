package com.examples.apps.atta.popularmoviesapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.apps.atta.popularmoviesapp.Model.Trailer;
import com.examples.apps.atta.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmedatta on 2/28/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private Context  mContext;
    private Trailer mTrailer = new Trailer();
    private ArrayList<Trailer> mTrailerList;

    private ItemHolderClicks handler;

    public TrailerAdapter(Context context , ArrayList<Trailer> trailerList) {
        this.mContext = context;
        this.mTrailerList = trailerList;
    }

    public interface ItemHolderClicks{
        void onTrailerClick(Trailer trailer);
    }

    public void setOnItemClickListener(ItemHolderClicks clickListener){
        this.handler = clickListener;
    }


    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_trailer,parent,false);
        view.setFocusable(true);
        return new TrailerAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final TrailerAdapterViewHolder holder, final int position) {
        if(mTrailerList != null){
            mTrailer = mTrailerList.get(position);
            holder.trailerName.setText(mTrailer.getName());
            String youtubeThumb_Url = "https://img.youtube.com/vi/" + mTrailer.getKey() + "/0.jpg" ;
            Picasso.with(mContext)
                    .load(youtubeThumb_Url)
                    .into(holder.trailerIV);
            holder.trailerIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.onTrailerClick(mTrailer);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getTrailerKey(){
        return mTrailer.getKey();
    }

    @Override
    public int getItemCount() {
        if(mTrailerList == null){
            return 0;
        }else {
            return mTrailerList.size();
        }
    }

    public void setTrailerList(ArrayList<Trailer> trailerList){
        if(trailerList != null){
            mTrailerList = new ArrayList<>(trailerList);
        }
        notifyDataSetChanged();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView trailerIV;
        TextView trailerName;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            trailerIV = itemView.findViewById(R.id.trailer_play_view);
            trailerName = itemView.findViewById(R.id.trailer_name);
        }

    }
}
