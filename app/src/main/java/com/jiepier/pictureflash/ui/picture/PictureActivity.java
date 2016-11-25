package com.jiepier.pictureflash.ui.picture;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiepier.pictureflash.R;
import com.jiepier.pictureflash.adapter.PictureAdapter;
import com.jiepier.pictureflash.base.App;
import com.jiepier.pictureflash.base.BaseActivity;
import com.jiepier.pictureflash.base.BaseToolBar;
import com.jiepier.pictureflash.bean.ImageFloder;
import com.jiepier.pictureflash.sql.PictureService;
import com.jiepier.pictureflash.util.DividerGridItemDecoration;
import com.jiepier.pictureflash.widget.MyAlertDialog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/11/17.
 */

public class PictureActivity extends BaseActivity implements PictureContract.View, ListImageDirPopupWindow.OnImageDirSelected {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.id_choose_dir)
    TextView idChooseDir;
    @BindView(R.id.id_total_count)
    TextView idTotalCount;
    @BindView(R.id.id_bottom_ly)
    RelativeLayout idBottomLy;
    @BindView(R.id.baseToolBar)
    BaseToolBar baseToolBar;

    private PicturePresenter mPresenter;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;
    private File mImgDir;
    private List<String> mImgs;
    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private List<ImageFloder> mImageFloders = new ArrayList<>();//扫描拿到所有的图片文件夹
    private PictureAdapter mAdapter;

    @Override
    public int initContentView() {
        return R.layout.activity_picture;
    }

    @Override
    public void initUiAndListener() {

        mContext = this;
        baseToolBar.setCenterText("图片选择");
        mPresenter = new PicturePresenter(this);
        mPresenter.attachView(this);
        mPresenter.getImages();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mAdapter = new PictureAdapter(this);
        recyclerView.setAdapter(mAdapter);

        LayoutInflater inflater = getLayoutInflater();
        View  dialog = inflater.inflate(R.layout.custom_dialog,null,false);
        final EditText editText = (EditText) dialog.findViewById(R.id.edit_album);

        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("创建专辑")
                .setView(dialog)
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                  @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       if (!editText.getText().toString().equals("")){
                           mDialog.cancel();
                           mPresenter.createAlumb(editText.getText().toString(),mAdapter.getmSelectedImage());
                           editText.setText("");
                           mAdapter.clearSelected();
                           Toast.makeText(mContext,"创建成功",Toast.LENGTH_SHORT).show();
                       }else {
                           mDialog.show();
                           Toast.makeText(mContext,"专辑名不能为空",Toast.LENGTH_SHORT).show();
                      }
                  }
                 }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editText.setText("");
            }
        });
        mDialog = mBuilder.create();

        idBottomLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(idBottomLy, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });

        baseToolBar.setLeftIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_36dp));
        baseToolBar.setRightIcon(getResources().getDrawable(R.drawable.ic_check_white_36dp));
        baseToolBar.setLeftViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        baseToolBar.setRightViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getmSelectedImage().size()!=0) {
                    mDialog.show();
                }else {
                    Toast.makeText(mContext,"创建专辑请选择至少一张图片",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPicTotalCount(String count) {
        idTotalCount.setText(count);
        //Toast.makeText(this, count, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPictureData(List<String> list, String dirPath) {
        mAdapter.setData(list, new File(dirPath));
    }

    @Override
    public void setImageFloders(List<ImageFloder> list) {

        //Log.w("haha12321",list.get(0).getFirstImagePath());
        mImageFloders = list;
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (App.sScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    public void selected(ImageFloder floder) {
        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter.setData(mImgs, mImgDir);
        idTotalCount.setText(mImgs.size() + "张");
        idChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }

}
