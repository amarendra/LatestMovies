package im.amar.latestmovies.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public final class Utils {
    private Utils() {
    }

    public static final String BASE_URL_IMG = "http://image.tmdb.org/t/p/w200";

    public static final String TAG = "LatestMovies";
    public static final String ARG_MOVIE = "movie";

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
            Gson gson = new Gson();
            GENRE_MAP = gson.fromJson(GENRE_JSON, new TypeToken<HashMap<Integer, String>>() {}.getType());
        } catch (Exception ex) {
            Log.e(TAG, "Exception: " + Log.getStackTraceString(ex));
        }
    }

    public static String getGenre(int id) {
        return (GENRE_MAP != null && GENRE_MAP.size() > 0) ? GENRE_MAP.get(id) : "";
    }

    public static String today() {
        Date today = new Date();
        return new SimpleDateFormat(DATE_FORMAT).format(today);
    }

    public static String lastWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -7);

        return new SimpleDateFormat(DATE_FORMAT).format(calendar.getTime());
    }

    public static boolean isDateSame(String date, int year, int month, int day) {
        return  !TextUtils.isEmpty(date) && date.equalsIgnoreCase(getDate(year, month, day));
    }

    public static String getDate(int year, int month, int day) {
        if (year != 0 && month != 0 && day != 0) {
            return year + "-" + month + "-" + day;
        } else {
            return lastWeek();
        }
    }
}
