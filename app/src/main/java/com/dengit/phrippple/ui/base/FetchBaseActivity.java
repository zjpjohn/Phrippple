package com.dengit.phrippple.ui.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dengit.phrippple.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import butterknife.Bind;
import timber.log.Timber;

/**
 * Created by dengit on 15/12/7.
 */
public abstract class FetchBaseActivity<T> extends BaseActivity implements FetchBaseView<T>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.initial_progress_bar)
    protected ProgressWheel mInitialProgressBar;

    @Bind(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @Bind(R.id.refresh_layout)
    protected SwipeRefreshLayout mRefreshLayout;

    protected View mFooter;
    protected ProgressWheel mFooterProgressBar;
    protected TextView mLoadMoreTV;
    protected LinearLayout mFooterLayout;

    private FetchBasePresenter<T> mFetchBasePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        mBasePresenter = new BasePresenterImpl<T>(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.footer) {
            mFetchBasePresenter.onFooterClick();
        }
    }

    @Override
    public void switchRefresh(boolean isOpen) {
        mRefreshLayout.setRefreshing(isOpen);
    }

    @Override
    public void onRefresh() {
        mFetchBasePresenter.fetchNewestItems(true);
    }

    @Override
    public void switchLoadMore(boolean isOpen, boolean isEnd) {
        if (isOpen) {
            mFooter.setClickable(false);
            mFooterProgressBar.setVisibility(View.VISIBLE);
            mLoadMoreTV.setText("loading");
        } else {
            setFooterStatus(isEnd);
        }
    }

    @Override
    public void handleError() {
        tryToGoneInitialProgressBar();
        switchRefresh(false);
        switchLoadMore(false, false);
        Toast.makeText(this, "network error! swipe to refresh it!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setItems(List<T> newItems, boolean isEnd) {

        tryToGoneInitialProgressBar();
        setAdapterData(newItems);

        if (mFooterLayout == null) {
            Timber.d("mFooterLayout == null");
            return;
        }
        setFooterStatus(isEnd);
    }

    @Override
    public void appendItems(List<T> newItems) {
        appendAdapterData(newItems);
    }

    protected void setBasePresenter(FetchBasePresenter<T> fetchBasePresenter) {
        mFetchBasePresenter = fetchBasePresenter;
    }

    protected abstract void setAdapterData(List<T> newItems);

    protected abstract void appendAdapterData(List<T> newItems);

    protected void setupBase() {
        mFooterLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.list_footer, null);
        mFooter = mFooterLayout.findViewById(R.id.footer);
        mFooterProgressBar = (ProgressWheel) mFooterLayout.findViewById(R.id.footer_progressbar);
        mLoadMoreTV = (TextView) mFooterLayout.findViewById(R.id.footer_loadmore);

        mFooter.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
//        mRefreshLayout.setWaveColor(Util.getColor(R.color.colorPrimary));

        //todo setMaxDropHeight noneffective
//        mRefreshLayout.setMaxDropHeight(getResources().getDimensionPixelSize(R.dimen.dropMaxHeight));
    }

    private void tryToGoneInitialProgressBar() {
        if (mInitialProgressBar.getVisibility() == View.VISIBLE) {
            mInitialProgressBar.setVisibility(View.GONE);
        }
    }

    private void setFooterStatus(boolean isEnd) {
        mFooterProgressBar.setVisibility(View.GONE);
        if (isEnd) {
            mFooter.setClickable(false);
            mLoadMoreTV.setText("no more data");
        } else {
            mFooter.setClickable(true);
            mLoadMoreTV.setText("click to load more");
        }
    }
}