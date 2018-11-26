package kr.zagros.shwan.moviemvvm.Entities;

public class NetworkState {
    private final Status status;
    private final String msg;
    private final Throwable throwable;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    public NetworkState(Status status, String msg, Throwable throwable) {
        this.status = status;
        this.msg = msg;
        this.throwable = throwable;
    }

    static {
        LOADING=new NetworkState(Status.RUNNING,"Running",null);
        LOADED=new NetworkState(Status.SUCCESS,"Success",null);
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public enum Status{
        RUNNING,
        SUCCESS,
        FAILED
    }

}