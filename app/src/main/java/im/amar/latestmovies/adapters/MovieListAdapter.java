package im.amar.latestmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import im.amar.latestmovies.MovieDetailActivity;
import im.amar.latestmovies.MovieListActivity;
import im.amar.latestmovies.R;
import im.amar.latestmovies.fragments.MovieDetailFragment;
import im.amar.latestmovies.models.Movie;

import static im.amar.latestmovies.utils.Utils.ARG_MOVIE;
import static im.amar.latestmovies.utils.Utils.BASE_URL_IMG;
import static im.amar.latestmovies.utils.Utils.getGenre;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private final MovieListActivity mParentActivity;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Movie movie = (Movie) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putSerializable(ARG_MOVIE, movie);
                MovieDetailFragment fragment = new MovieDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra(ARG_MOVIE, movie);

                context.startActivity(intent);
            }
        }
    };
    private ArrayList<Movie> mMovies;

    public MovieListAdapter(MovieListActivity parent, ArrayList<Movie> movies, boolean twoPane) {
        mMovies = movies;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    public void setData(ArrayList<Movie> movies) {
        int currentSize = mMovies.size();
        mMovies = movies;
        int newSize = mMovies.size();

        if (newSize > currentSize) {
            notifyItemRangeInserted(currentSize, (newSize - currentSize));
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        Picasso.get()
                .load(BASE_URL_IMG + movie.poster_path)
                .placeholder(R.drawable.img_no_image_small)
                .fit()
                .into(holder.mPoster);

        holder.mTitle.setText(movie.title);
        holder.mRatingAndYear.setText(ratingAndYear(movie));
        holder.mGenres.setText(genreString(movie.genre_ids));

        holder.itemView.setTag(movie);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    private String genreString(int[] genres) {
        String genreString = "";

        for (int i = 0; i < genres.length; i++) {
            String genre = getGenre(genres[i]);

            if (!TextUtils.isEmpty(genre)) {
                genreString += genre + ((i != (genres.length - 1)) ? ", " : "");
            }
        }

        return "Genres: " + genreString;
    }

    private String ratingAndYear(Movie movie) {
        String str = "";

        String vote = String.valueOf(movie.vote_average) + " (" + movie.vote_count + ")";
        String year = movie.release_date.substring(0, 4);

        if (!TextUtils.isEmpty(vote)) {
            str = vote + " | " + year;
        } else {
            str = year;
        }

        return str;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mPoster;
        final TextView mTitle;
        final TextView mRatingAndYear;
        final TextView mGenres;

        ViewHolder(View view) {
            super(view);
            mPoster = (ImageView) view.findViewById(R.id.iv_poster);
            mTitle = (TextView) view.findViewById(R.id.title);
            mRatingAndYear = (TextView) view.findViewById(R.id.rating_and_year);
            mGenres = (TextView) view.findViewById(R.id.genre);
        }
    }
}
