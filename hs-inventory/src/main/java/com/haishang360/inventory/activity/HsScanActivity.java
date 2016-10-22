package com.haishang360.inventory.activity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haishang360.inventory.R;
import com.haishang360.inventory.engine.Constants;
import com.haishang360.inventory.engine.DatabaseHelper;
import com.haishang360.inventory.models.Inv;
import com.haishang360.inventory.models.Sku;

import net.linvx.android.libs.utils.AppUtils;
import net.linvx.android.libs.utils.LogUtils;
import net.linvx.android.libs.utils.SharedPrefUtils;
import net.linvx.android.libs.utils.StringUtils;
import net.linvx.android.zxing.act.MipcaActivityCapture;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HsScanActivity extends HsBaseActivity implements TextView.OnEditorActionListener {
    @Bind(R.id.hs_scan_activity_edit_text_barcode)
    AutoCompleteTextView mEditTextBarcode;

    @Bind(R.id.hs_scan_activity_text_view_sku)
    EditText mEditTextSku;

//    @Bind(R.id.image_view_barcode)
//    ImageView mImageViewBarcode;
//
//    @Bind(R.id.keyboradview_number_input)
//    KeyboardView keyboardView;

    // 已经盘点数量
    @Bind(R.id.textview_number_input)
    TextView textViewHasInvCount;

    // 本次盘点数量
    @Bind(R.id.textview_curr_number_input)
    TextView textViewCurrInvCount;

    // 本次盘点有效期
    @Bind(R.id.textview_curr_valid_input)
    TextView textViewCurrExpire;

    // 已盘点的有效期数据
    @Bind(R.id.textview_valid_input)
    TextView textViewHasInvExpire;

    @Bind(R.id.button_save_to_db)
    Button mButtonSaveToDb;


    String guid, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hs_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        guid = this.getIntent().getStringExtra("guid");
        name = this.getIntent().getStringExtra("name");
        this.getSupportActionBar().setTitle(name);

        textViewCurrInvCount.requestFocus();
        textViewCurrInvCount.setOnEditorActionListener(this);
        mEditTextBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtils.error("onTextChanged");
                String text = mEditTextBarcode.getText().toString();
                if (StringUtils.isEmpty(text) || text.length() <= 4)
                    return;
                SQLiteDatabase db = DatabaseHelper.getReadableDb();
                Sku sku = Sku.getSkuFromDb(db, text);

                if (sku == null)
                    mEditTextSku.setText("无此商品");
                else
                    mEditTextSku.setText(sku.vc2mdse_name + "(" + sku.vc2mdse_sku + ")");

                Inv inv = Inv.getStockFromDb(db, guid, text);
                if (inv == null) {
                    textViewHasInvCount.setText("0");
                    textViewHasInvExpire.setText("");
                } else {
                    textViewHasInvCount.setText("" + inv.numstock_count);
                    textViewHasInvExpire.setText(inv.vc2expiredesc);
                    textViewCurrInvCount.setText("0");
                    textViewCurrExpire.setText("");
                }

                db.close();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        textViewHasInvCount = (TextView) findViewById(R.id.textview_number_input);
//        keyboardView = (KeyboardView) findViewById(R.id.keyboradview_number_input);
//        LnxNumberKeyboard k = new LnxNumberKeyboard(this, keyboardView, textViewHasInvCount);
//        k.showKeyboard();

        mButtonSaveToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sCount = textViewHasInvCount.getText().toString();
                String sCurrCount = textViewCurrInvCount.getText().toString();
                String sValid = textViewCurrExpire.getText().toString();
                String sHasInvExpire = textViewHasInvExpire.getText().toString();
                String code = mEditTextBarcode.getText().toString();
                String skuName = mEditTextSku.getText().toString();
                if (StringUtils.isEmpty(code)
                        || StringUtils.isEmpty(skuName)
                        || StringUtils.isEmpty(sCurrCount)
                        || StringUtils.isEmpty(sValid)
                        || Integer.parseInt(sValid) == 0)
                    return;
//                if (StringUtils.isEmpty(sCount) || StringUtils.isEmpty(code)
//                        || StringUtils.isEmpty(skuName) || Integer.parseInt(sCount) == 0
//                        || StringUtils.isEmpty(sCurrCount) || StringUtils.isEmpty(sValid)
//                        || Integer.parseInt(sCount) == 0 || Integer.parseInt(sValid) == 0)
//                    return;

                Inv inv = new Inv();
                inv.vc2account = SharedPrefUtils.readFieldString(HsScanActivity.this, Constants.CURR_USER_ACCOUNT, "");
                inv.numstock_count = Integer.parseInt(sCount) + Integer.parseInt(sCurrCount);
                inv.vc2mdse_name = skuName;
                inv.vc2guid = guid;
                inv.vc2mdse_code = code;
                if (StringUtils.isEmpty(sHasInvExpire)) {
                    inv.vc2expiredesc = "[有效期："+ sValid + "  数量：" + sCurrCount+"]";
                } else {
                    inv.vc2expiredesc = sHasInvExpire + "[有效期："+ sValid + "  数量：" + sCurrCount+"]";
                }
                SQLiteDatabase db = DatabaseHelper.getWritableDb();
                db.beginTransaction();
                try {
                    inv.saveToDb(db, true);
                    LogUtils.error("db is open: " + db.isOpen());
                    db.setTransactionSuccessful();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                    db.close();
                }


                AppUtils.showMsgShortTime(HsScanActivity.this, "保存完成");
                textViewHasInvCount.setText("0");
                mEditTextBarcode.setText("");
                mEditTextSku.setText("");
                net.linvx.android.zxing.act.MipcaActivityCapture.startBarcodeScanView(HsScanActivity.this);

            }
        });
        net.linvx.android.zxing.act.MipcaActivityCapture.startBarcodeScanView(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MipcaActivityCapture.SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    // 获取扫描结果（条码内容）
                    String text = bundle.getString("result");
                    LogUtils.debug(text);
                    SQLiteDatabase db = DatabaseHelper.getReadableDb();
                    Sku sku = Sku.getSkuFromDb(db, text);
                    db.close();
                    if (sku == null)
                        mEditTextSku.setText("无此商品");
                    else
                        mEditTextSku.setText(sku.vc2mdse_name + "(" + sku.vc2mdse_sku + ")");
                    mEditTextBarcode.setText(text);
                    textViewCurrInvCount.requestFocus();
                    // 获取扫描的图片
                    Bitmap bitmap = (Bitmap) data.getParcelableExtra("bitmap");
//                    mImageViewBarcode.setImageBitmap(bitmap);
                }
                break;
        }

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_NEXT:
            case EditorInfo.IME_NULL:
                LogUtils.getLogger().d("IME_ACTION_NEXT");
                if (v.getId() == R.id.textview_curr_number_input) {
                    textViewCurrExpire.requestFocus();
                    LogUtils.getLogger().d("IME_ACTION_NEXT textview_curr_number_input");
                }
                break;
            default:
                    return false;
        }
        return true;
    }

}
