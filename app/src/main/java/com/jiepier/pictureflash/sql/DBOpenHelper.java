package com.jiepier.pictureflash.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JiePier on 16/11/20.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_SQL = "create table  picture( id " +
            "integer primary key autoincrement,path varchar not null, " +
            "alumb varchar not null)";
    //final static String ADD_SQL = "alter table  student  add phone varchar null" ;

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
