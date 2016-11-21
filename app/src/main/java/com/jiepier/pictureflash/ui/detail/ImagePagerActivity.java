package com.jiepier.pictureflash.ui.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.jiepier.pictureflash.R;
import com.jiepier.pictureflash.base.BaseActivity;
import com.jiepier.pictureflash.base.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/11/21.
 */

public class ImagePagerActivity extends BaseActivity {

    public static final String EXTRA_ALUMB = "alumb";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    private static final int autoChangeTime= 3000;
    private String[] urls;
    private String alumb;
    private ImagePagerAdapter mAdapter;
    private Runnable runnable;
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
        alumb = getIntent().getStringExtra(EXTRA_ALUMB);
        urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        imagepager.setAdapter(mAdapter);

        baseToolBar.setCenterText(alumb);
        baseToolBar.setLeftIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_36dp));

        baseToolBar.setLeftViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imagepager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvIndex.setText(position+1+"/"+urls.length);
                viewHandler.removeCallbacks(runnable);
                viewHandler.postDelayed(runnable, autoChangeTime);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imagepager.setOffscreenPageLimit(0);

        runnable = new Runnable() {
            @Override
            public void run() {
                int next = imagepager.getCurrentItem() + 1;
                if(next >= mAdapter.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);

        tvIndex.setText("1/"+urls.length);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int index = msg.what;

            imagepager.setCurrentItem(index);
        }
    };

}
