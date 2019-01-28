package com.mx.moxietest.result.network;

import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 魔蝎测试接口，仅供参考
 */
public class HttpRequestUtils {
    private static OkHttpClient okHttpClient;

    // app_id用于服务端获取OCR结果 必须在服务端使用 生产环境app_id鉴权会校验服务器白名单 此处仅供调试使用
    //TODO #error 请填入测试的app_id 并删除此行
    public static final String APP_ID="00461de2d0b94c38a04e37828b19dee5";

    public static OkHttpClient getOkhttp(){
        if (okHttpClient==null){
            OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
            okhttpBuilder.connectTimeout(30, TimeUnit.SECONDS);
            okhttpBuilder.writeTimeout(30, TimeUnit.SECONDS);
            okhttpBuilder.readTimeout(30, TimeUnit.SECONDS);
            okhttpBuilder.addInterceptor(new HttpLoggingInterceptor(new MyLog()).setLevel(HttpLoggingInterceptor.Level.BODY));
            okHttpClient = okhttpBuilder.build();
        }

        return okHttpClient;
    }




    /**
     * 请求身份识别的接口
     * @param url
     * @param image
     * @param callback
     * @param isFront
     */
    public static void postOCRInfo(String url,byte[] image,NetworkCallback callback,boolean isFront){
        ApiParameterList apiParameterList;
        if (isFront){
            // 请求正面身份证信息接口
            apiParameterList = getCommonParameter("moxie.api.risk.orc.idcard.front");
        }else {
            apiParameterList = getCommonParameter("moxie.api.risk.orc.idcard.back");
        }
        String imageBase64 = Base64.encodeToString(image,Base64.NO_WRAP);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("trans_id",UUID.randomUUID().toString());
        jsonObject.put("image",imageBase64);
        apiParameterList.with("biz_content",jsonObject.toJSONString());
        postRequest(url,apiParameterList,callback);
    }


    public static void postSelfVerification(String url,byte[] image,NetworkCallback callback){
        ApiParameterList apiParameterList;
        apiParameterList = getCommonParameter("moxie.api.risk.self-idcard.verification");
        String imageBase64 = Base64.encodeToString(image,Base64.NO_WRAP);
        JSONObject jsonObject = new JSONObject();
        //TODO # error 填写身份证号码,并删除此行
        String idCard = "321322199402211210";
        //TODO # error 填写姓名,并删除此行
        String name = "耿义";
        jsonObject.put("id_card",idCard);
        jsonObject.put("name",name);
        jsonObject.put("trans_id",UUID.randomUUID().toString());
        jsonObject.put("image",imageBase64);
        apiParameterList.with("biz_content",jsonObject.toJSONString());
        if (idCard.isEmpty() || name.isEmpty()){
          return;
        }else {
            postRequest(url,apiParameterList,callback);
        }
    }



    public static void postRequest(String url,ApiParameterList apiParameters, final NetworkCallback callback){
        okHttpClient=getOkhttp();

        if (url == null || "".equals(url)) {
            sendFailResult(callback, 404, "URL无效");
            return;
        }

        Request.Builder builder = new Request.Builder();
        builder.addHeader("Content-Type","application/x-www-form-urlencoded;charset:utf-8");
        FormBody.Builder formBuild = new FormBody.Builder();
        for (ApiParameter param:apiParameters){
            formBuild.add(param.name,String.valueOf(param.value));
        }
        RequestBody requestBody=formBuild.build();
        Request request=builder.url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                sendFailResult(callback, 404, "网络请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dealRequestResponse(response, callback);
            }
        });

    }




    private static void dealRequestResponse(Response response, final NetworkCallback callback) throws IOException {
        if (response != null) {
            int code = response.code();
            if (code == 200) {
                String result = response.body().string();
                JSONObject resultObj = JSONObject.parseObject(result);
                boolean success = resultObj.getBooleanValue("success");
                String resultCode = resultObj.getString("code");
                String msg = resultObj.getString("msg");
                if (success){
                    sendSuccessResult(callback, result);
                }else{
                    sendFailResult(callback, Integer.parseInt(resultCode), msg);
                }
            } else {
                sendFailResult(callback, code, response.message());
            }
        } else {
            sendFailResult(callback, 0, "");
        }
    }

    public static <T> void sendFailResult(final NetworkCallback callback, final int errorCode, final String errorString) {
        if (callback != null) {
            callback.failed(errorCode, errorString);
        }
    }

    public static void sendSuccessResult(final NetworkCallback callback, final String response) {

        if (callback != null) {
            callback.completed(response);
        }
    }


    /**
     * 请求日志拦截器
     */
    public static class MyLog implements HttpLoggingInterceptor.Logger {

        @Override
        public void log(String message) {
            Log.i("moxieNetwork==", message);
        }
    }


    /**
     * 公共参数
     * @param methodName
     * @return
     */
    private static ApiParameterList getCommonParameter(String methodName){
        ApiParameterList apiParameterList = ApiParameterList.create();
        apiParameterList.with("app_id",APP_ID);
        apiParameterList.with("version","1.0");
        apiParameterList.with("method",methodName);
        apiParameterList.with("sign_type","TOKEN");
        apiParameterList.with("timestamp",System.currentTimeMillis());
        return apiParameterList;
    }

}
