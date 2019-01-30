package kr.zagros.shwan.moviemvvm.Entities

import androidx.recyclerview.widget.DiffUtil

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Movie {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("poster")
    @Expose
    var poster: String? = null
    @SerializedName("year")
    @Expose
    var year: String? = null
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("imdb_rating")
    @Expose
    var imdbRating: String? = null
    @SerializedName("genres")
    @Expose
    var genres: List<String>? = null
    @SerializedName("images")
    @Expose
    var images: List<String>? = null

    companion object {


        val DIFF_CALL: DiffUtil.ItemCallback<Movie> = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id === newItem.id
            }
        }
    }
}
