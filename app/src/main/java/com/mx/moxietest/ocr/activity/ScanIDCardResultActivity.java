package com.mx.moxietest.ocr.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.moxie.ocr.ocr.idcard.MXOCRResult;
import com.mx.moxietest.R;
import com.mx.moxietest.ocr.view.BackCardResultView;
import com.mx.moxietest.ocr.view.FrontCardResultView;
import com.mx.moxietest.result.CardResultPresenter;
import com.mx.moxietest.result.data.IDBackInfo;
import com.mx.moxietest.result.data.IDFrontInfo;

public class ScanIDCardResultActivity extends Activity implements RadioGroup.OnCheckedChangeListener{
    public static final String TAG = "MXScanIDCardResultActivity";

    public static final String KEY_CARD_FRONT_DATA = "key_card_front_data";
    public static final String KEY_CARD_BACK_DATA = "key_card_back_data";
    public static final String KEY_CARD_RESULT_TYPE = "key_card_result_type";
    public static final String KEY_CARD_RESULT_TITLE = "key_card_result_title";
    public static final String KEY_CAMERA_APERTURE_FRONT_IMAGE = "key_camera_aperture_front_image";
    public static final String KEY_CAMERA_APERTURE_BACK_IMAGE = "key_camera_aperture_back_image";
    public static final String KEY_CROP_FRONT_IMAGE = "key_crop_front_image";
    public static final String KEY_CROP_BACK_IMAGE = "key_crop_back_image";

    public static final int CARD_RESULT_TYPE_FRONT = 1;
    public static final int CARD_RESULT_TYPE_BACK = 2;
    public static final int CARD_RESULT_TYPE_BOTH = 3;

    public static final int CARD_TYPE_FRONT = 1;
    public static final int CARD_TYPE_BACK = 2;


    private TextView mTvTitle;

    private ImageView mIvBack;

    private RadioGroup mRGSwitch;

    private RadioButton mRBFront;

    private RadioButton mRBBack;

    private LinearLayout mLoading;

    /**
     * 扫描身份证前面界面
     */
    private FrontCardResultView mVFrontCardResult;

    /**
     * 扫描身份证反面界面
     */
    private BackCardResultView mVBackCardResult;


    /**
     * 身份证正面扫描数据
     */
    private IDFrontInfo mFrontCardViewData;

    /**
     * 身份证反面扫描数据
     */
    private IDBackInfo mBackCardViewData;

    /**
     * 身份证扫描类型
     */
    private int mCardResultType;

    /**
     * 标题
     */
    private String mTitle;

    /**
     * 正面取景框图像
     */
    private Bitmap mCameraApertureFrontBitmap;

    /**
     * 反面取景框图像
     */
    private Bitmap mCameraApertureBackBitmap;

    /**
     * 正面裁剪图
     */
    private Bitmap mCropFrontBitmap;

    /**
     * 人脸裁剪图
     */
    private Bitmap mCropFaceBitmap;

    /**
     * 反面裁剪图
     */
    private Bitmap mCropBackBitmap;

    private CardResultPresenter mCardPresenter;
    private MXOCRResult mFrontIDCard;
    private MXOCRResult mBackIDCard;

