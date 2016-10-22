package com.haishang360.inventory.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizelin on 16/3/30.
 */
public class Inv {
    public String vc2guid, vc2account, vc2mdse_code, vc2mdse_name, vc2expiredesc;
    public int numstock_count;

    public static Inv getStockFromDb(SQLiteDatabase db, String guid, String mdse_code) {
        Inv inv = null;

        String sql = "select t.vc2guid, t.vc2account, t.vc2mdse_code, t.numstock_count, s.vc2mdse_name, s.vc2mdse_sku, t.vc2expiredesc from" +
                "   invs t, skus s where t.vc2guid=? and t.vc2mdse_code=? " +
                "   and t.vc2mdse_code = s.vc2mdse_code";
        Cursor c = db.rawQuery(sql, new String[]{guid, mdse_code});
        if (c.moveToNext()) {
            inv = new Inv();
            inv.vc2guid = c.getString(0);
            inv.vc2account = c.getString(1);
            inv.vc2mdse_code = c.getString(2);
            inv.numstock_count = c.getInt(3);
            inv.vc2mdse_name = c.getString(4) + "("+c.getString(5)+")";
            inv.vc2expiredesc = c.getString(6);
        }
        c.close();
        return inv;
    }

    public static List<Inv> getAlreadyInvStockFromDb(SQLiteDatabase db, String guid) {
        Inv inv = null;
        List<Inv> list = new ArrayList<Inv>();
        String sql = "select t.vc2guid, t.vc2account, t.vc2mdse_code, t.numstock_count, " +
                "   s.vc2mdse_name, s.vc2mdse_sku, t.id, t.vc2expiredesc from" +
                "   invs t, skus s where t.vc2guid=?  " +
                "   and t.vc2mdse_code = s.vc2mdse_code " +
                "   order by t.id desc";
        Cursor c = db.rawQuery(sql, new String[]{guid});
        while (c.moveToNext()) {
            inv = new Inv();
            inv.vc2guid = c.getString(0);
            inv.vc2account = c.getString(1);
            inv.vc2mdse_code = c.getString(2);
            inv.numstock_count = c.getInt(3);
            inv.vc2mdse_name = c.getString(4) + "("+c.getString(5)+")";
            inv.vc2expiredesc = c.getString(7);
            list.add(inv);
        }
        c.close();
        return list;
    }

    public static List<Inv> getNotInvStockFromDb(SQLiteDatabase db, String guid) {
        Inv inv = null;
        List<Inv> list = new ArrayList<Inv>();
        String sql = "select c.vc2guid, c.vc2account, c.vc2mdse_code, " +
                "c.numstock_count, s.vc2mdse_name, s.vc2mdse_sku  from" +
                "   skus s, stocks c where c.vc2guid=?  " +
                "   and c.vc2mdse_code = s.vc2mdse_code  " +
                "   and not exists (" +
                "       select * from invs k where k.vc2mdse_code = c.vc2mdse_code" +
                "           and k.vc2guid = c.vc2guid )" +
                "    ";
        Cursor c = db.rawQuery(sql, new String[]{guid});
        while (c.moveToNext()) {
            inv = new Inv();
            inv.vc2guid = c.getString(0);
            inv.vc2account = c.getString(1);
            inv.vc2mdse_code = c.getString(2);
            inv.numstock_count = c.getInt(3);
            inv.vc2mdse_name = c.getString(4) + "("+c.getString(5)+")";
            list.add(inv);
        }
        c.close();
        return list;
    }

    public static void deleteInvsByAccount(SQLiteDatabase db, String account){
        db.execSQL("delete from invs where vc2account=?", new String[]{account});
    }

    public static void deleteInvsByGuid(SQLiteDatabase db, String guid){
        db.execSQL("delete from invs where vc2guid=?", new String[]{guid});
    }

    public void saveToDb(SQLiteDatabase db) {
        saveToDb(db, false);
    }

    public void saveToDb(SQLiteDatabase db, boolean check) {

        String sql = "delete from invs where vc2guid=? and vc2mdse_code=?";
        if (check) db.execSQL(sql, new Object[]{this.vc2guid, this.vc2mdse_code});

        sql = "insert into invs(vc2guid, vc2account, vc2mdse_code, numstock_count, vc2expiredesc)" +
                "   values(?,?,?,?,?)";
        db.execSQL(sql, new Object[]{this.vc2guid, this.vc2account, this.vc2mdse_code, this.numstock_count, this.vc2expiredesc});

    }

    public void delete(SQLiteDatabase db) {
        String sql = "delete from invs where vc2guid=? and vc2mdse_code=?";
       db.execSQL(sql, new Object[]{this.vc2guid, this.vc2mdse_code});
    }

    public static int getInvCount(SQLiteDatabase db, String guid) {
        String sql = "select count(*) from invs where vc2guid=?";
        Cursor c = db.rawQuery(sql, new String[]{guid});
        int ret = 0;
        if (c.moveToNext()) {
            ret = c.getInt(0);
        }
        return ret;
    }
}
