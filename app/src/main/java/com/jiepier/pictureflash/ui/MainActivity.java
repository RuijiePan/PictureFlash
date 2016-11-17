package com.jiepier.pictureflash.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jiepier.pictureflash.R;
import com.jiepier.pictureflash.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public int initContentView() {
        return 0;
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
        return false;
    }
}
