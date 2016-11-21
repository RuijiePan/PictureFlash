package com.jiepier.pictureflash.ui.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by JiePier on 16/11/21.
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    public String[] fileList;

    public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
        super(fm);
        this.fileList = fileList;
    }

    @Override
    public Fragment getItem(int position) {
        String url = fileList[position];
        return ImageDetailFragment.newInstance(url);
    }

    @Override
    public int getCount() {
        return fileList == null ? 0 : fileList.length;
    }
}
