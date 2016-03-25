package net.linvx.android.libs.ui;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.TextView;

import net.linvx.android.libs.R;
import net.linvx.android.libs.utils.StringUtils;

/**
 * Created by lizelin on 16/3/25.
 */
public class LnxNumberKeyboard {
    private KeyboardView keyboardView;
    private Keyboard k;// 数字键盘
    private TextView textView;

    public LnxNumberKeyboard(Context _ctx, KeyboardView _keyboardView, TextView _textView) {
        this.textView = _textView;
        k = new Keyboard(_ctx, R.xml.symbols);
        this.keyboardView = _keyboardView;
        keyboardView.setKeyboard(k);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setVisibility(View.VISIBLE);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        //一些特殊操作按键的codes是固定的比如完成、回退等
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            String input = textView.getText().toString();
            if (StringUtils.isEmpty(input))
                input = "";

            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (input.length()>=1)
                    input = input.substring(0, input.length()-1);
            } else if (primaryCode == 4896) {// 清空
                input = "";
            } else { //将要输入的数字现在编辑框中
                input += Character.toString((char) primaryCode);
            }

            if (StringUtils.isEmpty(input))
                input = "0";

            Long input_long = Long.parseLong(input);
            if (input_long > Integer.MAX_VALUE)
                return;
            Integer input_number = Integer.parseInt(input);
            input = input_number.toString();
            textView.setText(input);
        }
    };

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
}
