package kr.zagros.shwan.moviemvvm.views.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    public int MOVIE_ITEM_VIEW_TYPE = 1;
    public int LOAD_ITEM_VIEW_TYPE = 0;
    private Context mContext;
    private RetryCallback retryCallback;
    private NetworkState mNetworkState;
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
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;

        }
    }

    public void setmNetworkState(NetworkState networkState){}

    private boolean isLoadingData() {
        return (mNetworkState != null && mNetworkState != NetworkState.LOADED);
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
            movieTitleTextView.setMaxTextChar(15);
            movieTitleTextView.setText(movie.getTitle());
            // reviewsNumberTextView.setText(String.valueOf(movie.getVoteCount()) + " " + mContext.getString(R.string.reviews));
            GlideApp.with(mContext)
                    .load(movie.getImages().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(moviePosterImageView);
            ratingBar.setRating(Float.valueOf(movie.getImdbRating()));
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
