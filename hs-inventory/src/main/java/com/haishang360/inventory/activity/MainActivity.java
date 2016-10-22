package com.haishang360.inventory.activity;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.haishang360.inventory.R;
import com.haishang360.inventory.engine.AppHelper;
import com.haishang360.inventory.engine.Constants;
import com.haishang360.inventory.engine.DatabaseHelper;
import com.haishang360.inventory.engine.HttpTask;
import com.haishang360.inventory.models.Sku;
import com.haishang360.inventory.models.Stock;
import com.haishang360.inventory.models.Wh;

import net.linvx.android.libs.dialog.LoadingDialog;
import net.linvx.android.libs.http.LnxHttpResponse;
import net.linvx.android.libs.utils.AppUtils;
import net.linvx.android.libs.utils.LogUtils;
import net.linvx.android.libs.utils.SharedPrefUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends HsBaseActivity implements HttpTask.HttpResponseHandler {

    @Bind(R.id.main_activity_button_get_init_data)
    Button mButtonGetInitData;

    @Bind(R.id.main_activity_button_logout)
    Button mButtonLogout;

    @Bind(R.id.main_activity_button_inv_list)
    Button mButtonInvList;

    @OnClick({R.id.main_activity_button_get_init_data,
            R.id.main_activity_button_logout,
            R.id.main_activity_button_inv_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_activity_button_get_init_data:
                getInitData();
                break;
            case R.id.main_activity_button_logout:
                logout();
                break;
            case R.id.main_activity_button_inv_list:
                AppUtils.startActivity(this, InvListActivity.class);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        checkLogin();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
//        LnxImageLoader.init(this);
//        LogUtils.turnOff();
        LogUtils.turnOn();
    }

    private void checkLogin() {
        new HttpTask(Constants.URL_SERVER_PREFIX + "checklogin", "", this, this, Constants.TID_CHECKLOGIN).exec();
    }

    private void login() {
        AppUtils.startActivity(this, LoginActivity.class);
    }

    private void getInitData() {
        new HttpTask(Constants.URL_SERVER_PREFIX + "get_init_data", "", this, this, Constants.TID_GET_INIT_DATA).exec();
    }

    private void logout() {
        new HttpTask(Constants.URL_SERVER_PREFIX + "logout", "", this, this, Constants.TID_LOGOUT).exec();
    }

    @Override
    public void handleResponse(LnxHttpResponse response) {
        if (!AppHelper.preParseResponse(this, response))
            return;
        if (response.getTid().equalsIgnoreCase(Constants.TID_LOGOUT)) {
            SharedPrefUtils.saveField(this, Constants.CURR_USER_ACCOUNT, "");
            login();
        } else if (response.getTid().equalsIgnoreCase(Constants.TID_GET_INIT_DATA)) {
            parseJson(response);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
//            DbHelper.getInstance().getDb().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    LoadingDialog dialog = null;
    public void parseJson(final LnxHttpResponse response) {
        final LoadingDialog dialog = new LoadingDialog(this, "");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject json = (JSONObject) response.getExtraData(Constants.HTTP_EXTRA_DATA_JSON_KEY);
                SQLiteDatabase db = DatabaseHelper.getWritableDb();
                db.beginTransaction();
//                SQLiteDatabase db = DbHelper.getInstance().getDb();
                try {
                    Sku.deleteAllSkus(db);
                    Wh.deleteWhsByAccount(db, SharedPrefUtils.readFieldString(MainActivity.this, Constants.CURR_USER_ACCOUNT, ""));
                    Stock.deleteStocksByAccount(db, SharedPrefUtils.readFieldString(MainActivity.this, Constants.CURR_USER_ACCOUNT, ""));
                    Gson g = new Gson();

                    Sku[] skus = new Gson().fromJson(json.optJSONArray("skus").toString(), Sku[].class);
                    for (int i=0; i<skus.length; i++)
                        skus[i].saveToDb(db);
                    /**
                     * test data
                     */
                    Sku skutest = new Sku();
                    skutest.vc2mdse_code = "6920236708119";
                    skutest.vc2mdse_name = "饼干";
                    skutest.vc2mdse_sku = "500g";
                    skutest.saveToDb(db);

                    Wh[] whs = new Gson().fromJson(json.optJSONArray("whs").toString(), Wh[].class);
                    for (int i=0; i<whs.length; i++)
                        whs[i].saveToDb(db);
                    Stock[] stocks = new Gson().fromJson(json.optJSONArray("stocks").toString(), Stock[].class);
                    for (int i=0; i<stocks.length; i++)
                        stocks[i].saveToDb(db);
                    db.setTransactionSuccessful();

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    db.endTransaction();
                    db.close();
                }

                response.releaseMe();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });

            }
        }).start();


    }
}
