package com.haishang360.inventory.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lizelin on 16/3/30.
 */
public class Sku {
    @SerializedName("VC2MDSE_CODE")
    public String vc2mdse_code;
    @SerializedName("VC2MDSE_NAME")
    public String vc2mdse_name;
    @SerializedName("VC2MDSE_SKU")
    public String vc2mdse_sku;

    public static Sku getSkuFromDb(SQLiteDatabase db, String mdse_code ) {
        Sku sku = null;
        String sql = "select vc2mdse_code, vc2mdse_name, vc2mdse_sku from" +
                "   skus where vc2mdse_code=? ";
        Cursor c = db.rawQuery(sql, new String[]{mdse_code});
        if (c.moveToNext()) {
            sku = new Sku();
            sku.vc2mdse_code = c.getString(0);
            sku.vc2mdse_name = c.getString(1);
            sku.vc2mdse_sku = c.getString(2);

        }
        c.close();
        return sku;
    }

    public static void deleteAllSkus(SQLiteDatabase db){
        db.execSQL("delete from skus");
    }

    public void saveToDb(SQLiteDatabase db) {
        saveToDb(db, false);
    }
    public void saveToDb(SQLiteDatabase db, boolean check) {
        String sql = "delete from skus where vc2mdse_code=?";

        if (check)
            db.execSQL(sql, new String[]{this.vc2mdse_code});

        sql = "insert into skus(vc2mdse_code, vc2mdse_name, vc2mdse_sku)" +
                "   values(?,?,?)";
        db.execSQL(sql, new String[]{this.vc2mdse_code, this.vc2mdse_name, this.vc2mdse_sku});
    }

}
