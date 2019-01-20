package kr.zagros.shwan.moviemvvm.views.adapters;

import androidx.paging.PagedListAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import kr.zagros.shwan.moviemvvm.Entities.Movie;
import kr.zagros.shwan.moviemvvm.Entities.NetworkState;
import kr.zagros.shwan.moviemvvm.R;
import kr.zagros.shwan.moviemvvm.utils.GlideApp;
import kr.zagros.shwan.moviemvvm.views.CostumView.LimitedChatTextView;
import kr.zagros.shwan.moviemvvm.views.activities.RetryCallback;

public class MoviesAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {
    private String TAG = "MoviesAdapter";
    private int MOVIE_ITEM_VIEW_TYPE = 1;
    private int LOAD_ITEM_VIEW_TYPE = 0;
    private Context mContext;
    private RetryCallback retryCallback;
    private NetworkState myNetworkState;
    private boolean retryPageLoad = false;
    private String errorMsg;

    public MoviesAdapter(Context context, RetryCallback retryCallback) {
        super(Movie.DIFF_CALL);
        mContext = context;
        this.retryCallback = retryCallback;
    }

    @Override
    public int getItemViewType(int position) {
        return (isLoadingData() && position == getItemCount() - 1) ? LOAD_ITEM_VIEW_TYPE : MOVIE_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == MOVIE_ITEM_VIEW_TYPE) {
            view = inflater.inflate(R.layout.item_list, parent, false);
            return new MovieViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.load_progress_item, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MovieViewHolder) {
            MovieViewHolder movieViewHolder = (MovieViewHolder) viewHolder;
            Movie movie = getItem(position);
            movieViewHolder.bindData(movie);
        } else if (viewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingVH = (LoadingViewHolder) viewHolder;
            if (retryPageLoad) {
                loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                loadingVH.mProgressBar.setVisibility(View.GONE);

                loadingVH.mErrorTxt.setText(
                        errorMsg != null ?
                                errorMsg :
                                mContext.getString(R.string.error_msg_unknown));

            } else {
                loadingVH.mErrorLayout.setVisibility(View.GONE);
                loadingVH.mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setMyNetworkState(NetworkState networkState) {
        showRetry(networkState.getStatus() == NetworkState.Status.FAILED, networkState.getMsg());
        Log.e(TAG, "setNetworkState adapter:  networkState  " + networkState.getMsg());
        boolean wasLoading = isLoadingData();
        Log.e(TAG, "setNetworkState adapter: wasLoading  " + wasLoading);
        this.myNetworkState = networkState;
        boolean willLoad = isLoadingData();
        if (wasLoading != willLoad) {
            if (wasLoading) {
                Log.e(TAG, "setNetworkState adapter: notifyItemRemoved  ");
                notifyItemRemoved(getItemCount());
            } else {
                Log.e(TAG, "setNetworkState adapter: notifyItemInserted ");
                notifyItemInserted(getItemCount());
            }
        }

    }

    private boolean isLoadingData() {
        return (myNetworkState != null && myNetworkState != NetworkState.LOADED);
    }

    private void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(getItemCount() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    private class MovieViewHolder extends RecyclerView.ViewHolder {
        LimitedChatTextView movieTitleTextView;
        TextView reviewsNumberTextView;
        ImageView moviePosterImageView;
        SimpleRatingBar ratingBar;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitleTextView = itemView.findViewById(R.id.movie_title);
            reviewsNumberTextView = itemView.findViewById(R.id.reviews_number);
            moviePosterImageView = itemView.findViewById(R.id.movie_poster);
            ratingBar = itemView.findViewById(R.id.movie_rating_bar);
        }

        void bindData(Movie movie) {
            Log.e(TAG, "bindData: " + movie.getId());
            movieTitleTextView.setMaxTextChar(15);
            movieTitleTextView.setText(movie.getTitle());
            // reviewsNumberTextView.setText(String.valueOf(movie.getVoteCount()) + " " + mContext.getString(R.string.reviews));
            String url = "";
            if (movie.getImages() == null || movie.getImages().get(0) == null) {
                url = "https://efl-expertise.com/efl/uploads/2017/09/efllogoplaceholder.png";
            } else {
                url = movie.getImages().get(0);
            }
            GlideApp.with(mContext)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(moviePosterImageView);
            ratingBar.setRating(Float.valueOf(movie.getImdbRating()));
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.loadMore_progress);
            mRetryBtn = itemView.findViewById(R.id.retryLoadingButton);
            mErrorTxt = itemView.findViewById(R.id.loadMore_errorTxt);
            mErrorLayout = itemView.findViewById(R.id.loadMore_errorLayout);
        }
    }

}
