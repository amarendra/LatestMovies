package im.amar.latestmovies.models;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {
    public ArrayList<Movie> results;
    public int page;
    public int total_results;
    public int total_pages;

    @Override
    public String toString() {
        return "ApiResponse{" +
                "results=" + results +
                ", page=" + page +
                ", total_results=" + total_results +
                ", total_pages=" + total_pages +
                '}';
    }
}
