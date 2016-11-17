package com.jiepier.pictureflash.ui.picture;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.jiepier.pictureflash.bean.ImageFloder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by JiePier on 16/11/17.
 */

public class PicturePresenter implements PictureContract.Presenter {

    private PictureContract.View view;
    private Context mContext;
    private int totalCount = 0;
    private int mPicsSize;
    private File mImgDir;
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    private List<String> mImgs;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    public PicturePresenter(Context context){
        this.mContext = context;
    }

    @Override
    public void getImages() {
        new ScanAsyncTask().execute();
    }

    @Override
    public void attachView(@NonNull PictureContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    class ScanAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)){
                view.showToast("暂无外部存储");
                return;
            }
            view.showLoding();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String firstImage = null;

            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = mContext.getContentResolver();

            // 只查询jpeg和png的图片
            Cursor mCursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[] { "image/jpeg", "image/png" },
                    MediaStore.Images.Media.DATE_ADDED+" DESC");

            while (mCursor.moveToNext()){
                //获取图片路径
                String path = mCursor.getString(mCursor.getColumnIndex(
                        MediaStore.Images.Media.DATA
                ));

                if (firstImage == null){
                    firstImage = path;
                }

                File parentFile = new File(path).getParentFile();
                if (parentFile == null)
                    continue;

                String dirPath = parentFile.getAbsolutePath();
                ImageFloder imageFloder = null;

                // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                if (mDirPaths.contains(dirPath))
                {
                    continue;
                }else {
                    mDirPaths.add(dirPath);
                    imageFloder = new ImageFloder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                }

                int picSize = parentFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String filename) {
                        if (filename.endsWith(".jpg")
                                || filename.endsWith(".png")
                                || filename.endsWith(".jpeg"))
                            return true;
                        return false;
                    }
                }).length;
                totalCount += picSize;

                imageFloder.setCount(totalCount);
                mImageFloders.add(imageFloder);

                if (picSize > mPicsSize){
                    mPicsSize = picSize;
                    mImgDir = parentFile;
                }

            }
            mCursor.close();
            mDirPaths = null;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view.dismissLoading();
            data2View();
        }
    }

    private void data2View() {
        if (mImgDir == null){
            view.showToast("一张图片都没扫描到。。");
            return;
        }

        mImgs = Arrays.asList(mImgDir.list());
        view.setPictureData(mImgs,mImgDir.getAbsolutePath());
        view.setPicTotalCount(totalCount+"");
    }
}
