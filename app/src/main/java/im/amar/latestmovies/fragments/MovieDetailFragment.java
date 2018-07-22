package im.amar.latestmovies.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import im.amar.latestmovies.R;
import im.amar.latestmovies.models.Movie;

import static im.amar.latestmovies.utils.Utils.ARG_MOVIE;
import static im.amar.latestmovies.utils.Utils.BASE_URL_IMG;

public class MovieDetailFragment extends Fragment {
    private Movie mMovie;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            mMovie = (Movie) getArguments().getSerializable(ARG_MOVIE);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mMovie.title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        if (mMovie != null) {
            Picasso.get()
                    .load(BASE_URL_IMG + mMovie.poster_path)
                    .placeholder(R.drawable.img_no_image)
                    .fit()
                    .into((ImageView) rootView.findViewById(R.id.iv_poster));
            ((TextView) rootView.findViewById(R.id.tv_votes)).setText("Rating: " + String.valueOf(mMovie.vote_average) + " (" + mMovie.vote_count + ")");
            ((TextView) rootView.findViewById(R.id.title)).setText(mMovie.original_title);
            ((TextView) rootView.findViewById(R.id.rating_and_year)).setText(mMovie.overview);
        } else {
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }
}
