package net.linvx.android.practices;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.linvx.android.libs.ui.LnxNumberKeyboard;

public class NumberInputActivity extends AppCompatActivity {

    private TextView textView;
    private KeyboardView keyboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_input);
        textView = (TextView)findViewById(R.id.textview_number_input);
        keyboardView = (KeyboardView)findViewById(R.id.keyboradview_number_input);
        LnxNumberKeyboard k = new LnxNumberKeyboard(this, keyboardView, textView);
        k.showKeyboard();

    }

}
