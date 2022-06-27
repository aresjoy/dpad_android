package com.genius.baselib.frame.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.genius.baselib.frame.base.BaseDB;

/**
 * Created by hongseok on 2016-10-19.
 */

public class DB_Error extends BaseDB {

    public static final String TABLE_Error = "db_error";
    protected final static int DB_APP_VERSION = 2;


    public DB_Error(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, TABLE_Error, factory, DB_APP_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + COLUMN_ERROR.column_create());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) return;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Error);
        onCreate(db);
    }

    private static class COLUMN_ERROR {
        private static final String error = "cate";

        private static String column_create() {
            return TABLE_Error + "(" +
                    error + " TEXT PRIMARY KEY" +
                    ");";
        }
    }


    /**
     * Add or remove
     *
     * @return
     */
    public void pushApp(String error) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ERROR.error, error);
        if (mDB_Writer != null) {
               mDB_Writer.insert(TABLE_Error, null, contentValues);
        }
    }


    public boolean clear(){
       return deleteAll(TABLE_Error);
    }

    public List<String> getAllIndex(Context context) {

        List<String> launchAppInfos = new ArrayList<>();


        if (isExistRows(TABLE_Error) > 0) {

            Cursor cursor = fetchAllvalues(TABLE_Error);
            if (cursor != null && cursor.moveToFirst()) {

                try {
                    do {
                        String error = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.error));

                        launchAppInfos.add(error);
                    } while (cursor.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cursor.close();
                }


            }

        }

        return launchAppInfos;
    }

}
