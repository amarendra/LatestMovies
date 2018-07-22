package im.amar.latestmovies.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public final class Utils {
    public static final String BASE_URL_IMG = "http://image.tmdb.org/t/p/w200";
    public static final String TAG = "LatestMovies";
    public static final String ARG_MOVIE = "movie";
    public static final String SHARED_PREF_FILE_NAME = "latest_movies_shared_pref";
    public static final String KEY_VOTE_COUNT = "vote_count";
    public static final int DEFAULT_MIN_VOTE = 25;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String GENRE_JSON = "[\n" +
            "  {\n" +
            "    \"id\": 28,\n" +
            "    \"name\": \"Action\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 12,\n" +
            "    \"name\": \"Adventure\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 16,\n" +
            "    \"name\": \"Animation\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 35,\n" +
            "    \"name\": \"Comedy\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 80,\n" +
            "    \"name\": \"Crime\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 99,\n" +
            "    \"name\": \"Documentary\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 18,\n" +
            "    \"name\": \"Drama\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 10751,\n" +
            "    \"name\": \"Family\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 14,\n" +
            "    \"name\": \"Fantasy\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 36,\n" +
            "    \"name\": \"History\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 27,\n" +
            "    \"name\": \"Horror\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 10402,\n" +
            "    \"name\": \"Music\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 9648,\n" +
            "    \"name\": \"Mystery\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 10749,\n" +
            "    \"name\": \"Romance\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 878,\n" +
            "    \"name\": \"Science Fiction\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 10770,\n" +
            "    \"name\": \"TV Movie\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 53,\n" +
            "    \"name\": \"Thriller\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 10752,\n" +
            "    \"name\": \"War\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 37,\n" +
            "    \"name\": \"Western\"\n" +
            "  }\n" +
            "]";
    private static HashMap<Integer, String> GENRE_MAP = new HashMap<>();

    static {
        try {
            JSONArray jsonArray = new JSONArray(GENRE_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                GENRE_MAP.put(jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("name"));
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception: " + Log.getStackTraceString(ex));
        }
    }

    private Utils() {
    }

    public static String getGenre(int id) {
        return (GENRE_MAP != null && GENRE_MAP.size() > 0) ? GENRE_MAP.get(id) : "";
    }

    public static String lastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);

        return new SimpleDateFormat(DATE_FORMAT).format(calendar.getTime());
    }

    public static boolean isDateSame(String date, int year, int month, int day) {
        return !TextUtils.isEmpty(date) && date.equalsIgnoreCase(getDate(year, month, day));
    }

    public static String getDate(int year, int month, int day) {
        if (year != 0 && month != 0 && day != 0) {
            return year + "-" + month + "-" + day;
        } else {
            return lastMonth();
        }
    }
}
