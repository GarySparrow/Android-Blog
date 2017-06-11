package com.gary.blog.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gary.blog.Data.Post;
import com.gary.blog.DataBase.DataBaseSchema.PostTable;

/**
 * Created by hasee on 2016/12/23.
 */

public class DataBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "blogBase.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PostTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
                PostTable.Cols.id + ", " +
                PostTable.Cols.subject + ", " +
                PostTable.Cols.timeStamp + ", " +
                PostTable.Cols.postUrl + ", " +
                PostTable.Cols.authorUrl + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
