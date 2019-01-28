package com.mx.moxietest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mx.moxietest.R;
import com.mx.moxietest.result.data.MXResultShowBean;

import java.util.List;


public class MXDetectResultShowAdapter extends RecyclerView.Adapter<MXDetectResultShowAdapter.ResultShowViewHolder> {
    private List<MXResultShowBean> mData;

    private LayoutInflater mLayoutInflater;

    private MXIAdapterOnClickListener mOnClickListener;

    public MXDetectResultShowAdapter(Context context, List<MXResultShowBean> data) {
        this.mData = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public MXResultShowBean getItemDataByPosition(int position){
        MXResultShowBean resultShowBean = null;
        if (mData != null){
            resultShowBean = mData.get(position);
        }
        return resultShowBean;
    }

    @Override
    public ResultShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_result_show_item, null);
        return new ResultShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultShowViewHolder holder, int position) {
        MXResultShowBean result = mData.get(position);

        holder.mIvBtn.setImageBitmap(result.getThumBitmap());
        holder.mIvCenter.setImageBitmap(result.getPlayBitmap());
        holder.mIvBtn.setTag(position);
        holder.mIvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    int position = (int) v.getTag();
                    mOnClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setOnClickListener(MXIAdapterOnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    class ResultShowViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvBtn;
        private ImageView mIvCenter;

        public ResultShowViewHolder(View itemView) {
            super(itemView);
            mIvBtn = (ImageView) itemView.findViewById(R.id.id_iv_btn);
            mIvCenter = (ImageView) itemView.findViewById(R.id.id_iv_center);
        }
    }
}
