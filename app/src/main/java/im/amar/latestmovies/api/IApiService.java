package im.amar.latestmovies.api;

import im.amar.latestmovies.BuildConfig;
import im.amar.latestmovies.models.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IApiService {
    /**
     * @param page  page number requested
     * @param after movies after this day (and before <today>) will be returned
     * @param votes minimum vote count; default is 25
     */
    @GET("movie?api_key=" + BuildConfig.API_KEY + "&sort_by=release_date.asc&include_adult=false&include_video=false")
    Call<ApiResponse> discoverMovies(@Query("page") int page,
                                     @Query("primary_release_date.gte") String after,
                                     @Query("vote_count.gte") int votes);
}
