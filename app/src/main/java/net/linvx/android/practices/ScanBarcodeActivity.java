package net.linvx.android.practices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.linvx.android.zxing.act.MipcaActivityCapture;

import static net.linvx.android.zxing.act.MipcaActivityCapture.startBarcodeScanView;

public class ScanBarcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startBarcodeScanView(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MipcaActivityCapture.SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    // 获取扫描结果（条码内容）
                    String text = bundle.getString("result");
                    TextView tv = (TextView) this.findViewById(R.id.text_view_barcode);
                    tv.setText(text);
                    // 获取扫描的图片
                    Bitmap bitmap = (Bitmap) data.getParcelableExtra("bitmap");
                    ImageView iv = (ImageView) this.findViewById(R.id.image_view_barcode);
                    iv.setImageBitmap(bitmap);
                }
                break;
        }
    }
}
