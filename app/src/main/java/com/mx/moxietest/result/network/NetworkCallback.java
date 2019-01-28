package com.mx.moxietest.result.network;

public abstract class NetworkCallback {
    public abstract void completed(String response);

    /**
     * @param httpStatusCode
     * @param error
     */
    public void failed(int httpStatusCode, String error) {

    }
}
