package com.haishang360.inventory.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizelin on 16/3/30.
 */
public class Wh {
    @SerializedName("VC2GUID")
    public String vc2guid;
    @SerializedName("VC2ACCOUNT")
    public String vc2account;
    @SerializedName("VC2WAREHOUSETYPE")
    public String vc2warehousetype;
    @SerializedName("VC2WAREHOUSENAME")
    public String vc2warehousename;
    @SerializedName("VC2WAREHOUSEGUID")
    public String vc2warehouseguid;

    public static Wh getWhFromDb(SQLiteDatabase db, String guid) {
        Wh wh = null;

        String sql = "select vc2guid, vc2account, vc2warehousetype, vc2warehouseguid, vc2warehousename from" +
                "   whs where vc2guid=? ";
        Cursor c = db.rawQuery(sql, new String[]{guid});
        if (c.moveToNext()) {
            wh = new Wh();
            wh.vc2guid = c.getString(0);
            wh.vc2account = c.getString(1);
            wh.vc2warehousetype = c.getString(2);
            wh.vc2warehouseguid = c.getString(3);
            wh.vc2warehousename = c.getString(4);
        }
        c.close();
        return wh;
    }

    public static void deleteWhsByAccount(SQLiteDatabase db, String account){
        db.execSQL("delete from whs where vc2account=?", new String[]{account});
    }

    public void saveToDb(SQLiteDatabase db) {
        saveToDb(db, false);
    }

    public void saveToDb(SQLiteDatabase db, boolean check) {

        String sql = "delete from whs where vc2guid=?";
        if (check) db.execSQL(sql, new String[]{this.vc2guid});

        sql = "insert into whs(vc2guid, vc2account, vc2warehousetype, vc2warehouseguid, vc2warehousename)" +
                "   values(?,?,?,?,?)";
        db.execSQL(sql, new String[]{this.vc2guid, this.vc2account, this.vc2warehousetype, this.vc2warehouseguid, this.vc2warehousename});
    }

    public static List<Wh> getWhsByAccount(SQLiteDatabase db, String account){
        List<Wh> whs = new ArrayList<Wh>();

        Cursor c = db.rawQuery("select vc2guid, vc2account, vc2warehousetype, vc2warehouseguid, vc2warehousename" +
                " from  whs where vc2account=?", new String[]{account});
        while (c.moveToNext()) {
            Wh wh = new Wh();
            wh.vc2guid = c.getString(0);
            wh.vc2account = c.getString(1);
            wh.vc2warehousetype = c.getString(2);
            wh.vc2warehouseguid = c.getString(3);
            wh.vc2warehousename = c.getString(4);
            whs.add(wh);
        }
        c.close();
        return whs;
    }

}
