package im.amar.latestmovies.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class Movie implements Serializable {
    // keeping the name as per the response to avoid using @SerializedName to save some time
    public int vote_count;
    public int id;
    public boolean video;
    public float vote_average;
    public String title;
    public String original_title;   // both titles can be shown later todo
    public float popularity;
    public String poster_path;
    public String original_language;
    public int[] genre_ids; // can be used for a genre mapping to be shown on the page todo
    public String backdrop_path;
    public boolean adult;
    public String overview;

    public String release_date;
    public Date releaseDate;    // not part of API

    @Override
    public String toString() {
        return "Movie{" +
                "vote_count=" + vote_count +
                ", id=" + id +
                ", video=" + video +
                ", vote_average=" + vote_average +
                ", title='" + title + '\'' +
                ", original_title='" + original_title + '\'' +
                ", popularity=" + popularity +
                ", poster_path='" + poster_path + '\'' +
                ", original_language='" + original_language + '\'' +
                ", genre_ids=" + Arrays.toString(genre_ids) +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", adult=" + adult +
                ", overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
