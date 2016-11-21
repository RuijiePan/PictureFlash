package com.jiepier.pictureflash.ui.picture;

import com.jiepier.pictureflash.base.BasePresenter;
import com.jiepier.pictureflash.base.BaseView;
import com.jiepier.pictureflash.bean.ImageFloder;

import java.util.List;

/**
 * Created by JiePier on 16/11/17.
 */

public class PictureContract {

    interface View extends BaseView{
        void showLoding();

        void dismissLoading();

        void showToast(String string);

        void setPicTotalCount(String count);

        void setPictureData(List<String> list,String dirPath);

        void setImageFloders(List<ImageFloder> list);
    }

    interface Model{

    }

    interface Presenter extends BasePresenter<View>{
        void getImages();

        void createAlumb(String alumbName,List<String> path);
    }
}
