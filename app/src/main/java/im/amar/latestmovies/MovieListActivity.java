package im.amar.latestmovies;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import im.amar.latestmovies.adapters.MovieListAdapter;
import im.amar.latestmovies.api.IApiService;
import im.amar.latestmovies.api.RetrofitClient;
import im.amar.latestmovies.contracts.EndlessScrollListener;
import im.amar.latestmovies.models.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static im.amar.latestmovies.utils.Utils.DEFAULT_MIN_VOTE;
import static im.amar.latestmovies.utils.Utils.KEY_VOTE_COUNT;
import static im.amar.latestmovies.utils.Utils.SHARED_PREF_FILE_NAME;
import static im.amar.latestmovies.utils.Utils.TAG;
import static im.amar.latestmovies.utils.Utils.getDate;
import static im.amar.latestmovies.utils.Utils.isDateSame;
import static im.amar.latestmovies.utils.Utils.lastMonth;

public class MovieListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private ApiResponse mResponse;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private EndlessScrollListener mScrollListener;
    private MovieListAdapter mAdapter;
    private DatePickerDialog mPicker;
    private Calendar mCal = Calendar.getInstance();
    private String mAfterDateParam = lastMonth();
    private int mPage = 1;

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Log.d(TAG, "date changed: " + year + "-" + month + "-" + day);

            if (!isDateSame(mAfterDateParam, year, month, day)) {
                mAfterDateParam = getDate(year, month, day);
                mScrollListener.resetIndices();
                mPage = 1;
                mResponse.results.clear();
                mAdapter.setData(mResponse.results);
                fetchMovies();
            } else {
                Log.d(TAG, "Same date");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Fab clicked");
                mPicker.show();
            }
        });

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }

        mPicker = new DatePickerDialog(this, mDateSetListener, mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH),
                mCal.get(Calendar.DAY_OF_MONTH));

        mRecyclerView = findViewById(R.id.movie_list);
        assert mRecyclerView != null;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mScrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPage++;
                fetchMovies();
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);
        mResponse = new ApiResponse();
        mResponse.results = new ArrayList<>();

        mAdapter = new MovieListAdapter(this, mResponse.results, mTwoPane);
        mRecyclerView.setAdapter(mAdapter);

        mProgressBar = findViewById(R.id.progress_bar);
        assert mProgressBar != null;

        fetchMovies(); // fetch the first page of movies between 7 days ago and today
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_vote).setTitle("Minimum vote: " + getMinVoteCount());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_vote:

                return true;
            case R.id.menu_about:
                Toast.makeText(getApplicationContext(), "Just a simple movie app, nothing fancy.", Toast.LENGTH_LONG).show();
                return true;

            case R.id.vote_none:
                setMinVoteCount(0);
                return true;

            case R.id.vote_10:
                setMinVoteCount(10);
                return true;

            case R.id.vote_25:
                setMinVoteCount(25);
                return true;

            case R.id.vote_50:
                setMinVoteCount(50);
                return true;

            case R.id.vote_500:
                setMinVoteCount(500);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchMovies() {
        IApiService apiService = RetrofitClient.getClient().create(IApiService.class);
        Call<ApiResponse> call = apiService.discoverMovies(mPage, mAfterDateParam, getMinVoteCount());

        Log.d(TAG, "Request made - mPage: " + mPage + " After: " + mAfterDateParam);

        mProgressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d(TAG, "Response received: " + response.body().results);

                int currentSize = mResponse.results.size();

                if (mResponse != null && mResponse.results != null && mResponse.results.size() > 0
                        && response.body() != null && response.body().results != null && response.body().results.size() > 0) {
                    mResponse.page = response.body().page;
                    mResponse.total_pages = response.body().total_pages;
                    mResponse.total_results = response.body().total_results;

                    mResponse.results.addAll(response.body().results);
                } else {
                    if (mResponse.results.size() == 0) {
                        mResponse = response.body();
                    } else {
                        Toast.makeText(getApplicationContext(), "No more movies :(. Maybe choose an older date, or lower vote count", Toast.LENGTH_LONG).show();
                    }
                }

                if (currentSize != mResponse.results.size()) {
                    mAdapter.setData(mResponse.results);
                } else {
                    Log.d(TAG, "Data size didn't change");  // test for breaking edge cases todo
                }

                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "API Fail received: " + Log.getStackTraceString(t));
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Fetching movies failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private int getMinVoteCount() {
        return getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE).getInt(KEY_VOTE_COUNT, DEFAULT_MIN_VOTE);
    }

    // there will be no forced API call on min vote change, however it will be picked on next calls onwards
    private void setMinVoteCount(int votes) {
        getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE).edit().putInt(KEY_VOTE_COUNT, votes).apply();
        Toast.makeText(getApplicationContext(), "Min vote count set to: " + votes, Toast.LENGTH_LONG).show();
    }
}
