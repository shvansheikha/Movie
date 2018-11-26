package kr.zagros.shwan.moviemvvm.DataSource.Store;

import io.reactivex.Single;
import kr.zagros.shwan.moviemvvm.Entities.MovieResponse;

public interface DataStore {

    Single<MovieResponse> getMovies(int page);
}