    private byte[] mCameraApertureFront;
    private byte[] mCameraApertureBack;
    private byte[] mCropFrontByte;
    private byte[] mCropBackByte;
    private byte[] mCropFaceByte;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scan_id_card_result_main);

        initPresenter();
        getIntentData();
        initView();
        initCardResultType(null, null);
        initData();
    }

    private void initPresenter() {
        mCardPresenter = new CardResultPresenter();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mCardResultType = intent.getIntExtra(KEY_CARD_RESULT_TYPE, CARD_RESULT_TYPE_FRONT);
            mTitle = intent.getStringExtra(KEY_CARD_RESULT_TITLE);

            mFrontIDCard = (MXOCRResult) intent.getParcelableExtra(KEY_CARD_FRONT_DATA);
            mBackIDCard = (MXOCRResult) intent.getParcelableExtra(KEY_CARD_BACK_DATA);

            if (mFrontIDCard!=null){
                mCameraApertureFront = mFrontIDCard.getCameraApertureImage();
                if (mCameraApertureFront != null) {
                    mCameraApertureFrontBitmap = BitmapFactory.decodeByteArray(mCameraApertureFront, 0, mCameraApertureFront.length);
                }

                mCropFrontByte = mFrontIDCard.getCropImage();
                if (mCropFrontByte != null) {
                    mCropFrontBitmap = BitmapFactory.decodeByteArray(mCropFrontByte, 0, mCropFrontByte.length);
                }

                mCropFaceByte = mFrontIDCard.getCropFaceImage();
                if (mCropFaceByte != null){
                    mCropFaceBitmap = BitmapFactory.decodeByteArray(mCropFaceByte, 0, mCropFaceByte.length);
                }
            }
            if (mBackIDCard!=null){
                mCameraApertureBack = mBackIDCard.getCameraApertureImage();
                if (mCameraApertureBack != null) {
                    mCameraApertureBackBitmap = BitmapFactory.decodeByteArray(mCameraApertureBack, 0, mCameraApertureBack.length);
                }


                mCropBackByte = mBackIDCard.getCropImage();
                if (mCropBackByte != null) {
                    mCropBackBitmap = BitmapFactory.decodeByteArray(mCropBackByte, 0, mCropBackByte.length);
                }
            }



        }
    }



    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.id_tv_title);
        mIvBack = (ImageView) findViewById(R.id.id_iv_back);
        mRGSwitch = (RadioGroup) findViewById(R.id.id_rg_scan_result_switch);
        mRBFront = (RadioButton) findViewById(R.id.id_rb_scan_front_result);
        mRBBack = (RadioButton) findViewById(R.id.id_rb_scan_back_result);
        mVFrontCardResult = (FrontCardResultView) findViewById(R.id.id_lcrv_front);
        mVBackCardResult = (BackCardResultView) findViewById(R.id.id_lcrv_back);
        TextView tvComplete = (TextView) findViewById(R.id.id_tv_complete);
        mLoading = findViewById(R.id.id_ll_loading);
        mRGSwitch.setOnCheckedChangeListener(this);
        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTvTitle.setText(mTitle);
    }

    private void initCardResultType(IDFrontInfo frontCardViewData, IDBackInfo backCardViewData) {
        switch (mCardResultType) {
            case CARD_RESULT_TYPE_FRONT:
                refreshFrontView(frontCardViewData);
                break;
            case CARD_RESULT_TYPE_BACK:
                refreshBackView(backCardViewData);
                break;
            case CARD_RESULT_TYPE_BOTH:
                refreshBothView(frontCardViewData, backCardViewData);
                break;
            default:
                break;
        }
    }

    private void refreshFrontView(IDFrontInfo frontCardViewData) {
        mRGSwitch.setVisibility(View.GONE);
        mRBFront.setChecked(true);
        mVFrontCardResult.refreshData(frontCardViewData, true, mCameraApertureFrontBitmap, mCropFrontBitmap,mCropFaceBitmap);
    }

    private void refreshBackView(IDBackInfo backCardViewData) {
        mRGSwitch.setVisibility(View.GONE);
        mRBBack.setChecked(true);
        mVBackCardResult.refreshData(backCardViewData, true, mCameraApertureBackBitmap, mCropBackBitmap);
    }


    private void refreshBothView(IDFrontInfo frontCardViewData, IDBackInfo backCardViewData) {
        mRGSwitch.setVisibility(View.VISIBLE);
        mRBFront.setChecked(true);
        mVFrontCardResult.refreshData(frontCardViewData, true, mCameraApertureFrontBitmap, mCropFrontBitmap,mCropFaceBitmap);
        mVBackCardResult.refreshData(backCardViewData, true, mCameraApertureBackBitmap, mCropBackBitmap);
    }

    private void initData() {
        if (mFrontIDCard != null) {
            mLoading.setVisibility(View.VISIBLE);
            getOCRFrontInfo();
        }
        if  (mBackIDCard != null){
            mLoading.setVisibility(View.VISIBLE);
            getOCRBackInfo();
        }

    }

    private void refreshData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoading.setVisibility(View.GONE);
                initCardResultType(mFrontCardViewData, mBackCardViewData);
            }
        });
    }

    private void dissmissLoading(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.id_rb_scan_front_result:
                refreshCardResultView(true);
                break;
            case R.id.id_rb_scan_back_result:
                refreshCardResultView(false);
                break;
        }
    }

    private void refreshCardResultView(boolean showFrontView) {
        mVFrontCardResult.setVisibility(showFrontView ? View.VISIBLE : View.GONE);
        mVBackCardResult.setVisibility(!showFrontView ? View.VISIBLE : View.GONE);
    }


    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ScanIDCardResultActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * 识别身份证正面信息
     *
     */
    public void getOCRFrontInfo(){
        mCardPresenter.getOCRInfo(mCropFrontByte, new CardResultPresenter.IOCRInfoCallback() {
            @Override
            public void callback(String result) {
                IDFrontInfo idFrontInfo = JSON.parseObject(result,IDFrontInfo.class);
                if (idFrontInfo.getData() != null && idFrontInfo.getData().getStatus().equals("1")){
                    mFrontCardViewData = idFrontInfo;
                    refreshData();
                }else {
                    showToast("获取身份证信息失败，result:"+result);
                    dissmissLoading();
                }
            }

            @Override
            public void fail(String error) {
                showToast("获取身份证信息失败，"+error);
                dissmissLoading();
            }
        },true);
    }


    /**
     * 识别身份证背面信息
     *
     */
    public void getOCRBackInfo(){
        mCardPresenter.getOCRInfo(mCropBackByte, new CardResultPresenter.IOCRInfoCallback() {
            @Override
            public void callback(String result) {
                IDBackInfo idBackInfo = JSON.parseObject(result,IDBackInfo.class);
                if (idBackInfo.getData() != null&& idBackInfo.getData().getStatus().equals("1")){
                    mBackCardViewData = idBackInfo;
                    refreshData();
                }else {
                    showToast("获取身份证信息失败，result:"+result);
                    dissmissLoading();
                }
            }

            @Override
            public void fail(String error) {
                showToast("获取身份证信息失败，"+error);
                dissmissLoading();
            }
        },false);
    }
}
