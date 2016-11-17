package com.jiepier.pictureflash.ui.picture;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiepier.pictureflash.R;
import com.jiepier.pictureflash.base.BaseActivity;
import com.jiepier.pictureflash.bean.ImageFloder;
import com.jiepier.pictureflash.util.image.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/11/17.
 */

public class PictureActivity extends BaseActivity implements PictureContract.View{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.id_choose_dir)
    TextView idChooseDir;
    @BindView(R.id.id_total_count)
    TextView idTotalCount;
    @BindView(R.id.id_bottom_ly)
    RelativeLayout idBottomLy;

    private PicturePresenter mPresenter;
    private ProgressDialog mProgressDialog;
    private int mPicsSize;//存储文件夹中的图片数量
    private File mImgDir;
    private List<String> mImgs;//所有的图片
    private List<ImageFloder> mImageFloders = new ArrayList<>();//扫描拿到所有的图片文件夹
    int totalCount = 0;
    private int mScreenHeight;

    @Override
    public int initContentView() {
        return R.layout.activity_picture;
    }

    @Override
    public void initUiAndListener() {
        mPresenter = new PicturePresenter(this);
        mPresenter.attachView(this);
        mPresenter.getImages();
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @Override
    public void showLoding() {
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
    }

    @Override
    public void dismissLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showToast(String string) {
        Toast.makeText(this,string,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPicTotalCount(String count) {
        idTotalCount.setText(count);
        Toast.makeText(this,count,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPictureData(List<String> list, String dirPath) {

    }

    @Override
    public void getImageFloders(List<ImageFloder> list) {

    }  

    @Override
    public void getImgs(List<String> list) {

    }
}
