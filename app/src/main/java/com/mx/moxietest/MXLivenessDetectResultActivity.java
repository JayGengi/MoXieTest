package com.mx.moxietest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxie.liveness.MXLivenessSDK;
import com.moxie.liveness.util.LivenessUtils;
import com.moxie.liveness.util.MXProtoBufUtil;
import com.moxie.liveness.util.MXReturnResult;

import java.io.File;

public class MXLivenessDetectResultActivity extends Activity {
    private static final String TAG = "MXLivenesResultActivity";

    public static final String KEY_DETECT_RESULT = "key_detect_result";


    private ImageView mIvResultCenter;
    private RelativeLayout mRlytSmall;
    private ImageView mIvResultSmall;
    private ImageView mIvBack;
    private TextView mTvResultCount;
    private TextView mTvRestartDetect;

    private MXReturnResult mReturnResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_liveness_detect_result_main);

        getIntentData();
        initView();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mReturnResult = (MXReturnResult) intent.getSerializableExtra(KEY_DETECT_RESULT);
        }
    }

    private void initView() {
        mIvResultCenter = (ImageView) findViewById(R.id.id_iv_result_center);
        mRlytSmall = (RelativeLayout) findViewById(R.id.id_rlyt_small);
        mIvResultSmall = (ImageView) findViewById(R.id.id_iv_result_small);
        mTvResultCount = (TextView) findViewById(R.id.id_tv_main_result_count);
        mIvBack = (ImageView) findViewById(R.id.id_iv_back);


        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBack();
            }
        });

        mRlytSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mReturnResult != null) {
                    toShowImageResult();
                }
            }
        });

        refreshData();
    }


    private void refreshData() {
        if (mReturnResult != null) {
            MXLivenessSDK.MXLivenessImageResult[] imageResultArr = mReturnResult.getImageResults();
            if (imageResultArr != null) {
                int size = imageResultArr.length;
                mTvResultCount.setText(size + "p");
                if (size > 0) {
                    MXLivenessSDK.MXLivenessImageResult imageResult = imageResultArr[0];
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageResult.image, 0, imageResult.image.length);
                    mIvResultCenter.setImageBitmap(imageBitmap);
                    mIvResultSmall.setVisibility(View.VISIBLE);
                    mIvResultSmall.setImageBitmap(imageBitmap);
                }else {
                    mIvResultSmall.setVisibility(View.GONE);
                }
            }
            // protoBuf文件
            byte[] protoBuf = MXProtoBufUtil.getProtoBuf();
            String saveFilePath = Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + File.separator
                    + "liveness" + File.separator;
            LivenessUtils.saveFile(protoBuf, saveFilePath, "proto_buf_file");
        }
    }

    private void onClickBack() {
        finish();
    }

    private void toShowImageResult() {
        Intent intent = new Intent(this, MXLivenessDetectShowActivity.class);
        intent.putExtra(MXLivenessDetectShowActivity.KEY_DETECT_IMAGE_RESULT, mReturnResult);
        startActivity(intent);
    }


}
