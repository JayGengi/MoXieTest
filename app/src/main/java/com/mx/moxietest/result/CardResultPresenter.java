package com.mx.moxietest.result;

import android.util.Log;

import com.mx.moxietest.result.network.HttpRequestUtils;
import com.mx.moxietest.result.network.NetworkCallback;

public class CardResultPresenter {
    private static final String TAG = "MXCardResultPresenter";


    public CardResultPresenter() {
        Log.i(TAG, "MXCardResultPresenter" + "***online");
    }


    public void getOCRInfo(byte[] image,final IOCRInfoCallback callback,boolean isFront){
        if (image!=null && image.length>0){
            // 魔蝎测试接口
            HttpRequestUtils.postOCRInfo("https://api.51datakey.com/identity/api/gateway", image, new NetworkCallback() {
                @Override
                public void completed(String response) {
                    callback.callback(response);
                }

                @Override
                public void failed(int httpStatusCode, String error) {
                    super.failed(httpStatusCode, error);
                    callback.fail(error);
                }
            },isFront);
        }
    }


    public void getSelfVerification(byte[] image,final ISelfVerificationCallback callback){
        if (image!=null && image.length>0){
            // 魔蝎测试接口
            HttpRequestUtils.postSelfVerification("https://api.51datakey.com/identity/api/gateway", image, new NetworkCallback() {
                @Override
                public void completed(String response) {
                    callback.callback(response);
                }

                @Override
                public void failed(int httpStatusCode, String error) {
                    super.failed(httpStatusCode, error);
                    callback.fail(error);
                }
            });
        }
    }


    public interface ISelfVerificationCallback{
        void callback(String result);

        void fail(String error);
    }

    public interface IOCRInfoCallback{
        void callback(String result);

        void fail(String error);
    }

}
