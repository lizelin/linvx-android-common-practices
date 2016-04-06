package com.haishang360.inventory.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lizelin on 16/3/30.
 */
public class Stock {
    @SerializedName("VC2GUID")
    public String vc2guid;
    @SerializedName("VC2ACCOUNT")
    public String vc2account;
    @SerializedName("VC2MDSE_CODE")
    public String vc2mdse_code;
    @SerializedName("NUMSTOCK_COUNT")
    public int numstock_count;

    public static Stock getStockFromDb(SQLiteDatabase db, String guid, String mdse_code) {
        Stock stock = null;

        String sql = "select vc2guid, vc2account, vc2mdse_code, numstock_count from" +
                "   stocks where vc2guid=? and vc2mdse_code=?";
        Cursor c = db.rawQuery(sql, new String[]{guid, mdse_code});
        if (c.moveToNext()) {
            stock = new Stock();
            stock.vc2guid = c.getString(0);
            stock.vc2account = c.getString(1);
            stock.vc2mdse_code = c.getString(2);
            stock.numstock_count = c.getInt(3);
        }
        c.close();
        return stock;
    }

    public static void deleteStocksByAccount(SQLiteDatabase db, String account){
        db.execSQL("delete from stocks where vc2account=?", new String[]{account});
    }

    public void saveToDb(SQLiteDatabase db) {
        saveToDb(db, false);
    }
    public void saveToDb(SQLiteDatabase db, boolean check) {

        String sql = "delete from stocks where vc2guid=? and vc2mdse_code=?";
        if ( check )
            db.execSQL(sql, new Object[]{this.vc2guid, this.numstock_count});

        sql = "insert into stocks(vc2guid, vc2account, vc2mdse_code, numstock_count)" +
                "   values(?,?,?,?)";
        db.execSQL(sql, new Object[]{this.vc2guid, this.vc2account, this.vc2mdse_code, this.numstock_count});
    }

    public static int getStockCountByGuid(SQLiteDatabase db, String guid) {
        int ret = 0;
        String sql = "select count(*) from stocks where vc2guid=? ";
        Cursor c = db.rawQuery(sql, new String[]{guid});
        if (c.moveToNext()) {
            ret = c.getInt(0);
        }
        return ret;
    }
}
