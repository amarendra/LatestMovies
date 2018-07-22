package im.amar.latestmovies;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import im.amar.latestmovies.adapters.MovieListAdapter;
import im.amar.latestmovies.api.IApiService;
import im.amar.latestmovies.api.RetrofitClient;
import im.amar.latestmovies.contracts.EndlessScrollListener;
import im.amar.latestmovies.models.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Calendar;

import static im.amar.latestmovies.utils.Utils.getDate;
import static im.amar.latestmovies.utils.Utils.isDateSame;
import static im.amar.latestmovies.utils.Utils.TAG;
import static im.amar.latestmovies.utils.Utils.today;

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
                mAdapter.notifyDataSetChanged();
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
        assert  mProgressBar != null;

        fetchMovies(); // fetch the first page of movies between 7 days ago and today
    }

    private void fetchMovies() {
        IApiService apiService = RetrofitClient.getClient().create(IApiService.class);
        Call<ApiResponse> call = apiService.discoverMovies(mPage, getDate(0, 0, 0), today());

        Log.d(TAG, "Request made");

        mProgressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d(TAG, "Response received: " + response.body().results);

                int currentSize = (mResponse != null && mResponse.results != null) ? mResponse.results.size() : 0;

                if (mResponse != null && mResponse.results != null && mResponse.results.size() > 0
                        && response.body() != null && response.body().results != null && response.body().results.size() > 0) {
                    mResponse.page = response.body().page;
                    mResponse.total_pages = response.body().total_pages;
                    mResponse.total_results = response.body().total_results;

                    mResponse.results.addAll(response.body().results);
                } else {
                    mResponse = response.body();
                }

                mAdapter.setData(mResponse.results);
                mAdapter.notifyItemRangeInserted(currentSize, response.body().results.size());

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

    private Calendar mCal = Calendar.getInstance();

    private String mAfterDateParam;
    private int mPage = 1;
}
