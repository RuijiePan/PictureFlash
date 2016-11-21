package com.jiepier.pictureflash.ui.detail;

import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jiepier.pictureflash.R;
import com.jiepier.pictureflash.util.image.ImageLoader;

/**
 * Created by JiePier on 16/11/21.
 */
public class ImageDetailFragment extends Fragment {

    private String ImageUrl;
    private ImageView img;
    private Animation animation;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageUrl = getArguments()!=null?getArguments().getString("url"):null;
        getArguments().remove("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_detail_fragment,container,false);
        img = (ImageView) v.findViewById(R.id.image);
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO)
                .loadImage(ImageUrl,img);
        animation = AnimationUtils.loadAnimation(getContext(),R.anim.scale_in);

        img.setClickable(true);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        animation = AnimationUtils.loadAnimation(getContext(),R.anim.scale_in);
        img.startAnimation(animation);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser&&animation!=null){
            img.startAnimation(animation);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
