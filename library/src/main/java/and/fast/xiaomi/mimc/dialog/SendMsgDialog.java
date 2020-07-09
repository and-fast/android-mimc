package and.fast.xiaomi.mimc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.xiaomi.mimc.MIMCUser;
import and.fast.xiaomi.mimc.R;
import and.fast.xiaomi.mimc.common.Constant;
import and.fast.xiaomi.mimc.common.UserManager;

public class SendMsgDialog extends Dialog {

    public SendMsgDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_msg_dialog);
        setCancelable(true);
        setTitle(R.string.button_send);
        final EditText toEditText = (EditText)findViewById(R.id.chat_to);
        final SharedPreferences sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        toEditText.setText(sp.getString("toAccount", null));

        findViewById(R.id.chat_send).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mTo = toEditText.getText().toString();
                String mContent = ((EditText) findViewById(R.id.chat_content))
                        .getText().toString();

                sp.edit().putString("toAccount", mTo).commit();

                if (!TextUtils.isEmpty(mTo)){
                    UserManager userManager = UserManager.getInstance();
                    MIMCUser user = userManager.getMIMCUser();
                    if (user != null) {
                        userManager.sendMsg(mTo, mContent.getBytes(), Constant.TEXT);
                    }
                    dismiss();
                }
            }
        });
    }
}
