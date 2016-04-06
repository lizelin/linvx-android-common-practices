package com.haishang360.inventory.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.linvx.android.libs.common.MyApplication;

/**
 * Created by lizelin on 16/3/31.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static volatile DatabaseHelper helper;
    private static final  String DB_NAME = "stock.db";
    private SQLiteDatabase mWritableDb;

    public SQLiteDatabase _innerGetWritableDb(){
        if (mWritableDb ==null || !mWritableDb.isOpen()) {
            mWritableDb = this.getWritableDatabase();
        }
        return mWritableDb;
    }
    public static DatabaseHelper getInstance() {
        if (helper == null){
            synchronized (DatabaseHelper.class) {
                if (helper == null){
                    DatabaseHelper temp = new DatabaseHelper();
                    helper = temp;
                    helper._innerGetWritableDb();
                }
            }
        }

        return helper;
    }

    public static SQLiteDatabase getWritableDb() {
        return DatabaseHelper.getInstance()._innerGetWritableDb();
    }

    public static SQLiteDatabase getReadableDb() {
        return DatabaseHelper.getInstance().getReadableDatabase();
    }

    public DatabaseHelper() {
        super(MyApplication.getAppContext(), DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initTables(true, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void initTables(boolean reset, SQLiteDatabase db) {
        if (reset) {
            db.execSQL("drop table  if exists whs");
            db.execSQL("drop table  if exists skus");
            db.execSQL("drop table  if exists stocks");
            db.execSQL("drop table  if exists invs");
        }
        if (this.tabbleIsExist("whs", db))
            return;
        String sql = "create table whs (vc2guid varchar, vc2account varchar, vc2warehouseguid varchar, vc2warehousetype varchar, vc2warehousename varchar, primary key (vc2guid))";
        db.execSQL(sql);
        sql = "create index idx_whs on whs (vc2account)";
        db.execSQL(sql);
        sql = "create table skus (vc2mdse_code varchar, vc2mdse_name varchar, vc2mdse_sku varchar, primary key (vc2mdse_code))";
        db.execSQL(sql);
        sql = "create table stocks (vc2guid varchar, vc2account, vc2mdse_code, numstock_count smallint, primary key(vc2guid,vc2mdse_code))";
        db.execSQL(sql);
        sql = "create index idx_stock on stocks (vc2account)";
        db.execSQL(sql);
        sql = "create table invs (id INTEGER AUTO_INCREMENT, vc2guid varchar, vc2account, vc2mdse_code, numstock_count smallint, primary key(vc2guid,vc2mdse_code))";
        db.execSQL(sql);
        sql = "create index idx_inv on invs (vc2account)";
        db.execSQL(sql);
    }

    public boolean tabbleIsExist(String tableName, SQLiteDatabase db){
        boolean result = false;
        if(tableName == null){
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tableName.trim()+"' ";
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

}
