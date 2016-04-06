package com.haishang360.inventory.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.haishang360.inventory.R;
import com.haishang360.inventory.engine.UploadDataTask;

import net.linvx.android.libs.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UploadDataActivity extends AppCompatActivity {
    String name, guid;
    @Bind(R.id.button_upload_data)
    Button mButtonUploadData;

    @Bind(R.id.upload_data_textinput)
    AutoCompleteTextView mEditTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDataActivity.this.finish();
            }
        });
        guid = this.getIntent().getStringExtra("guid");
        name = this.getIntent().getStringExtra("name");
        this.getSupportActionBar().setTitle("上传" + name + "数据");

        mButtonUploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditTextEmail.getText().toString();
                if (StringUtils.isEmpty(email) || !email.contains("@"))
                    mEditTextEmail.setError("email 非法");
                else {
                    new UploadDataTask(UploadDataActivity.this, guid, email).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

            }
        });
    }
}
