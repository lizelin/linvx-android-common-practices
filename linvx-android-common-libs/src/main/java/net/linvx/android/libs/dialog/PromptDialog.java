package net.linvx.android.libs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.linvx.android.libs.R;
import net.linvx.android.libs.utils.DeviceUtils;
import net.linvx.android.libs.utils.StringUtils;

public class PromptDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Button btn_mydialog_ok, btn_mydialog_cancel;
    private TextView tv_mydialog_content_message;
    private PromptDialogListener listener = null;

    public PromptDialog(Context context) {
        super(context, R.style.theme_prompt_dialog);
        setContentView(R.layout.dialog_prompt);
        setCanceledOnTouchOutside(true);
        this.context = context;
        findById();
    }

    private void findById() {
        btn_mydialog_ok = (Button) findViewById(R.id.btn_mydialog_ok);
        btn_mydialog_cancel = (Button) findViewById(R.id.btn_mydialog_cancel);
        btn_mydialog_cancel.setOnClickListener(this);
        btn_mydialog_ok.setOnClickListener(this);
        tv_mydialog_content_message = (TextView) findViewById(R.id.tv_mydialog_content_message);
        tv_mydialog_content_message.setMinimumHeight((int)(DeviceUtils.getScreenHeight(context)*0.2));
        tv_mydialog_content_message.setMinimumWidth((int)(DeviceUtils.getScreenWidth(context)*0.68));
    }

    @Override
    public void onClick(View v) {
        if (listener!=null) {
            if (v.getId() == R.id.btn_mydialog_ok) {
                listener.onOk();;
            } else if (v.getId() == R.id.btn_mydialog_cancel) {
                listener.onCancel();
            }
        }
        cancel();
    }

    public void setMessage(CharSequence message) {
        if (StringUtils.isNotEmpty(message.toString()))
            tv_mydialog_content_message.setText(message);
    }

    public void setOkButtonText(String msg) {
        if (StringUtils.isNotEmpty(msg))
            btn_mydialog_ok.setText(msg);
    }

    public void setCancelButtonText(String msg) {
        if (StringUtils.isNotEmpty(msg))
            btn_mydialog_cancel.setText(msg);
    }

    public void setListener(PromptDialogListener l) {
        this.listener  = l;
    }

    public void setOkButtonVisible(boolean show) {
        if (show)
            this.btn_mydialog_ok.setVisibility(View.VISIBLE);
        else
            this.btn_mydialog_ok.setVisibility(View.GONE);
    }
    public void setCancelButtonVisible(boolean show) {
        if (show)
            this.btn_mydialog_cancel.setVisibility(View.VISIBLE);
        else
            this.btn_mydialog_cancel.setVisibility(View.GONE);
    }

    public static PromptDialog showMyPrompt(Context context, String msg, PromptDialogListener l,
                                            String okText, String cancelText, boolean showOk, boolean showCancel){
        PromptDialog d = new PromptDialog(context);
        if (msg!=null)
            d.setMessage(msg);
        if (l!=null)
            d.setListener(l);
        if (StringUtils.isNotEmpty(okText))
            d.setOkButtonText(okText);
        if (StringUtils.isNotEmpty(cancelText))
            d.setCancelButtonText(cancelText);
        d.setOkButtonVisible(showOk);
        d.setCancelButtonVisible(showCancel);
        d.getWindow().setGravity(Gravity.CENTER);

        d.show();
        return d;
    }

    public static PromptDialog showMyPrompt(Context context, String msg, PromptDialogListener l){
        return PromptDialog.showMyPrompt(context, msg, l, null, null, true, true);
    }

    public static PromptDialog showMyAlert(Context context, String msg, PromptDialogListener l){
        return PromptDialog.showMyPrompt(context, msg, l, null, null, true, false);
    }
}