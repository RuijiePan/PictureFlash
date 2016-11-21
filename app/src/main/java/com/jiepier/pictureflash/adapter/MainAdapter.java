package com.jiepier.pictureflash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiepier.pictureflash.R;
import com.jiepier.pictureflash.base.BaseViewHolder;
import com.jiepier.pictureflash.bean.AlumbBean;
import com.jiepier.pictureflash.bean.ImageBean;

import java.util.List;

/**
 * Created by JiePier on 16/11/20.
 */

public class MainAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<AlumbBean> mList;
    private Context mContext;
    private OnItemClickListener mListener;

    public MainAdapter(Context context){
        this.mContext = context;
    }

    public MainAdapter(Context context,List<AlumbBean> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main,parent,false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {

        String path = mList.get(position).getPath().split(",")[0];
        holder.setText(R.id.alumb_name,mList.get(position).getAlumb())
                .setImageByUrl(R.id.img_picture,path);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null)
                mListener.onItemClick(mList.get(position).getPath(),
                        mList.get(position).getAlumb());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null?0:mList.size();
    }

    public void setData(List<AlumbBean> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemClickListener{
        void onItemClick(String path,String alumb);
    }

}
