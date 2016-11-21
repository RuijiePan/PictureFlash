package com.jiepier.pictureflash.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jiepier.pictureflash.R;
import com.jiepier.pictureflash.adapter.MainAdapter;
import com.jiepier.pictureflash.base.App;
import com.jiepier.pictureflash.base.BaseActivity;
import com.jiepier.pictureflash.base.BaseToolBar;
import com.jiepier.pictureflash.bean.AlumbBean;
import com.jiepier.pictureflash.ui.detail.ImagePagerActivity;
import com.jiepier.pictureflash.ui.picture.PictureActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainContract.View{

    @BindView(R.id.baseToolBar)
    BaseToolBar baseToolBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    public static final String EXTRA_ALUMB = "alumb";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    private MainAdapter mAdapter;
    private MainPresenter mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initUiAndListener() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        baseToolBar.setCenterText("Picture Flash");
        baseToolBar.setRightIcon(getResources().getDrawable(R.drawable.ic_add_white_36dp));

        baseToolBar.setRightViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.sContext, PictureActivity.class);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String path,String alumb) {
                Intent intent = new Intent(App.sContext, ImagePagerActivity.class);
                intent.putExtra(EXTRA_IMAGE_URLS,path.split(","));
                intent.putExtra(EXTRA_ALUMB,alumb);
                startActivity(intent);
            }
        });

        mPresenter = new MainPresenter(this);
        mPresenter.attachView(this);
        mPresenter.getData();
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
    public void showLoading() {
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
    }

    @Override
    public void dismissLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void setData(List<AlumbBean> list) {
        mAdapter.setData(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getData();
    }
}
