package com.genius.baselib.frame.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.genius.baselib.frame.base.BaseDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongseok on 2016-10-19.
 */

public class DB_Offerwall_History extends BaseDB {

    public static final String TABLE_PART = "db_offerwall_history";
    protected final static int DB_APP_VERSION = 11;


    public DB_Offerwall_History(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, TABLE_PART, factory, DB_APP_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + COLUMN_ERROR.column_create());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) return;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PART);
        onCreate(db);
    }

    private static class COLUMN_ERROR {
        private static final String ADID = "adid";
        private static final String TITLE = "title";
        private static final String ACTION = "action";
        private static final String PARTID = "partid";
        private static String column_create() {
            return TABLE_PART + "(" +
                    ADID + " TEXT PRIMARY KEY," +
                    TITLE+ " TEXT ," +
                    PARTID+ " TEXT ," +
                    ACTION + " TEXT" +
                    ");";
        }
    }


    /**
     * Add or remove
     *
     * @return
     */
    public void pushPartInfo(HistoryItem historyItem ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ERROR.ADID, historyItem.getAdid());
        contentValues.put(COLUMN_ERROR.TITLE, historyItem.getTitle());
        contentValues.put(COLUMN_ERROR.ACTION, historyItem.getAction_str());
        contentValues.put(COLUMN_ERROR.PARTID, historyItem.getPartId());
        if (mDB_Writer != null) {
            HistoryItem info = getInfo(historyItem.getAdid());
            if(info==null){
                mDB_Writer.insert(TABLE_PART, null, contentValues);
            }else{
                mDB_Writer.update(TABLE_PART,contentValues, COLUMN_ERROR.ADID+"=?",new String[]{historyItem.getAdid()});
            }
        }
    }

    /**
     * Add or remove
     *
     * @return
     */
    public void remodePartInfo(HistoryItem partInfo ) {
        if (mDB_Writer != null) {
            mDB_Writer.delete(TABLE_PART, COLUMN_ERROR.ADID+"=?",new String[]{partInfo.getAdid()});
        }
    }
    public boolean clear(){
       return deleteAll(TABLE_PART);
    }


    public HistoryItem getInfo(String campid){
        if (mDB_Writer == null){
            return null;
        }
        Cursor cursor = mDB_Writer.query(TABLE_PART, null, COLUMN_ERROR.ADID + "=?", new String[]{campid}, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String part_ids = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.ADID));
                String campaign_id = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.TITLE));
                String ad_type = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.ACTION));
                String partid = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.PARTID));
              HistoryItem historyItem = new HistoryItem();
                historyItem.setAdid(part_ids);
                historyItem.setTitle(campaign_id);
                historyItem.setPartId(partid);
                historyItem.setAction_str(ad_type);

                cursor.close();

                return historyItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return null;
    }

    public List<HistoryItem> getAllIndex(Context context) {

        List<HistoryItem> launchAppInfos = new ArrayList<>();


        if (isExistRows(TABLE_PART) > 0) {

            Cursor cursor = fetchAllvalues(TABLE_PART);
            if (cursor != null && cursor.moveToFirst()) {

                try {
                    do {
                        String part_ids = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.ADID));
                        String campaign_id = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.TITLE));
                        String ad_type = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.ACTION));
                        String partid = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.PARTID));
                        HistoryItem historyItem = new HistoryItem();
                        historyItem.setAdid(part_ids);
                        historyItem.setTitle(campaign_id);
                        historyItem.setAction_str(ad_type);
                        historyItem.setPartId(partid);
                        launchAppInfos.add(historyItem);
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

    public List<String> getAllIndex_Camp(Context context) {

        List<String> launchAppInfos = new ArrayList<>();


        if (isExistRows(TABLE_PART) > 0) {

            Cursor cursor = fetchAllvalues(TABLE_PART);
            if (cursor != null && cursor.moveToFirst()) {

                try {
                    do {
                        String campaign_id = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.ADID));
                        launchAppInfos.add(campaign_id);
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
