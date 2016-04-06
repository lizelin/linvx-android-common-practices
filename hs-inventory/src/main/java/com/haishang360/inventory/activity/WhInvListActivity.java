package com.haishang360.inventory.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.haishang360.inventory.R;
import com.haishang360.inventory.engine.DatabaseHelper;
import com.haishang360.inventory.models.Inv;

import net.linvx.android.libs.ui.LnxBaseAdapter;
import net.linvx.android.libs.utils.AppUtils;
import net.linvx.android.libs.utils.LogUtils;

import java.util.List;

public class WhInvListActivity extends AppCompatActivity {
    private String guid, name, invFlag;
    private ListView list;
    private WhInvListAdapter adapter;
    private Button mButtonUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wh_inv_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        guid = this.getIntent().getStringExtra("guid");
        LogUtils.error(guid);
        name = this.getIntent().getStringExtra("name");
        invFlag = this.getIntent().getStringExtra("invflag");
        this.getSupportActionBar().setTitle(name);

        list = (ListView) this.findViewById(R.id.wh_inv_list_activity_list);
        mButtonUpload = (Button)this.findViewById(R.id.wh_inv_list_activity_upload);

        List<Inv> data = null;
        if ("Y".equalsIgnoreCase(invFlag)) {
            SQLiteDatabase db = DatabaseHelper.getReadableDb();
            data = Inv.getAlreadyInvStockFromDb(db, guid);
            db.close();
            mButtonUpload.setVisibility(View.VISIBLE);
            mButtonUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("guid", guid);
                    bundle.putString("name", name);
                    AppUtils.startActivity(WhInvListActivity.this, UploadDataActivity.class, bundle);

                }
            });
        } else {
            SQLiteDatabase db = DatabaseHelper.getReadableDb();
            data = Inv.getNotInvStockFromDb(db, guid);
            db.close();
            mButtonUpload.setVisibility(View.GONE);
        }
        adapter = new WhInvListAdapter(this, data);
        list.setAdapter(adapter);


    }

    public class WhInvListAdapter extends LnxBaseAdapter<Inv> {
        public WhInvListAdapter(Context context, List<Inv> l) {
            super(context);
            this.addItems(l);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_wh_inv_list, parent, false);
            }
            TextView count = getAdapterView(convertView, R.id.wh_inv_list_activity_list_item_barcodeandcount);
            TextView name = getAdapterView(convertView, R.id.wh_inv_list_activity_list_item_name);
            Inv inv = this.getItem(position);
            count.setText(inv.vc2mdse_code + ": 数量："+inv.numstock_count);
            name.setText(inv.vc2mdse_name);
            return convertView;
        }
    }

}
