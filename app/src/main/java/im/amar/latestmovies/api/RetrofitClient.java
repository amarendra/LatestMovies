package im.amar.latestmovies.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/";

    private static Retrofit sINSTANCE = null;
    private static Object LOCK = new Object();

    public static Retrofit getClient() {   // check this thread safe impl. todo
        Retrofit client = sINSTANCE;

        if (client == null) {
            synchronized (LOCK) {
                client = sINSTANCE;

                if (client == null) {
                    sINSTANCE = client = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return sINSTANCE;
    }
}
