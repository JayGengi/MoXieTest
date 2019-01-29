package com.mx.moxietest;

import android.content.Intent;
import android.graphics.Color;
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
import com.moxie.ocr.ocr.idcard.IDCardActivity;
import com.moxie.ocr.ocr.idcard.IDCardRecognizer;
import com.moxie.ocr.ocr.idcard.MXOCRResult;
import com.mx.moxietest.ocr.activity.ScanIDCardResultActivity;
import com.mx.moxietest.result.CardResultPresenter;
import com.mx.moxietest.result.data.LivenessResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG="MainActivity";

    private CardResultPresenter mCardPresenter;
    private static final int KEY_PERMISSION_REQUEST_ID_CARD_FRONT = 100;
    /**
     * 扫描身份证正面请求码
     */
    private static final int MX_SCAN_ID_CARD_FRONT_REQUEST = 100;

    /**
     * 扫描身份证反面请求码
     */
    private static final int MX_SCAN_ID_CARD_BACK_REQUEST = 101;
    /**
     * 扫描身份证正反面请求码
     */
    private static final int MX_SCAN_ID_CARD_BOTH_REQUEST = 102;
    private static final int KEY_PERMISSION_REQUEST_ID_CARD_BACK = 101;
    private static final int KEY_PERMISSION_REQUEST_ID_CARD_BOTH = 102;
    /**
     * 活体检测请求码
     */
    private static final int KEY_TO_DETECT_REQUEST_CODE = 104;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView btn_liveness = findViewById(R.id.btn_liveness);
        TextView doubleSide = findViewById(R.id.btn_double_side);
        btn_liveness.setOnClickListener(this);
        doubleSide.setOnClickListener(this);
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
            case R.id.btn_double_side:
                // 连续扫描身份证正反面
                toScanIdCardBoth();
                break;
            case R.id.btn_liveness:
                // 活体检测
                startLiveness();
                break;
            default:
                break;
        }
    }
    /**
     * 跳转扫描身份证界面，扫描正面和反面
     */
    private void toScanIdCardBoth() {
        // 扫描双面时，从正面开始扫描
        Intent scanBothIdCardIntent = getScanBothIdCardIntent(IDCardRecognizer.Mode.FRONT, "请将身份证正面放入扫描框内");

        startActivityForResult(scanBothIdCardIntent, MX_SCAN_ID_CARD_BOTH_REQUEST);
    }

    /**
     * 跳转IDCard正反面扫描界面
     *
     * @param mode IDCard扫描类型
     */
    private Intent getScanBothIdCardIntent(IDCardRecognizer.Mode mode, String scanText) {
        Intent scanIntent = new Intent(this, IDCardActivity.class);

        // KEY_PRODUCTION_MODE，true为生产环境，false为开发环境，默认是false
        // 注意上线产品设置为生产环境
        scanIntent.putExtra(IDCardActivity.KEY_PRODUCTION_MODE,false);

        //设置返回按钮图片
        scanIntent.putExtra(IDCardActivity.EXTRA_BACK_DRAWABLE_ID, R.mipmap.icon_scan_back);

        //设置身份证扫描类型
        scanIntent.putExtra(IDCardActivity.EXTRA_RECOGNIZE_MODE, mode);

        //设置是否连续扫描正反面
        scanIntent.putExtra(IDCardActivity.KEY_SCAN_BOTH_MODE,true);

        //设置身份证扫描文字
        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_TIPS, scanText);

        //设置标题
        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_TITLE, "请拍摄身份证");

        //设置是否开启扫描光标
        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_LINE_STATUS, true);

        //扫描取景框边界颜色
        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_GUIDE_COLOR, Color.parseColor("#78FFFFFF"));

        //设置扫描的超时时间
        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_TIME_OUT, 30);


        return scanIntent;
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
        intent.putExtra(LivenessActivity.EXTRA_MOTION_SEQUENCE, "BLINK");//BLINK MOUTH NOD YAW
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
            case KEY_PERMISSION_REQUEST_ID_CARD_BACK:
            case KEY_PERMISSION_REQUEST_ID_CARD_BOTH:
            case KEY_PERMISSION_REQUEST_ID_CARD_FRONT:

                // OCR检测的结果回调
                switch (resultCode) {
                    // OCR识别成功
                    case IDCardActivity.RESULT_CARD_INFO:
                        dealScanIDCardResult(requestCode, data);
                        Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",OCR识别成功");
                        break;
                    // OCR相机权限获取失败
                    case IDCardActivity.RESULT_CAMERA_NOT_AVAILABLE:
                        showToast(Constants.ERROR_CAMERA_REFUSE);
                        Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",OCR识别相机权限获取失败");
                        break;
                    //  扫描取消
                    case IDCardActivity.RESULT_CANCELED:
                        showToast(Constants.ERROR_SCAN_CANCEL);
                        Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",扫描被取消");
                        break;
                    //  算法SDK初始化失败
                    case IDCardActivity.RESULT_RECOGNIZER_INIT_FAILED:
                        showToast(Constants.ERROR_SDK_INITIALIZE);
                        Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",算法SDK初始化失败");
                        break;
                    // 扫描超时
                    case IDCardActivity.RESULT_RECOGNIZER_FAIL_SCAN_TIME_OUT:
                        showToast(Constants.ERROR_TIME_OUT);
                        Log.i(TAG, "onActivityResult" + "   resultCode:" + resultCode+",扫描超时");
                        break;
                    default:
                        break;
                }
                break;
        }
    }
    /**
     * @des    身份证扫描正反面成功执行方法
     * @auther JayGengi
     * @data 2019/1/29 9:36
     * @email JayGengi@163.com
     */
    private void dealScanIDCardResult(int requestCode, Intent data) {
        switch (requestCode) {
            case MX_SCAN_ID_CARD_FRONT_REQUEST:
                dealAutoScanIDCardFrontResult(data);
                break;
            case MX_SCAN_ID_CARD_BACK_REQUEST:
                dealScanIDCardBackResult(data);
                break;
            case MX_SCAN_ID_CARD_BOTH_REQUEST:
                dealScanIDCardBothResult(data);
                break;
        }
    }
    /**
     * 处理自动扫描身份证正面结果
     *
     * @param data
     */
    private void dealAutoScanIDCardFrontResult(final Intent data) {
        // 扫描单面通过IDCardActivity.EXTRA_SCAN_RESULT取出扫描结果
        MXOCRResult idCardResult = (MXOCRResult) data.getParcelableExtra(IDCardActivity.EXTRA_SCAN_RESULT);
        Intent intent = getToCardResultIntent(idCardResult, null, "扫描正面", ScanIDCardResultActivity.CARD_RESULT_TYPE_FRONT);
        startActivity(intent);
    }


    /**
     * 处理扫描身份证反面结果
     *
     * @param data
     */
    private void dealScanIDCardBackResult(final Intent data) {
        // 扫描单面通过IDCardActivity.EXTRA_SCAN_RESULT取出扫描结果
        MXOCRResult idCardResult = (MXOCRResult) data.getParcelableExtra(IDCardActivity.EXTRA_SCAN_RESULT);
        Intent intent = getToCardResultIntent(null, idCardResult, "扫描反面",
                ScanIDCardResultActivity.CARD_RESULT_TYPE_BACK);
        startActivity(intent);
    }

    /**
     * 处理扫描身份证正反面结果
     *
     * @param data
     */
    private void dealScanIDCardBothResult(final Intent data) {
        // 扫描双面通过IDCardActivity.KEY_FRONT_CARD_DATA取出正面扫描结果
        MXOCRResult idCardFrontResult = (MXOCRResult) data.getParcelableExtra(IDCardActivity.KEY_FRONT_CARD_DATA);
        // 扫描双面通过IDCardActivity.KEY_BACK_CARD_DATA取出正面扫描结果
        MXOCRResult idCardBackResult = (MXOCRResult) data.getParcelableExtra(IDCardActivity.KEY_BACK_CARD_DATA);
        Intent intent = getToCardResultIntent(idCardFrontResult, idCardBackResult, "连续扫描正反面",
                ScanIDCardResultActivity.CARD_RESULT_TYPE_BOTH);
        startActivity(intent);
    }

    private Intent getToCardResultIntent(MXOCRResult frontIDCard, MXOCRResult backIDCard,
                                         String resultTitle, int cardResultType
    ) {
        Intent intent = new Intent(this, ScanIDCardResultActivity.class);
        intent.putExtra(ScanIDCardResultActivity.KEY_CARD_RESULT_TITLE, resultTitle);
        intent.putExtra(ScanIDCardResultActivity.KEY_CARD_RESULT_TYPE, cardResultType);
        intent.putExtra(ScanIDCardResultActivity.KEY_CARD_FRONT_DATA, frontIDCard);
        intent.putExtra(ScanIDCardResultActivity.KEY_CARD_BACK_DATA, backIDCard);
        return intent;
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
