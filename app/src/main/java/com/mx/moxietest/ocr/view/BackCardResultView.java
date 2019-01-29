package com.mx.moxietest.ocr.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.moxietest.R;
import com.mx.moxietest.result.data.IDBackInfo;


public class BackCardResultView extends LinearLayout {
    /**
     * 裁剪图
     */
    private ImageView mIvCrop;
    /**
     * 取景框图片
     */
    private ImageView mIvCameraAperture;

    /**
     * IDCard内容
     */
    private LinearLayout mCardContent;

    private View backCardView;

    public BackCardResultView(Context context) {
        super(context);
        init(context);
    }

    public BackCardResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BackCardResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context != null) {
            if (backCardView==null){
                backCardView = inflate(context, R.layout.layout_view_front_card_result, null);
                addView(backCardView);
                initView();
            }
        }
    }

    private void initView() {
        mIvCrop = (ImageView) findViewById(R.id.id_iv_front_card_crop);
        mIvCameraAperture = (ImageView) findViewById(R.id.id_iv_front_card_camera_aperture);
        ImageView ivFace = (ImageView) findViewById(R.id.id_iv_front_card_face_image);
        mCardContent = (LinearLayout) findViewById(R.id.id_llyt_card_content);

        ivFace.setVisibility(View.GONE);
    }

    public void refreshData(IDBackInfo cardViewData, boolean isCanEdit, Bitmap cameraApertureBitmap, Bitmap cropBitmap) {
        if (mIvCameraAperture != null) {
            mIvCameraAperture.setImageBitmap(cameraApertureBitmap);
        }
        if (mIvCrop != null) {
            mIvCrop.setImageBitmap(cropBitmap);
        }
        if (cardViewData != null ){
            initCardContent(cardViewData.getData(), isCanEdit);
        }

    }

    private void initCardContent(IDBackInfo.BackInfo backInfo, boolean isCanEdit) {
        mCardContent.removeAllViews();
        if (mCardContent != null && backInfo != null) {
            //签发机关
            View nameCardItemView = getCardItemContentView("签发机关", backInfo.getPolice(), isCanEdit);
            mCardContent.addView(nameCardItemView);
            //有效期限
            View sexCardItemView = getCardItemContentView("有效期限", backInfo.getExpiry(), isCanEdit);
            mCardContent.addView(sexCardItemView);
        }

    }

    private View getCardItemContentView(String title, String content, boolean isCanEdit) {
        View itemView = inflate(getContext(), R.layout.layout_view_card_result_item, null);
        TextView tvTitle = (TextView) itemView.findViewById(R.id.id_tv_title);
        TextView tvContent = (TextView) itemView.findViewById(R.id.id_tv_content);
        tvContent.setEnabled(isCanEdit);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        if (tvContent != null) {
            tvContent.setText(content);
        }
        return itemView;
    }
}
