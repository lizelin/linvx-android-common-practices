package net.linvx.android.libs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import net.linvx.android.libs.R;
import net.linvx.android.libs.common.MyTimer;
import net.linvx.android.libs.utils.StringUtils;

public class LoadingDialog extends Dialog {
    private LoadingDialog(Context context) {
        super(context);
    }

    private LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    private LoadingDialog(Context context, boolean cancelable,
                          OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public LoadingDialog(Context context, String msg) {
        super(context, R.style.theme_loading_dialog);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(true);

        if (StringUtils.isNotEmpty(msg))
            ((TextView) findViewById(R.id.dialog_loading)).setText(msg);
    }

    public void setMsg(String msg){
        String oldMsg = ((TextView) findViewById(R.id.dialog_loading)).getText().toString();
        if (!msg.equals(oldMsg))
            ((TextView) findViewById(R.id.dialog_loading)).setText(msg);
    }

    private MyTimer myTimer = new MyTimer(new Runnable(){
        public void run() {
            if (LoadingDialog.this.isShowing())
                LoadingDialog.this.dismiss();
        }
    }, null, 10, 1, 1);

    @Override
    public void show() {
        if (!this.isShowing())
            super.show();
        myTimer.start();
    }

    @Override
    public void dismiss() {
        myTimer.stop();
        super.dismiss();
    }


}
