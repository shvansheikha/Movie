package kr.zagros.shwan.moviemvvm.views.adapters

import androidx.paging.PagedListAdapter
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.iarcuschin.simpleratingbar.SimpleRatingBar

import kr.zagros.shwan.moviemvvm.Entities.Movie
import kr.zagros.shwan.moviemvvm.Entities.NetworkState
import kr.zagros.shwan.moviemvvm.R
import kr.zagros.shwan.moviemvvm.views.CostumView.LimitedChatTextView
import kr.zagros.shwan.moviemvvm.views.activities.RetryCallback

class MoviesAdapter(private val mContext: Context, private val retryCallback: RetryCallback) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(Movie.DIFF_CALL) {
    private val t = "MoviesAdapter"
    private val MOVIE_ITEM_VIEW_TYPE = 1
    private val LOAD_ITEM_VIEW_TYPE = 0
    private var myNetworkState: NetworkState? = null
    private var retryPageLoad = false
    private var errorMsg: String? = null

    private val isLoadingData: Boolean
        get() = myNetworkState != null && myNetworkState !== NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingData && position == itemCount - 1) LOAD_ITEM_VIEW_TYPE else MOVIE_ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == MOVIE_ITEM_VIEW_TYPE) {
            view = inflater.inflate(R.layout.item_list, parent, false)
            return MovieViewHolder(view)
        } else {
            view = inflater.inflate(R.layout.load_progress_item, parent, false)
            return LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is MovieViewHolder) {
            val movie = getItem(position)
            if (movie != null) {
                viewHolder.bindData(movie)
            }
        } else if (viewHolder is LoadingViewHolder) {
            if (retryPageLoad) {
                viewHolder.mErrorLayout.visibility = View.VISIBLE
                viewHolder.mProgressBar.visibility = View.GONE
                viewHolder.mErrorTxt.text = if (errorMsg != null)
                    errorMsg
                else
                    mContext.getString(R.string.error_msg_unknown)

            } else {
                viewHolder.mErrorLayout.visibility = View.GONE
                viewHolder.mProgressBar.visibility = View.VISIBLE
            }
        }
    }

    fun setMyNetworkState(networkState: NetworkState) {
        showRetry(networkState.status == NetworkState.Status.FAILED, networkState.msg)
        Log.e(t, "setNetworkState adapter:  networkState  " + networkState.msg)
        val wasLoading = isLoadingData
        Log.e(t, "setNetworkState adapter: wasLoading  $wasLoading")
        this.myNetworkState = networkState
        val willLoad = isLoadingData
        if (wasLoading != willLoad) {
            if (wasLoading) {
                Log.e(t, "setNetworkState adapter: notifyItemRemoved  ")
                notifyItemRemoved(itemCount)
            } else {
                Log.e(t, "setNetworkState adapter: notifyItemInserted ")
                notifyItemInserted(itemCount)
            }
        }

    }

    private fun showRetry(show: Boolean, errorMsg: String?) {
        retryPageLoad = show
        notifyItemChanged(getItemCount() - 1)
        if (errorMsg != null) this.errorMsg = errorMsg
    }

    private inner class MovieViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var movieTitleTextView: LimitedChatTextView = itemView.findViewById(R.id.movie_title)
        internal var reviewsNumberTextView: TextView = itemView.findViewById(R.id.reviews_number)
        internal var moviePosterImageView: ImageView = itemView.findViewById(R.id.movie_poster)
        internal var ratingBar: SimpleRatingBar = itemView.findViewById(R.id.movie_rating_bar)

        internal fun bindData(movie: Movie) {
            Log.e(t, "bindData: " + movie.id!!)
            movieTitleTextView.maxTextChar = 15
            movieTitleTextView.text = movie.title
            val url: String = if (movie.images == null) {
                "https://efl-expertise.com/efl/uploads/2017/09/efllogoplaceholder.png"
            } else {
                movie.images!![0]
            }
            Glide.with(mContext)
                    .load(url)
                 //   .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(moviePosterImageView)
            ratingBar.rating = java.lang.Float.valueOf(movie.imdbRating!!)
        }
    }

    private inner class LoadingViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mProgressBar: ProgressBar = itemView.findViewById(R.id.loadMore_progress)
        val mRetryBtn: ImageButton = itemView.findViewById(R.id.retryLoadingButton)
        val mErrorTxt: TextView = itemView.findViewById(R.id.loadMore_errorTxt)
        val mErrorLayout: LinearLayout = itemView.findViewById(R.id.loadMore_errorLayout)

    }

}
