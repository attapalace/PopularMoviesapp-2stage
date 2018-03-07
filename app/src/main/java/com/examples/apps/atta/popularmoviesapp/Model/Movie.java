package com.examples.apps.atta.popularmoviesapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmedatta on 2/19/18.
 */

public class Movie implements Parcelable{

    public int id;
    public String title;
    public String posterImage;
    public String overView;
    public double userRating;
    public String releaseDate;

    public Movie() {
    }

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterImage = in.readString();
        overView = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public String getOverView() {
        return overView;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterImage);
        dest.writeString(overView);
        dest.writeDouble(userRating);
        dest.writeString(releaseDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
