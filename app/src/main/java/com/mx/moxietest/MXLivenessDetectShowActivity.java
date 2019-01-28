package com.mx.moxietest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.moxie.liveness.MXLivenessSDK;
import com.moxie.liveness.util.MXReturnResult;
import com.mx.moxietest.adapter.MXDetectResultShowAdapter;
import com.mx.moxietest.adapter.MXIAdapterOnClickListener;
import com.mx.moxietest.result.data.MXResultShowBean;

import java.util.ArrayList;
import java.util.List;

public class MXLivenessDetectShowActivity  extends Activity implements MXIAdapterOnClickListener {
    public static final String TAG = "MXDetectShowActivity";

    public static final String KEY_DETECT_IMAGE_RESULT = "key_detect_image_result";

    private ImageView mIvBack;
    private ImageView mIvDetail;
    private VideoView mVVideoResult;
    private ImageView mIvVideoPlay;
    private RecyclerView mRvBtn;

    private MXReturnResult mImageResult;

    private MXDetectResultShowAdapter mResultShowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_liveness_detect_show_main);

        getIntentData();
        initView();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mImageResult = (MXReturnResult) intent.getSerializableExtra(KEY_DETECT_IMAGE_RESULT);
        }
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.id_iv_back);
        mIvDetail = (ImageView) findViewById(R.id.id_iv_image_detail);
        mVVideoResult = (VideoView) findViewById(R.id.id_vv_video_result);
        mIvVideoPlay = (ImageView) findViewById(R.id.id_iv_play_btn);
        mRvBtn = (RecyclerView) findViewById(R.id.id_rv_btn);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

        mIvVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvVideoPlay.setVisibility(View.GONE);
                playVideo();
            }
        });

        mVVideoResult.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mIvVideoPlay.setVisibility(View.VISIBLE);
            }
        });

        mVVideoResult.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {

                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            // video started
                            Log.i(TAG, "onPrepared");
                            mIvDetail.setVisibility(View.GONE);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        refreshData();
    }

    private void refreshData() {
        if (mImageResult != null) {
            MXLivenessSDK.MXLivenessImageResult[] imageResults = mImageResult.getImageResults();

            List<MXResultShowBean> imageBtn = new ArrayList<>();
            if (imageResults != null) {
                for (MXLivenessSDK.MXLivenessImageResult result : imageResults) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(result.image, 0, result.image.length);
                    MXResultShowBean resultShow = new MXResultShowBean();
                    resultShow.setThumBitmap(bitmap);
                    imageBtn.add(resultShow);
                }
            }

            String videoResultPath = mImageResult.getVideoResultPath();
            if (!TextUtils.isEmpty(videoResultPath)) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_media_play);
                Bitmap thumBitmap = ThumbnailUtils.createVideoThumbnail(videoResultPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                MXResultShowBean resultShow = new MXResultShowBean();
                resultShow.setThumBitmap(thumBitmap);
                resultShow.setPlayBitmap(bitmap);
                imageBtn.add(resultShow);
            }

            mResultShowAdapter = new MXDetectResultShowAdapter(this, imageBtn);
            mResultShowAdapter.setOnClickListener(this);

            mRvBtn.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mRvBtn.setAdapter(mResultShowAdapter);

            initVideoViewData(mImageResult.getVideoResultPath());

            if (imageResults != null && imageResults.length > 0) {
                onSwitchDetailImage(0);
            }
        }
    }

    private void initVideoViewData(String videoPath) {
        if (!TextUtils.isEmpty(videoPath)) {
            mVVideoResult.setVideoPath(videoPath);
        }
    }

    private void onSwitchDetailImage(int position) {
        showDetailView();
        mVVideoResult.pause();
        mVVideoResult.resume();
        if (mImageResult != null) {
            MXResultShowBean itemData = mResultShowAdapter.getItemDataByPosition(position);
            if (itemData != null) {
                mIvDetail.setImageBitmap(itemData.getThumBitmap());
                if (itemData.getPlayBitmap() != null) {
                    mIvVideoPlay.setVisibility(View.VISIBLE);
                    mIvVideoPlay.setImageBitmap(itemData.getPlayBitmap());
                } else {
                    mIvVideoPlay.setVisibility(View.GONE);
                }
            }
        }
    }

    private void showDetailView() {
        mIvDetail.setVisibility(View.VISIBLE);
    }

    private void playVideo() {
        mVVideoResult.setVisibility(View.VISIBLE);
        mVVideoResult.start();
        mVVideoResult.requestFocus();
    }

    @Override
    public void onClick(int position) {
        onSwitchDetailImage(position);
    }

    private void onBack() {
        finish();
    }
}
