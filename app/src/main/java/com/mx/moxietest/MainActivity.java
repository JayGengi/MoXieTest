package com.mx.moxietest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.moxie.liveness.MXLivenessSDK;
import com.moxie.liveness.base.MXPermissionManager;
import com.moxie.liveness.ui.LivenessActivity;
import com.moxie.liveness.util.MXReturnResult;
import com.mx.moxietest.result.CardResultPresenter;
import com.mx.moxietest.result.data.LivenessResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG="MainActivity";

    private CardResultPresenter mCardPresenter;
    /**
     * 活体检测请求码
     */
    private static final int KEY_TO_DETECT_REQUEST_CODE = 104;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView btn_liveness = findViewById(R.id.btn_liveness);
        btn_liveness.setOnClickListener(this);
    }


    @Override
    public void onClick(final View v) {
        MXPermissionManager manager=new MXPermissionManager(new MXPermissionManager.PermissionListener() {
            @Override
            public void allGranted() {
                clickBtn(v);
                Toast.makeText(MainActivity.this,"权限获取成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void deniedPermission(String[] deniedPermission) {
                Toast.makeText(MainActivity.this,"权限获取失败",Toast.LENGTH_SHORT).show();
            }
        });
        manager.startRequestPermission(this);
    }

    private void clickBtn(View view){
        switch (view.getId()){
            case R.id.btn_liveness:
                // 活体检测
                startLiveness();
                break;
            default:
                break;
        }
    }
    /**
     * @des    Liveness 开启活体认证
     * @auther JayGengi
     * @data 2019/1/28 13:23
     * @email JayGengi@163.com
     */
    private void startLiveness() {
        Intent intent = new Intent();
        intent.setClass(this, LivenessActivity.class);
        // KEY_PRODUCTION_MODE，true为生产环境，false为开发环境，默认是false
        // 注意上线产品设置为生产环境
        intent.putExtra(LivenessActivity.KEY_PRODUCTION_MODE,false);
        //OUTPUT_TYPE 配置, 传入的outputType类型为singleImg （单图），multiImg （多图），video（低质量视频），fullvideo（高质量视频）
        intent.putExtra(LivenessActivity.OUTTYPE, Constants.VIDEO);
        //EXTRA_MOTION_SEQUENCE 动作检测序列配置，支持四种检测动作， BLINK(眨眼), MOUTH（张嘴）, NOD（点头）, YAW（摇头）, 各个动作以空格隔开。 推荐第一个动作为BLINK。
        //默认配置为"BLINK MOUTH NOD YAW"
        intent.putExtra(LivenessActivity.EXTRA_MOTION_SEQUENCE, "BLINK MOUTH NOD YAW");
        //SOUND_NOTICE 配置, 传入的soundNotice为boolean值，true为打开, false为关闭。
        intent.putExtra(LivenessActivity.SOUND_NOTICE, true);
        //COMPLEXITY 配置, 传入的complexity类型为normal,支持四种难度，easy, normal, hard, hell.
        intent.putExtra(LivenessActivity.COMPLEXITY, Constants.HARD);
        //设置返回protobuf结果,默认为true
        intent.putExtra(LivenessActivity.KEY_DETECT_PROTO_BUF_RESULT, true);
        startActivityForResult(intent, KEY_TO_DETECT_REQUEST_CODE);
    }
    /**
     * @des    回调方法
     * @auther JayGengi
     * @data 2019/1/28 13:28
     * @email JayGengi@163.com
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case KEY_TO_DETECT_REQUEST_CODE:
                // 活体检测的结果回调
                dealDetectResult(data, resultCode);
                break;
        }
    }
    /**
     * @des    活体检测的结果回调
     * @auther JayGengi
     * @data 2019/1/28 13:29
     * @email JayGengi@163.com
     */
    private void dealDetectResult(Intent data, int resultCode) {
        switch (resultCode) {
            case LivenessActivity.RESULT_LIVENESS_OK:
                detectSuccess(data);
                Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",活体检测成功");
                break;
            case LivenessActivity.RESULT_SDK_INIT_FAIL_APPLICATION_ID_ERROR:
                showToast(Constants.ERROR_PACKAGE);
                Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",未替换包名或包名错误");
                break;
            case LivenessActivity.RESULT_SDK_INIT_FAIL_LICENSE_OUT_OF_DATE:
                showToast(Constants.ERROR_LICENSE_OUT_OF_DATE);
                Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",授权文件过期");
                break;
            case LivenessActivity.RESULT_SDK_INIT_FAIL_OUT_OF_DATE:
                showToast(Constants.ERROR_SDK_INITIALIZE);
                Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",算法SDK初始化失败：可能是授权文件或模型路径错误，SDK权限过期，包名绑定错误");
                break;
            case LivenessActivity.RESULT_CREATE_HANDLE_ERROR:
                showToast(Constants.ERROR_SDK_INITIALIZE);
                Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",算法SDK初始化失败：可能是授权文件或模型路径错误，SDK权限过期，包名绑定错误");
                break;
            case LivenessActivity.RESULT_CAMERA_ERROR_NOPRERMISSION_OR_USED:
                showToast(Constants.ERROR_CAMERA_REFUSE);
                Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",相机权限获取失败或权限被拒绝");
                break;
            case LivenessActivity.RESULT_CANCELED:
                showToast("检测取消");
                Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",检测取消");
                break;
        }
    }
    /**
     * @des    活体检测成功方法
     * @auther JayGengi
     * @data 2019/1/28 13:30
     * @email JayGengi@163.com
     */
    private void detectSuccess(Intent data) {
        /***********************************************
         * 请将图片上传到自己服务端,调用魔蝎接口进行实人认证  *
         *                                             *
         ***********************************************/
        MXReturnResult returnResult = new MXReturnResult();
        if (data != null) {
            returnResult = (MXReturnResult) data.getSerializableExtra(LivenessActivity.KEY_DETECT_RESULT);
        }
//        MXLivenessSDK.MXLivenessImageResult[] images = returnResult.getImageResults();
//        if (images!=null && images.length>0){
//            mCardPresenter.getSelfVerification(images[0].image, new CardResultPresenter.ISelfVerificationCallback() {
//                @Override
//                public void callback(String result) {
//                    LivenessResult livenessResult= JSON.parseObject(result,LivenessResult.class);
//                    if (livenessResult.getData()!=null && livenessResult.getData().getStatus().equals("1")){
//                        showToast("人证比对成功");
//                    }else {
//                        showToast("认证比对失败:"+livenessResult.getData().getReason());
//                    }
//                }
//
//                @Override
//                public void fail(String error) {
//                    showToast("人证比对失败："+error);
//                }
//            });
//        }
        toDetectResult(returnResult);
    }
    private void toDetectResult(MXReturnResult imageResult) {
        Intent intent = new Intent(this, MXLivenessDetectResultActivity.class);
        intent.putExtra(MXLivenessDetectResultActivity.KEY_DETECT_RESULT, imageResult);
        startActivity(intent);
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
