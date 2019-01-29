package com.mx.moxietest.ocr.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.moxietest.R;
import com.mx.moxietest.result.data.IDFrontInfo;


public class FrontCardResultView extends LinearLayout {
    /**
     * 裁剪图
     */
    private ImageView mIvCrop;
    /**
     * 取景框图片
     */
    private ImageView mIvCameraAperture;

    /**
     * 人脸图片
     */
    private ImageView mIvFace;

    /**
     * IDCard内容
     */
    private LinearLayout mCardContent;

    private View frontCardView;

    public FrontCardResultView(Context context) {
        super(context);
        init(context);
    }

    public FrontCardResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FrontCardResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context != null) {
            if (frontCardView == null){
                frontCardView = inflate(context, R.layout.layout_view_front_card_result, null);
                addView(frontCardView);
                initView();
            }
        }
    }

    private void initView() {
        mIvCrop = (ImageView) findViewById(R.id.id_iv_front_card_crop);
        mIvCameraAperture = (ImageView) findViewById(R.id.id_iv_front_card_camera_aperture);
        mIvFace = (ImageView) findViewById(R.id.id_iv_front_card_face_image);
        mCardContent = (LinearLayout) findViewById(R.id.id_llyt_card_content);
    }

    public void refreshData(IDFrontInfo cardViewData, boolean isCanEdit, Bitmap cameraApertureBitmap, Bitmap cropBitmap, Bitmap faceBitmap) {
        if (mIvCameraAperture != null) {
            mIvCameraAperture.setImageBitmap(cameraApertureBitmap);
        }
        if (mIvCrop != null) {
            mIvCrop.setImageBitmap(cropBitmap);
        }

        if (mIvFace != null && faceBitmap != null) {
            mIvFace.setImageBitmap(faceBitmap);

        }
        if (cardViewData != null) {
            initCardContent(cardViewData.getData(), isCanEdit);
        }

    }

    private void initCardContent(IDFrontInfo.FrontInfo frontInfo, boolean isCanEidt) {
        mCardContent.removeAllViews();
        if (mCardContent != null) {
            //姓名
            View nameCardItemView = getCardItemContentView("姓名", frontInfo.getName(), isCanEidt);
            mCardContent.addView(nameCardItemView);
            //性别
            View sexCardItemView = getCardItemContentView("性别", frontInfo.getSex(), isCanEidt);
            mCardContent.addView(sexCardItemView);
            //民族
            View nationCardItemView = getCardItemContentView("民族", frontInfo.getNation(), isCanEidt);
            mCardContent.addView(nationCardItemView);
            //年龄
            View birthCardItemView = getCardItemContentView("出生", frontInfo.getBirth(), isCanEidt);
            mCardContent.addView(birthCardItemView);
            //住址
            View addressCardItemView = getCardItemContentView("住址", frontInfo.getAddress(), isCanEidt);
            mCardContent.addView(addressCardItemView);
            //号码
            View numberCardItemView = getCardItemContentView("身份证号码", frontInfo.getId_card(), isCanEidt);
            mCardContent.addView(numberCardItemView);
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
