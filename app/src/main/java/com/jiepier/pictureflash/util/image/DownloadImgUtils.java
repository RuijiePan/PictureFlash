package com.jiepier.pictureflash.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JiePier on 16/11/17.
 */

public class DownloadImgUtils {

    /**
     * 根据url下载图片在指定的文件
     *
     * @param urlStr
     * @param file
     * @return
     */
    public static boolean downloadImgByUrl(String urlStr, File file){

        FileOutputStream fos = null;
        InputStream is = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            is = conn.getInputStream();
            fos = new FileOutputStream(file);

            byte[] buf = new byte[512];
            int len = 0;
            while ((len = is.read(buf))!= -1){
                fos.write(buf,0,len);
            }
            fos.flush();
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }finally {

            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static Bitmap downloadImgByUrl(String urlStr, ImageView imageView){
        FileOutputStream fos = null;
        InputStream is = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(conn.getInputStream());
            is.mark(is.available());

            ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeStream(is,null,opts);
            opts.inSampleSize = ImageSizeUtil.caculateInSampleSize(opts,
                    imageSize.width,imageSize.height);
            opts.inJustDecodeBounds = false;
            is.reset();

            bitmap = BitmapFactory.decodeStream(is,null,opts);
            conn.disconnect();
            return bitmap;
        }catch (Exception e){

        }finally
        {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException e)
            {
            }

            try
            {
                if (fos != null)
                    fos.close();
            } catch (IOException e)
            {
            }
        }

        return null;
    }
}
