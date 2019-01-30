package kr.zagros.shwan.moviemvvm.Entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieResponse {

    @SerializedName("data")
    @Expose
    var data: List<Movie>? = null
    @SerializedName("metadata")
    @Expose
    var metadata: Metadata? = null

}
