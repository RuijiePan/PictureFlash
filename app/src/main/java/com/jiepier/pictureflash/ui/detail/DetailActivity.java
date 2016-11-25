package com.jiepier.pictureflash.ui.detail;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.jiepier.pictureflash.R;
import com.jiepier.pictureflash.base.BaseActivity;
import com.jiepier.pictureflash.base.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/11/21.
 */

public class DetailActivity extends BaseActivity {

    @BindView(R.id.baseToolBar)
    BaseToolBar baseToolBar;
    @BindView(R.id.imagepager)
    ViewPager imagepager;
    @BindView(R.id.tv_index)
    TextView tvIndex;

    @Override
    public int initContentView() {
        return R.layout.image_detail_pager;
    }

    @Override
    public void initUiAndListener() {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

}
