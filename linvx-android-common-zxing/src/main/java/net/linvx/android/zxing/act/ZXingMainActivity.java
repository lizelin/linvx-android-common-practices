package net.linvx.android.zxing.act;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.linvx.android.zxing.R;


/**
 * 调用demo
 */
public class ZXingMainActivity extends Activity {
//	private final static int SCANNIN_GREQUEST_CODE = 1;
	/**
	 * 显示扫描结果
	 */
	private TextView mTextView ;
	/**
	 * 显示扫描拍的图片
	 */
	private ImageView mImageView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		mTextView = (TextView) findViewById(R.id.result);
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);

		//点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		//扫描完了之后调到该界面
		Button mButton = (Button) findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MipcaActivityCapture.startBarcodeScanView(ZXingMainActivity.this);
//				Intent intent = new Intent();
//				intent.setClass(ZXingMainActivity.this, MipcaActivityCapture.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case MipcaActivityCapture.SCANNIN_GREQUEST_CODE:
				if(resultCode == RESULT_OK){
					Bundle bundle = data.getExtras();
					//显示扫描到的内容
					mTextView.setText(bundle.getString("result"));
					//显示
					mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
				}
				break;
		}
	}

}
