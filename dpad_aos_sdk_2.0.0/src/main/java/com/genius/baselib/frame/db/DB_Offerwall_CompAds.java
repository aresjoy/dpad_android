package com.genius.baselib.frame.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dpad.offerwall.bean.PartInfo;
import com.genius.baselib.frame.base.BaseDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rocklee on 2017-04-26.
 */

public class DB_Offerwall_CompAds extends BaseDB {

    public static final String TABLE_PART = "db_offerwall_comp";
    protected final static int DB_APP_VERSION = 9;


    public DB_Offerwall_CompAds(Context context, SQLiteDatabase.CursorFactory factory) {
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
        private static final String PART_ID = "part_id";
        private static final String CAMPAIGN_ID = "campaign_id";
        private static final String LANDURL = "landurl";
        private static String column_create() {
            return TABLE_PART + "(" +
                    CAMPAIGN_ID + " TEXT PRIMARY KEY," +
                    PART_ID + " TEXT ," +
                    LANDURL + " TEXT" +
                    ");";
        }
    }

    /**
     * Add or remove
     *
     * @return
     */
    public void pushPartInfo(PartInfo partInfo ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ERROR.PART_ID, partInfo.getPart_id());
        contentValues.put(COLUMN_ERROR.CAMPAIGN_ID, partInfo.getCampaign_id());
        contentValues.put(COLUMN_ERROR.LANDURL, partInfo.getLandingUrl());
        if (mDB_Writer != null) {
            PartInfo info = getInfo(partInfo.getCampaign_id());
            if(info==null){
                mDB_Writer.insert(TABLE_PART, null, contentValues);
            }else{
                mDB_Writer.update(TABLE_PART,contentValues, COLUMN_ERROR.CAMPAIGN_ID+"=?",new String[]{partInfo.getCampaign_id()});
            }
        }
    }

    /**
     * Add or remove
     *
     * @return
     */
    public void remodePartInfo(PartInfo partInfo ) {
        if (mDB_Writer != null) {
            mDB_Writer.delete(TABLE_PART, COLUMN_ERROR.CAMPAIGN_ID+"=?",new String[]{partInfo.getCampaign_id()});
        }
    }
    public boolean clear(){
        return deleteAll(TABLE_PART);
    }


    public PartInfo getInfo(String campid){
        if (mDB_Writer == null){
            return null;
        }
        Cursor cursor = mDB_Writer.query(TABLE_PART, null, COLUMN_ERROR.CAMPAIGN_ID + "=?", new String[]{campid}, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String part_ids = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.PART_ID));
                String campaign_id = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.CAMPAIGN_ID));
                String ad_type = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.LANDURL));
                PartInfo partInfo = new PartInfo();
                partInfo.setPart_id(part_ids);
                partInfo.setLandingUrl(ad_type);
                partInfo.setCampaign_id(campaign_id);
                cursor.close();
                return partInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return null;
    }

    public List<PartInfo> getAllIndex(Context context) {

        List<PartInfo> launchAppInfos = new ArrayList<>();


        if (isExistRows(TABLE_PART) > 0) {

            Cursor cursor = fetchAllvalues(TABLE_PART);
            if (cursor != null && cursor.moveToFirst()) {

                try {
                    do {
                        String part_id = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.PART_ID));
                        String campaign_id = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.CAMPAIGN_ID));
                        String ad_type = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.LANDURL));
                        PartInfo partInfo = new PartInfo();
                        partInfo.setPart_id(part_id);
                        partInfo.setCampaign_id(campaign_id);
                        partInfo.setLandingUrl(ad_type);
                        launchAppInfos.add(partInfo);
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
                        String campaign_id = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR.CAMPAIGN_ID));
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
