package com.example.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyOpener extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Message.db";
    public static final String TABLE_NAME = "Message_table";
    public static final int DATABASE_VERSION = 1;
    public static final String COL_1 = "ID";
    public static final String COL_2 = "MESSAGE";
    public static final String COL_3 = "ISSENT";
    public static final String COL_4 = "ISRECEIVED";

    public MyOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID LONG PRIMARY KEY,MESSAGE TEXT,ISSENT INTEGER,ISRECEIVED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(Long id, String message,Integer issent,Integer isreceived) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,message);
        contentValues.put(COL_3,issent);
        contentValues.put(COL_4,isreceived);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public long deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
