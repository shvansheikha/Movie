package kr.zagros.shwan.moviemvvm.DataSource.PagedSources

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import android.util.Log

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.util.concurrent.Executor

import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService
import kr.zagros.shwan.moviemvvm.DataSource.Store.DataStoreImp
import kr.zagros.shwan.moviemvvm.Entities.Movie
import kr.zagros.shwan.moviemvvm.Entities.NetworkState
import kr.zagros.shwan.moviemvvm.utils.Config
import kr.zagros.shwan.moviemvvm.utils.netUtils.JSONParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesDataSource internal constructor(private val executor: Executor, private val apiService: ApiService) : PageKeyedDataSource<Long, Movie>() {
    private val storeImp: DataStoreImp? = null

    private val params: PageKeyedDataSource.LoadParams<Long>? = null
    private val callback: PageKeyedDataSource.LoadCallback<Long, Movie>? = null

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Long>, callback: PageKeyedDataSource.LoadInitialCallback<Long, Movie>) {
        Log.e(TAG, "loadInitial: ")
        networkState.postValue(NetworkState.LOADING)
        apiService.getMovies(Config.API_DEFAULT_PAGE_KEY.toLong()).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseString: String
                val movieList: List<Movie>
                if (response.isSuccessful && response.code() == 200) {
                    try {
                        networkState.postValue(NetworkState.LOADED)
                        responseString = response.body()!!.string()
                        movieList = JSONParser.getMovieList(responseString)
                        for (m in movieList) {
                            Log.e(TAG, "onResponse: " + m.id!!)
                        }
                        callback.onResult(movieList, null, 2.toLong())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    Log.e(TAG, "onResponse: " + response.message())
                    networkState.postValue(NetworkState(NetworkState.Status.FAILED, response.message(), null))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errorMessage = t.message
                Log.e(TAG, "onFailure: $errorMessage")
                networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage!!, t))
            }
        })
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Long>, callback: PageKeyedDataSource.LoadCallback<Long, Movie>) {}

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Long>, callback: PageKeyedDataSource.LoadCallback<Long, Movie>) {
        Log.e(TAG, "load page: " + params.key)
        networkState.postValue(NetworkState.LOADING)
        apiService.getMovies(params.key).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseJson: JSONObject
                val responseString: String
                val moviesList: List<Movie>
                val nextKey: Long?
                if (response.isSuccessful && response.code() == 200) {
                    try {
                        networkState.postValue(NetworkState.LOADED)
                        responseString = response.body()!!.string()
                        moviesList = JSONParser.getMovieList(responseString)
                        responseJson = JSONObject(responseString)
                        nextKey = if (params.key == responseJson.getJSONObject("metadata").getInt("page_count").toLong()) null else params.key + 1
                        callback.onResult(moviesList, nextKey)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    Log.e(TAG, "onResponse error " + response.message())
                    networkState.postValue(NetworkState(NetworkState.Status.FAILED, response.message(), null))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errorMessage = t.message
                Log.e(TAG, "onFailure: $errorMessage")
                networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage!!, t))
            }
        })

    }

    companion object {
        private const val TAG = "MoviesDataSource"
    }
}
