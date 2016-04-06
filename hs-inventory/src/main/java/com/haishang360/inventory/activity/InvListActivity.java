package com.haishang360.inventory.activity;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.haishang360.inventory.R;
import com.haishang360.inventory.engine.Constants;
import com.haishang360.inventory.engine.DatabaseHelper;
import com.haishang360.inventory.models.Inv;
import com.haishang360.inventory.models.Stock;
import com.haishang360.inventory.models.Wh;

import net.linvx.android.libs.dialog.PromptDialog;
import net.linvx.android.libs.dialog.PromptDialogListener;
import net.linvx.android.libs.ui.LnxBaseAdapter;
import net.linvx.android.libs.utils.AppUtils;
import net.linvx.android.libs.utils.LogUtils;
import net.linvx.android.libs.utils.SharedPrefUtils;

import java.util.List;

public class InvListActivity extends HsBaseActivity {
    private ListView list = null;
    private InvListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = (ListView) this.findViewById(R.id.inv_list_activity_list);

        LogUtils.debug(SharedPrefUtils.readFieldString(this, Constants.CURR_USER_ACCOUNT, ""));
        SQLiteDatabase db = DatabaseHelper.getReadableDb();
        List<Wh> whs = Wh.getWhsByAccount(db,
                SharedPrefUtils.readFieldString(this, Constants.CURR_USER_ACCOUNT, ""));
        db.close();
        LogUtils.debug(whs);
        adapter = new InvListAdapter(this, whs);


        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public class InvListAdapter extends LnxBaseAdapter<Wh> {

        public InvListAdapter(Context context, List<Wh> l) {
            super(context);
            this.addItems(l);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_inv_list, parent, false);
            }
            TextView name = getAdapterView(convertView, R.id.inv_list_activity_list_item_name);
            TextView inv_count = getAdapterView(convertView, R.id.inv_list_activity_list_item_inv_count);
            Button show = getAdapterView(convertView, R.id.inv_list_activity_list_item_show);
            Button delete = getAdapterView(convertView, R.id.inv_list_activity_list_item_delete);
            Button notInv = getAdapterView(convertView, R.id.inv_list_activity_list_item_uninv);
            Button doInv = getAdapterView(convertView, R.id.inv_list_activity_list_item_do_inv);

            name.setText(this.getItem(position).vc2guid + "." + this.getItem(position).vc2warehousename);
//            SQLiteDatabase db = DbHelper.getInstance().getDb();
            SQLiteDatabase db = DatabaseHelper.getReadableDb();
            final int c = Inv.getInvCount(db, this.getItem(position).vc2guid);
            final int c1 = Stock.getStockCountByGuid(db, this.getItem(position).vc2guid);
            db.close();
            inv_count.setText("当前已经盘点商品数量：" + c + "  未盘：" + (c1 - c));

            doInv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String guid = adapter.getItem(position).vc2guid;
                    String name = adapter.getItem(position).vc2warehousename;
                    Bundle bundle = new Bundle();
                    bundle.putString("guid", guid);
                    bundle.putString("name", name);
                    // bundle.putString("invflag", "Y");
                    AppUtils.startActivity(context, HsScanActivity.class, bundle);
                }
            });

            if (c == 0)
                delete.setEnabled(false);
            else
                delete.setEnabled(true);

            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String guid = adapter.getItem(position).vc2guid;
                    String name = adapter.getItem(position).vc2warehousename;
                    Bundle bundle = new Bundle();
                    bundle.putString("guid", guid);
                    bundle.putString("name", name);
                    bundle.putString("invflag", "Y");
                    AppUtils.startActivity(context, WhInvListActivity.class, bundle);

                }
            });

            notInv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String guid = adapter.getItem(position).vc2guid;
                    String name = adapter.getItem(position).vc2warehousename;
                    Bundle bundle = new Bundle();
                    bundle.putString("guid", guid);
                    bundle.putString("name", name);
                    bundle.putString("invflag", "N");
                    AppUtils.startActivity(context, WhInvListActivity.class, bundle);
                }
            });


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PromptDialog.showMyPrompt(InvListActivity.this, "请确认删除？数据不可恢复！",
                            new PromptDialogListener() {
                                @Override
                                public void onOk() {

                                    SQLiteDatabase db = DatabaseHelper.getWritableDb();
                                    db.beginTransaction();
                                    try {
                                        Inv.deleteInvsByGuid(db, adapter.getItem(position).vc2guid);
                                        db.setTransactionSuccessful();

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        db.endTransaction();
                                        db.close();
                                    }

                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                }
            });
            return convertView;
        }
    }
}

