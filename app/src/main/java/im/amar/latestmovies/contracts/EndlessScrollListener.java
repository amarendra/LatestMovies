package im.amar.latestmovies.contracts;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int thresholdIndex = 5;   // when item number reaches less or these many, make another call
    private int currentPageIndex = 0;
    private int lastTotalMoviesCount = 0;
    private boolean isStillLoading = true;
    private int firstPageIndex = 0;

    RecyclerView.LayoutManager mLayoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }

            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }

        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        if (totalItemCount < lastTotalMoviesCount) {
            this.currentPageIndex = this.firstPageIndex;
            this.lastTotalMoviesCount = totalItemCount;
            if (totalItemCount == 0) {
                this.isStillLoading = true;
            }
        }

        if (isStillLoading && (totalItemCount > lastTotalMoviesCount)) {
            isStillLoading = false;
            lastTotalMoviesCount = totalItemCount;
        }

        if (!isStillLoading && (lastVisibleItemPosition + thresholdIndex) > totalItemCount) {
            currentPageIndex++;
            onLoadMore(currentPageIndex, totalItemCount, view);
            isStillLoading = true;
        }
    }

    public void resetIndices() {
        this.currentPageIndex = this.firstPageIndex;
        this.lastTotalMoviesCount = 0;
        this.isStillLoading = true;
    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}