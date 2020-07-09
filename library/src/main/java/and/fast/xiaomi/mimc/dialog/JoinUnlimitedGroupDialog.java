package and.fast.xiaomi.mimc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.xiaomi.mimc.common.MIMCConstant;
import and.fast.xiaomi.mimc.R;
import and.fast.xiaomi.mimc.common.NetWorkUtils;
import and.fast.xiaomi.mimc.common.UserManager;

public class JoinUnlimitedGroupDialog extends Dialog {

    public JoinUnlimitedGroupDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_unlimited_group_dialog);
        setCancelable(true);
        setTitle(R.string.join_unlimited_group);
        final EditText etGroupId = findViewById(R.id.et_group_id);

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String groupId = etGroupId.getText().toString();

                if (!NetWorkUtils.isNetwork(getContext())) {
                    Toast.makeText(getContext(), getContext().getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
                    return;
                } else if (UserManager.getInstance().getStatus() != MIMCConstant.OnlineStatus.ONLINE) {
                    Toast.makeText(getContext(), getContext().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                    return;
                } else if (groupId.isEmpty()) {
                    Toast.makeText(getContext(), getContext().getString(R.string.input_id_of_group), Toast.LENGTH_SHORT).show();
                    return;
                }

                UserManager.getInstance().getMIMCUser().joinUnlimitedGroup(Long.parseLong(groupId), null);
                dismiss();
            }
        });
    }
}
