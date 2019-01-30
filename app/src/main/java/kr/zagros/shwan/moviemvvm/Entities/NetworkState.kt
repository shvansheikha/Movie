package kr.zagros.shwan.moviemvvm.Entities

class NetworkState(val status: Status, val msg: String, val throwable: Throwable?) {

    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

    companion object {

        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success", null)
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running", null)

    }

}