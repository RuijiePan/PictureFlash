package com.jiepier.pictureflash.base;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiepier.pictureflash.util.image.ImageLoader;
import com.jiepier.pictureflash.util.image.Type;

import java.io.ByteArrayOutputStream;

/**
 * Created by panruijiesx on 2016/11/14.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;
    private View convertView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
        convertView = itemView;
    }

    public View getConvertView(){
        return convertView;
    }

    public <T extends View> T getView(int viewId){
        View view = views.get(viewId);
        if (view == null){
            view = convertView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T)view;
    }

    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setText(int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap){
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int resourceId){
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public BaseViewHolder setImageByUrl(int viewId,String url){
        View view = getView(viewId);
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO)
                .loadImage(url,(ImageView) getView(viewId));
        return this;
    }

    public BaseViewHolder setColorFilter(int viewId,int color){
        View view = getView(viewId);
        ((ImageView)view).setColorFilter(Color.parseColor("#77000000"));
        return this;
    }

    public BaseViewHolder setColorFilter(int viewId, ColorFilter colorFilter){
        View view = getView(viewId);
        ((ImageView)view).setColorFilter(colorFilter);
        return this;
    }

    public BaseViewHolder setVisibility(int viewId, boolean visibility) {
        View view = getView(viewId);
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        }else {
            view.setVisibility(View.INVISIBLE);
        }
        return this;
    }
}
