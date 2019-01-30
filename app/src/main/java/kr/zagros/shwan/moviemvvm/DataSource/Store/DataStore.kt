package kr.zagros.shwan.moviemvvm.DataSource.Store

import io.reactivex.Single
import kr.zagros.shwan.moviemvvm.Entities.MovieResponse

interface DataStore {

    fun getMovies(page: Int): Single<MovieResponse>
}
