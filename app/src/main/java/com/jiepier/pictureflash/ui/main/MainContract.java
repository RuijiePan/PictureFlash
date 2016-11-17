package com.jiepier.pictureflash.ui.main;

import com.jiepier.pictureflash.base.BasePresenter;
import com.jiepier.pictureflash.base.BaseView;

/**
 * Created by JiePier on 16/11/17.
 */

public class MainContract {

    interface View extends BaseView{

        void showLoading();

        void dismissLoading();
    }

    interface MainModel{

    }

    interface Presenter extends BasePresenter<View>{


    }
}
