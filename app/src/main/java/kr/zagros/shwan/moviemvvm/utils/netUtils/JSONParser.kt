package kr.zagros.shwan.moviemvvm.utils.netUtils

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

import org.json.JSONException
import org.json.JSONObject

import kr.zagros.shwan.moviemvvm.Entities.Movie

object JSONParser {

    @Throws(JSONException::class)
    fun getMovieList(jsonString: String): List<Movie> {
        val responseJson: JSONObject
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        responseJson = JSONObject(jsonString)
        return gson.fromJson(responseJson.getJSONArray("data").toString(), object : TypeToken<List<Movie>>() {

        }.type)
    }
}
