package com.jiepier.pictureflash.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiepier.pictureflash.sql.PictureService;

/**
 * Created by JiePier on 16/11/20.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private PictureService mSqlService;
    private Context mContext;

    public MainPresenter(Context context){
        this.mContext = context;
        mSqlService = new PictureService(mContext,1);
    }

    @Override
    public void getData() {
        mView.setData(mSqlService.getAlumbInfo());
    }

    @Override
    public void attachView(@NonNull MainContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
