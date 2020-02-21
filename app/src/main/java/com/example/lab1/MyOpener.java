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

    public static final String ACTIVITY_NAME="MyOpener";
    private static final int DATABASE_VERSION=1;
    protected static final String DATABASE_NAME="ChatData";
    protected static final String TABLE_NAME="Message";
    protected static final String COL_ID="id";
    protected static final String COL_MESSAGE="message";
    protected static final String COL_TYPE="type";

    public MyOpener( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql= "CREATE TABLE "+TABLE_NAME+" ("+COL_ID+" LONG PRIMARY KEY ,"+COL_MESSAGE+" TEXT,"+COL_TYPE+" TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS Message";
        db.execSQL(sql);
        onCreate(db);
    }

    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM Message";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    public void create(MessageModel message) {



        ContentValues values = new ContentValues();
        values.put("id",(long)count());
        values.put("message", message.getMessage());
        values.put("type", message.getType());

        SQLiteDatabase db = this.getWritableDatabase();
        db.getPageSize();
        boolean createSuccessful = db.insert("Message", null, values) > 0;
        db.close();

    }

    public List<MessageModel> read() {

        List<MessageModel> recordsList = new ArrayList<MessageModel>();

        String sql = "SELECT * FROM Message ORDER BY id ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                long id = Long.parseLong(cursor.getString(cursor.getColumnIndex("id")));
                String messageEntry = cursor.getString(cursor.getColumnIndex("message"));
                String type = cursor.getString(cursor.getColumnIndex("type"));

                MessageModel message = new MessageModel(id, messageEntry, type);


                recordsList.add(message);

            } while (cursor.moveToNext());
        }
        printCursor(cursor);
        cursor.close();
        db.close();

        return recordsList;
    }

    public void printCursor(Cursor c){

        Log.i(ACTIVITY_NAME,  "Database Version: "+DATABASE_VERSION);

        Log.i(ACTIVITY_NAME,  "column count: "+c.getColumnCount());

        for(int i = 0; i < c.getColumnCount() ; i++){
            Log.i(ACTIVITY_NAME,  "column "+i+": " +c.getColumnName(i));
        }



        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                Log.i(ACTIVITY_NAME,"Id: " + c.getString(c.getColumnIndex("id")));
                Log.i(ACTIVITY_NAME,"Message: " + c.getString(c.getColumnIndex("message")));
                Log.i(ACTIVITY_NAME,"Type: " + c.getString(c.getColumnIndex("type")));
                c.moveToNext();
            }
        }


        Log.i(ACTIVITY_NAME,  String.valueOf(c.getColumnCount()));


    }

    public void delete(MessageModel message){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="DELETE FROM "+TABLE_NAME+ " WHERE id="+message.getId();
        db.execSQL(sql,null);
        db.close();
        //String whereClause ="id=?";
        //String[] whereArgs = new String[]{String.valueOf(message.getId())};
        //db.delete(TABLE_NAME,whereClause,whereArgs);
    }
}
