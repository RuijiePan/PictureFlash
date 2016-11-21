package com.jiepier.pictureflash.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiepier.pictureflash.R;
import com.jiepier.pictureflash.base.BaseViewHolder;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by JiePier on 16/11/18.
 */

public class PictureAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<String> mImgs;
    private File mImgDir;
    private Context mContext;
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public List<String> mSelectedImage = new LinkedList<String>();

    public PictureAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_picture,parent,false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {

        final String picPath = mImgDir+File.separator+mImgs.get(position);
        holder.setImageResource(R.id.id_item_image,R.mipmap.pictures_no)
                .setImageResource(R.id.id_item_select,R.mipmap.picture_unselected)
                .setImageByUrl(R.id.id_item_image,picPath);

        holder.getView(R.id.id_item_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mSelectedImage.contains(picPath)){
                    mSelectedImage.remove(picPath);
                    holder.setImageResource(R.id.id_item_select,R.mipmap.picture_unselected)
                            .setColorFilter(R.id.id_item_image,null);

                }else {
                    mSelectedImage.add(picPath);
                    holder.setImageResource(R.id.id_item_select,R.mipmap.pictures_selected)
                            .setColorFilter(R.id.id_item_image, Color.parseColor("#77000000"));
                }
            }
        });

        if (mSelectedImage.contains(picPath)){
            holder.setImageResource(R.id.id_item_select,R.mipmap.pictures_selected)
                    .setColorFilter(R.id.id_item_image,Color.parseColor("#77000000"));
        }

    }

    @Override
    public int getItemCount() {
        return mImgs == null ? 0 : mImgs.size();
    }

    public void setData(List<String> imgs,File imgDir){
        this.mImgs = imgs;
        this.mImgDir = imgDir;
        notifyDataSetChanged();
    }

    public List<String> getmSelectedImage(){
        return mSelectedImage;
    }

    public void clearSelected(){
        mSelectedImage.clear();
        notifyDataSetChanged();
    }
}
