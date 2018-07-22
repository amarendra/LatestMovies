package im.amar.latestmovies.api;

import im.amar.latestmovies.BuildConfig;
import im.amar.latestmovies.models.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiService {
    @GET("movie?api_key=" + BuildConfig.API_KEY + "&sort_by=release_date.desc&include_adult=false&include_video=false")
    Call<ApiResponse> discoverMovies(@Query("page") int page, @Query("primary_release_date_gte") String after,
                                     @Query("primary_release_date_lte") String before);
}
