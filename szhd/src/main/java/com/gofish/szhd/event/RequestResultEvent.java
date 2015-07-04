package com.gofish.szhd.event;

public class RequestResultEvent {
    public final boolean isSuccess;
    public final String errMessage;

    public RequestResultEvent(boolean isSuccess, String errMessage) {
        this.isSuccess = isSuccess;
        this.errMessage = errMessage;
    }
}
