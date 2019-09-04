package com.ute.recall.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.ute.recall.model.channelinfor;
import com.ute.recall.model.channelConfig;


import java.util.ArrayList;
import java.util.List;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 1;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Note_Manager";


    // Tên bảng: Note.
    private static final String TABLE_NOTE = "user";

    private static final String COLUMN_NOTE_ID ="channelID";
    private static final String COLUMN_NOTE_TITLE ="writekey";
    private static final String COLUMN_NOTE_CONTENT = "readkey";
    private static final String COLUMN_NOTE_CONTENT1 = "channelName";


    private static final String TABLE_NOTE1 = "channel";

    private static final String COLUMN_NOTE_ID1 ="channelID";
    private static final String COLUMN_NOTE_TITLE1 ="tempe";
    private static final String COLUMN_NOTE_CONTENT2 = "humi";
    private static final String COLUMN_NOTE_CONTENT21 = "soil";
    private static final String COLUMN_NOTE_CONTENT22 = "warn";
    private static final String COLUMN_NOTE_CONTENT23 = "auto";
    private static final String COLUMN_NOTE_CONTENT24 = "pump";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Script tạo bảng.
        String script = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_NOTE_ID + " TEXT PRIMARY KEY,"
                + COLUMN_NOTE_TITLE + " TEXT,"
                + COLUMN_NOTE_CONTENT + " TEXT,"
                + COLUMN_NOTE_CONTENT1 + " TEXT"
                + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
        // Script tạo bảng.
        String script2 = "CREATE TABLE " + TABLE_NOTE1 + "("
                + COLUMN_NOTE_ID1 + " TEXT PRIMARY KEY,"
                + COLUMN_NOTE_TITLE1 + " TEXT,"
                + COLUMN_NOTE_CONTENT2 + " TEXT,"
                + COLUMN_NOTE_CONTENT21 + " TEXT,"
                + COLUMN_NOTE_CONTENT22 + " INTEGER,"
                + COLUMN_NOTE_CONTENT23 + " INTEGER,"
                + COLUMN_NOTE_CONTENT24 + " TEXT"
                + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script2);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);


        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng Note chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.



    public void addUser(channelinfor note) {

        try {

            List<channelinfor> noteList = getAllUsers();
            for(channelinfor ch : noteList)
            {
                if(note.getChannelID() == ch.getChannelID())
                    return;
            }

            try {
                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(COLUMN_NOTE_ID, note.getChannelID());
                values.put(COLUMN_NOTE_TITLE, note.getWritekey());
                values.put(COLUMN_NOTE_CONTENT, note.getReadkey());


                // Trèn một dòng dữ liệu vào bảng.
                db.insert(TABLE_NOTE, null, values);


                // Đóng kết nối database.
                db.close();
            } catch (Exception e) {
            }
        } catch (Exception ex) {
        }
    }






    public List<channelinfor> getAllUsers() {

        List<channelinfor> noteList = new ArrayList<channelinfor>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                channelinfor note = new channelinfor();
                note.setChannelID(Integer.parseInt(cursor.getString(0)));
                note.setWritekey(cursor.getString(1));
                note.setReadkey(cursor.getString(2));
                note.setChannelName(cursor.getString(3));
                // Thêm vào danh sách.
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        // return note list
        return noteList;
    }


    public int updateUser(channelinfor note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getWritekey());
        values.put(COLUMN_NOTE_CONTENT, note.getReadkey());
        values.put(COLUMN_NOTE_CONTENT1, note.getChannelName());
        // updating row
        return db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getChannelID())});
    }




    public void deleteNote(channelinfor note) {


        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                new String[] { String.valueOf(note.getChannelID()) });
        db.close();
    }

    public void deleteNoteByID(String idnote) {


        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                new String[] { String.valueOf(idnote) });
        db.close();

        try
        {
            deleteConfigByID(idnote);
        }catch (Exception e)
        {}
    }






//////////////////////////////////////////////



    public List<channelConfig> getAllConfigs() {

        List<channelConfig> noteList = new ArrayList<channelConfig>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE1;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                channelConfig note = new channelConfig();
                note.setChannelID(Integer.parseInt(cursor.getString(0)));
                note.setTemp(Double.valueOf(cursor.getString(1)));
                note.setHumi(Double.valueOf(cursor.getString(2)));
                note.setSoil(Double.valueOf(cursor.getString(3)));
                note.setWarn(Integer.parseInt(cursor.getString(4)));
                note.setAuto(Integer.parseInt(cursor.getString(5)));
                note.setPump(Double.valueOf(cursor.getString(6)));
                // Thêm vào danh sách.
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        // return note list
        return noteList;
    }


    public int updateConfig(channelConfig note) {

        List<channelConfig> list = getAllConfigs();
        for(channelConfig c : list)
            if(c.getChannelID() == note.getChannelID())
            {

                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(COLUMN_NOTE_TITLE1, note.getTemp());
                values.put(COLUMN_NOTE_CONTENT2, note.getHumi());
                values.put(COLUMN_NOTE_CONTENT21, note.getSoil());
                values.put(COLUMN_NOTE_CONTENT22, note.getWarn());
                values.put(COLUMN_NOTE_CONTENT23, note.getAuto());
                values.put(COLUMN_NOTE_CONTENT24, note.getPump());
                // updating row
                return db.update(TABLE_NOTE1, values, COLUMN_NOTE_ID1 + " = ?",
                        new String[]{String.valueOf(note.getChannelID())});
            }
            else
            {
                 addConfig(note);
                 return 1;
            }
return 1;

    }


    public channelConfig getConfig(int ChannelID) {
        List<channelConfig> channelConfigs = getAllConfigs();
        for(channelConfig c: channelConfigs)
        {
            if(c.getChannelID() == ChannelID)
            {
                return c;
            }
        }
        return  null;
    }


    public void addConfig(channelConfig note) {

        try {



            try {
                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(COLUMN_NOTE_ID1, note.getChannelID());
                values.put(COLUMN_NOTE_TITLE1, note.getTemp());
                values.put(COLUMN_NOTE_CONTENT2, note.getHumi());
                values.put(COLUMN_NOTE_CONTENT21, note.getSoil());
                values.put(COLUMN_NOTE_CONTENT22, note.getWarn());
                values.put(COLUMN_NOTE_CONTENT23, note.getAuto());
                values.put(COLUMN_NOTE_CONTENT24, note.getPump());
                // Trèn một dòng dữ liệu vào bảng.
                db.insert(TABLE_NOTE1, null, values);


                // Đóng kết nối database.
                db.close();
            } catch (Exception e) {
            }
        } catch (Exception ex) {
        }
    }




    public void deleteConfig(channelConfig note) {


        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE1, COLUMN_NOTE_ID1 + " = ?",
                new String[] { String.valueOf(note.getChannelID()) });
        db.close();
    }


    public void deleteConfigByID(String idnote) {


        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE1, COLUMN_NOTE_ID1 + " = ?",
                new String[] { String.valueOf(idnote) });
        db.close();
    }
}