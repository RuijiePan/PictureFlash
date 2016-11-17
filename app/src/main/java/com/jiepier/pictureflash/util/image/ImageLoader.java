package com.jiepier.pictureflash.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by JiePier on 16/11/17.
 */

public class ImageLoader {

    private static ImageLoader mInstance;

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEAFULT_THREAD_COUNT = 1;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;

    private boolean isDiskCacheEnable = true;

    private static final String TAG = "ImageLoader";

    private ImageLoader(int threadCount, Type type)
    {
        init(threadCount, type);
    }

    private void init(int threadCount, Type type) {
        initBackThread();

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;

        mLruCache = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };

        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        mType = type;
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    //初始化后台轮询线程
    private void initBackThread() {

        mPoolThread = new Thread(){
            @Override
            public void run() {

                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        // 线程池去取出一个任务进行执行
                        mThreadPool.execute(getTask());
                        try
                        {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e)
                        {
                        }
                    }
                };
                // 释放一个信号量
                mSemaphoreThreadPool.release();
                Looper.loop();
            }
        };
    }

    public static ImageLoader getInstance(){
        if (mInstance == null){
            synchronized (ImageLoader.class){
                mInstance = new ImageLoader(DEAFULT_THREAD_COUNT,Type.LIFO);
            }
        }
        return mInstance;
    }

    public static ImageLoader getInstance(int threadCount,Type type){
        if (mInstance == null)
        {
            synchronized (ImageLoader.class)
            {
                if (mInstance == null)
                {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    public Runnable getTask(){
        if (mType == Type.FIFO){
            return mTaskQueue.removeFirst();
        }else if (mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
    }

    /**
     * 根据path为imageview设置图片
     *
     * @param path
     * @param imageView
     */
    public void loadImage(final String path,final ImageView imageView,
                          final boolean isFromNet){
        imageView.setTag(path);
        if (mUIHandler == null)
        {
            mUIHandler = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    // 获取得到图片，为imageview回调设置图片
                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageview = holder.imageView;
                    String path = holder.path;
                    // 将path与getTag存储路径进行比较
                    if (imageview.getTag().toString().equals(path))
                    {
                        imageview.setImageBitmap(bm);
                    }
                };
            };
        }

        // 根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);

        if (bm != null){
            refreshBitmap(path,imageView,bm);
        }else {
            addTask(buildTask(path,imageView,isFromNet));
        }
    }

    private void addTask(Runnable runnable) {

        mTaskQueue.add(runnable);

        try {
            if (mPoolThreadHandler == null)
                mSemaphorePoolThreadHandler.acquire();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        mPoolThreadHandler.sendEmptyMessage(0x110);
    }


    /**
     * 根据传入的参数，新建一个任务
     *
     * @param path
     * @param imageView
     * @param isFromNet
     * @return
     */
    private Runnable buildTask(final String path, final ImageView imageView,
                               final boolean isFromNet)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                Bitmap bm = null;
                if (isFromNet)
                {
                    File file = getDiskCacheDir(imageView.getContext(),
                            EncryptUtil.md5(path));
                    if (file.exists())// 如果在缓存文件中发现
                    {
                        Log.e(TAG, "find image :" + path + " in disk cache .");
                        bm = loadImageFromLocal(file.getAbsolutePath(),
                                imageView);
                    } else
                    {
                        if (isDiskCacheEnable)// 检测是否开启硬盘缓存
                        {
                            boolean downloadState = DownloadImgUtils
                                    .downloadImgByUrl(path, file);
                            if (downloadState)// 如果下载成功
                            {
                                Log.e(TAG,
                                        "download image :" + path
                                                + " to disk cache . path is "
                                                + file.getAbsolutePath());
                                bm = loadImageFromLocal(file.getAbsolutePath(),
                                        imageView);
                            }
                        } else
                        // 直接从网络加载
                        {
                            Log.e(TAG, "load image :" + path + " to memory.");
                            bm = DownloadImgUtils.downloadImgByUrl(path,
                                    imageView);
                        }
                    }
                } else
                {
                    bm = loadImageFromLocal(path, imageView);
                }
                // 3、把图片加入到缓存
                addBitmapToLruCache(path, bm);
                refreshBitmap(path, imageView, bm);
                mSemaphoreThreadPool.release();
            }


        };
    }

    /**
     * 将图片加入LruCache
     *
     * @param path
     * @param bm
     */
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null)
        {
            if (bm != null)
                mLruCache.put(path, bm);
        }
    }

    private Bitmap loadImageFromLocal(String path, ImageView imageView) {

        Bitmap bm;
        // 加载图片
        // 图片的压缩
        // 1、获得图片需要显示的大小
        ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
        // 2、压缩图片
        bm = decodeSampleBitmapFromPath(path,imageSize.width,imageSize.height);
        return bm;
    }

    private Bitmap decodeSampleBitmapFromPath(String path, int width, int height) {

        // 获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        // 使用获得到的InSampleSize再次解析图片
        options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,width,height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return bitmap;
    }

    private void refreshBitmap(String path, ImageView imageView, Bitmap bm) {
        Message message = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.imageView = imageView;
        holder.bitmap = bm;
        holder.path = path;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    public File getDiskCache(Context context,String uniqueName){

        String cachePath;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获得缓存图片的地址
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName)
    {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()))
        {
            cachePath = context.getExternalCacheDir().getPath();
        } else
        {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
