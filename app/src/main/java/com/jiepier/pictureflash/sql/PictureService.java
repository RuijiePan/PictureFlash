package com.jiepier.pictureflash.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jiepier.pictureflash.bean.AlumbBean;
import com.jiepier.pictureflash.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiePier on 16/11/20.
 */

public class PictureService {

    DBOpenHelper mDbHelper;
    SQLiteDatabase mDb;

    public PictureService(Context context, int version) {
        mDbHelper = new DBOpenHelper(context, "picture.db", null, version);
    }

    public void insert(String path,String alumb) {
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL("insert into picture(path,alumb) values(?,?)",
                new Object[]{path, alumb});
//        mDb.setTransactionSuccessful();
//        mDb.endTransaction();
    }

    public void delete(String alumb) {
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL("delete from picture where alumb=?", new String[]{alumb});
    }

    public List<AlumbBean> getAlumbInfo() {

        mDb = mDbHelper.getWritableDatabase();
        List<AlumbBean> alumbList = new ArrayList<>();

        Cursor cursor = mDb.rawQuery("select * from picture",null);
        boolean first = true;
        String myPath = "";
        String myAlumb = "";

        if (cursor.moveToFirst()){
            do {

                String path = cursor.getString(cursor.getColumnIndex("path"));
                String alumb = cursor.getString(cursor.getColumnIndex("alumb"));
                ImageBean bean = new ImageBean();
                bean.setPath(path)
                        .setAlumb(alumb);

                    AlumbBean alumbBean = new AlumbBean(myPath,myAlumb);
                    if (alumbBean.getAlumb().equals(alumb)){
                        myPath = myPath + "," +path;
                    }else {
                        if (first){
                            first = false;
                        }else {
                            alumbList.add(alumbBean);
                        }
                        myPath = path;
                        myAlumb = alumb;

                       /* for (int i=0;i<alumbList.size();i++)
                            Log.w("haha111",alumbList.get(i).getAlumb()+"!!1"+
                                    alumbList.get(i).getPath());*/
                    }
                }while (cursor.moveToNext());
                alumbList.add(new AlumbBean(myPath,myAlumb));
            }
            cursor.close();

        /*for (int i=0;i<alumbList.size();i++)
            Log.w("haha111",alumbList.get(i).getAlumb()+"!!1"+
                    alumbList.get(i).getPath());*/
        return alumbList;
    }
}
