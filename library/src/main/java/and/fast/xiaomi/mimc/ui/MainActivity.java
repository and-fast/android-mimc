package and.fast.xiaomi.mimc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.xiaomi.mimc.*;
import com.xiaomi.mimc.common.MIMCConstant;
import com.xiaomi.mimc.R;
import and.fast.xiaomi.mimc.bean.ChatMsg;
import and.fast.xiaomi.mimc.common.ChatAdapter;
import and.fast.xiaomi.mimc.common.NetWorkUtils;
import and.fast.xiaomi.mimc.common.ParseJson;
import and.fast.xiaomi.mimc.common.UserManager;
import and.fast.xiaomi.mimc.dialog.*;

import java.util.ArrayList;
import java.util.List;

import and.fast.xiaomi.mimc.dialog.CreateGroupDialog;
import and.fast.xiaomi.mimc.dialog.CreateUnlimitedGroupDialog;
import and.fast.xiaomi.mimc.dialog.DismissGroupDialog;
import and.fast.xiaomi.mimc.dialog.DismissUnlimitedGroupDialog;
import and.fast.xiaomi.mimc.dialog.GroupInfoDialog;
import and.fast.xiaomi.mimc.dialog.JoinGroupDialog;
import and.fast.xiaomi.mimc.dialog.JoinUnlimitedGroupDialog;
import and.fast.xiaomi.mimc.dialog.KickGroupDialog;
import and.fast.xiaomi.mimc.dialog.LoginDialog;
import and.fast.xiaomi.mimc.dialog.PullP2PHistoryMsgDialog;
import and.fast.xiaomi.mimc.dialog.PullP2THistoryMsgDialog;
import and.fast.xiaomi.mimc.dialog.QueryGroupInfoDialog;
import and.fast.xiaomi.mimc.dialog.QueryUnlimitedGroupMembersDialog;
import and.fast.xiaomi.mimc.dialog.QueryUnlimitedGroupOnlineUsersDialog;
import and.fast.xiaomi.mimc.dialog.QuitGroupDialog;
import and.fast.xiaomi.mimc.dialog.QuitUnlimitedGroupDialog;
import and.fast.xiaomi.mimc.dialog.SendGroupMsgDialog;
import and.fast.xiaomi.mimc.dialog.SendMsgDialog;
import and.fast.xiaomi.mimc.dialog.SendUnlimitedGroupMsgDialog;
import and.fast.xiaomi.mimc.dialog.UpdateGroupDialog;
import and.fast.xiaomi.mimc.dialog.VoiceDialog;

@Deprecated
public class MainActivity extends Activity implements UserManager.OnHandleMIMCMsgListener {

    private ChatAdapter   mAdapter;
    private RecyclerView  mRecyclerView;
    private List<ChatMsg> mDatas = new ArrayList<>();

    GroupInfoDialog groupInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        groupInfoDialog = new GroupInfoDialog(this);

        // 设置处理MIMC消息监听器
        UserManager.getInstance().setHandleMIMCMsgListener(this);

        // 登录
        findViewById(R.id.mimc_login).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog LoginDialog = new LoginDialog(MainActivity.this);
                    LoginDialog.show();
                }
            });

        // 注销
        findViewById(R.id.mimc_logout).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MIMCUser user = UserManager.getInstance().getMIMCUser();
                    if (user != null) {
                        user.logout();
                        user.destroy();
                    }
                }
            });

        // 发送消息
        findViewById(R.id.mimc_sendMsg).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog SendMsgDialog = new SendMsgDialog(MainActivity.this);
                    SendMsgDialog.show();
                }
            });

        // 创建群
        findViewById(R.id.btn_create_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgCreateGroup = new CreateGroupDialog(MainActivity.this);
                    dlgCreateGroup.show();
                }
            });

        // 查询群信息
        findViewById(R.id.btn_query_group_info).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgQueryGroupInfo = new QueryGroupInfoDialog(MainActivity.this);
                    dlgQueryGroupInfo.show();
                }
            });

        // 查询用户已加入的群信息
        findViewById(R.id.btn_query_all_group_info).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkUtils.isNetwork(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (UserManager.getInstance().getStatus() != MIMCConstant.OnlineStatus.ONLINE) {
                        Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserManager.getInstance().queryGroupsOfAccount();
                }
            });

        // 邀请用户加入群
        findViewById(R.id.btn_join_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgJoinGroup = new JoinGroupDialog(MainActivity.this);
                    dlgJoinGroup.show();
                }
            });

        // 非群主用户退出群
        findViewById(R.id.btn_quit_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgQuitGroup = new QuitGroupDialog(MainActivity.this);
                    dlgQuitGroup.show();
                }
            });

        // 群主踢用户出群
        findViewById(R.id.btn_kick_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgKickGroup = new KickGroupDialog(MainActivity.this);
                    dlgKickGroup.show();
                }
            });

        // 群主更新群信息
        findViewById(R.id.btn_update_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgUpdateGroup = new UpdateGroupDialog(MainActivity.this);
                    dlgUpdateGroup.show();
                }
            });

        // 群主销毁群
        findViewById(R.id.btn_dismiss_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgDismissGroup = new DismissGroupDialog(MainActivity.this);
                    dlgDismissGroup.show();
                }
            });

        // 发送群消息
        findViewById(R.id.btn_send_group_msg).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgSendGroupMsg = new SendGroupMsgDialog(MainActivity.this);
                    dlgSendGroupMsg.show();
                }
            });

        // 拉取单聊休息记录
        findViewById(R.id.btn_p2p_history).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgP2PHistory = new PullP2PHistoryMsgDialog(MainActivity.this);
                    dlgP2PHistory.show();
                }
            });

        // 拉取群聊消息记录
        findViewById(R.id.btn_p2t_history).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dlgP2THistory = new PullP2THistoryMsgDialog(MainActivity.this);
                        dlgP2THistory.show();
                    }
                });

        // 创建无限大群
        findViewById(R.id.btn_create_unlimited_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgCreateUnlimitedGroup = new CreateUnlimitedGroupDialog(MainActivity.this);
                    dlgCreateUnlimitedGroup.show();
                }
            });
        // 加入无限大群
        findViewById(R.id.btn_join_unlimited_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgJoinUnlimitedGroup = new JoinUnlimitedGroupDialog(MainActivity.this);
                    dlgJoinUnlimitedGroup.show();
                }
            });
        // 退出无限大群
        findViewById(R.id.btn_quit_unlimited_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgQuitUnlimitedGroup = new QuitUnlimitedGroupDialog(MainActivity.this);
                    dlgQuitUnlimitedGroup.show();
                }
            });
        // 解散无限大群
        findViewById(R.id.btn_dismiss_unlimited_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgDismissUnlimitedGroup = new DismissUnlimitedGroupDialog(MainActivity.this);
                    dlgDismissUnlimitedGroup.show();
                }
            });
        // 查询无限大群成员
        findViewById(R.id.btn_query_unlimited_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgQueryUnlimitedGroupMembers = new QueryUnlimitedGroupMembersDialog(MainActivity.this);
                    dlgQueryUnlimitedGroupMembers.show();
                }
            });
        // 查询所属无限大群
        findViewById(R.id.btn_query_owner_all_unlimited_group).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkUtils.isNetwork(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (UserManager.getInstance().getStatus() != MIMCConstant.OnlineStatus.ONLINE) {
                        Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserManager.getInstance().queryUnlimitedGroups();
                }
            });
        // 查询无限大群在线用户数
        findViewById(R.id.btn_query_unlimited_group_online_users).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgQueryUnlimitedGroupOnlineUsers = new QueryUnlimitedGroupOnlineUsersDialog(MainActivity.this);
                    dlgQueryUnlimitedGroupOnlineUsers.show();
                }
            });

        // 发送无限大群消息
        findViewById(R.id.btn_send_unlimited_group_message).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dlgSendUnlimitedGroupMsg = new SendUnlimitedGroupMsgDialog(MainActivity.this);
                    dlgSendUnlimitedGroupMsg.show();
                }
            });


        // 语音通话
        findViewById(R.id.btn_p2p_audio_call).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog voiceDialog = new VoiceDialog(MainActivity.this);
                    voiceDialog.show();
                }
            });

        mRecyclerView = findViewById(R.id.rv_chat);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ChatAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void updateOnlineStatus(MIMCConstant.OnlineStatus status) {
        TextView textView = findViewById(R.id.mimc_status);
        Drawable drawable;
        if (status == MIMCConstant.OnlineStatus.ONLINE) {
            drawable = getResources().getDrawable(R.drawable.point_h);
        } else {
            drawable = getResources().getDrawable(R.drawable.point);
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null,
                null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (UserManager.getInstance().getMIMCUser() != null) {
            updateOnlineStatus(UserManager.getInstance().getMIMCUser().getOnlineStatus());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 处理单聊消息
    @Override
    public void onHandleMessage(final ChatMsg chatMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDatas.add(chatMsg);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
    }

    // 处理群消息
    @Override
    public void onHandleGroupMessage(final ChatMsg chatMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDatas.add(chatMsg);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
    }

    public List<ChatMsg> getDatas() {
        return mDatas;
    }

    // 处理登录状态
    @Override
    public void onHandleStatusChanged(final MIMCConstant.OnlineStatus status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateOnlineStatus(status);
            }
        });
    }

    // 处理服务端消息确认
    @Override
    public void onHandleServerAck(final MIMCServerAck serverAck) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "ServerAck: " + serverAck.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onHandleOnlineMessageAck(final MIMCOnlineMessageAck onlineMessageAck) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "OnlineMessageAck: " + onlineMessageAck.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPullNotification() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "onPullNotification: Please pull offline msgs!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // 处理创建群
    @Override
    public void onHandleCreateGroup(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseCreateGroupJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理查询群信息
    @Override
    public void onHandleQueryGroupInfo(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseQueryGroupInfoJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理查询已加入的群信息
    @Override
    public void onHandleQueryGroupsOfAccount(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseQueryGroupsOfAccountJson(this, json);
        }
        final String info = json;
                runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理加入群
    @Override
    public void onHandleJoinGroup(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseJoinGroupJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理非群主退群
    @Override
    public void onHandleQuitGroup(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseQuitGroupJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理群主踢人出群
    @Override
    public void onHandleKickGroup(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseKickGroupJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理群主更新群信息
    @Override
    public void onHandleUpdateGroup(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseUpdateGroupJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理群主销毁群
    @Override
    public void onHandleDismissGroup(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseDismissGroupJson(json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理拉取单聊消息
    @Override
    public void onHandlePullP2PHistory(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseP2PHistoryJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理拉取群聊消息
    @Override
    public void onHandlePullP2THistory(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseP2THistoryJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    // 处理发送消息超时
    @Override
    public void onHandleSendMessageTimeout(MIMCMessage message) {
        final String info = new String(message.getPayload());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Send message timeout: " +
                    info, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 处理发送群消息超时
    @Override
    public void onHandleSendGroupMessageTimeout(final MIMCGroupMessage groupMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Send group message timeout: " +
                    new String(groupMessage.getPayload()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onHandleJoinUnlimitedGroup(long topicId, int code, String errMsg) {
        final String info = "topicId:" + topicId + " code:" + code + " errMsg:" + errMsg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    @Override
    public void onHandleQuitUnlimitedGroup(long topicId, int code, String message) {
        final String info = "topicId:" + topicId + " code:" + code + " message:" + message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    @Override
    public void onHandleDismissUnlimitedGroup(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseDismissUnlimitedGroupJson(json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    @Override
    public void onHandleQueryUnlimitedGroupMembers(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseQueryUnlimitedGroupJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    @Override
    public void onHandleQueryUnlimitedGroups(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseQueryUnlimitedGroupsJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }

    @Override
    public void onHandleQueryUnlimitedGroupOnlineUsers(String json, boolean isSuccess) {
        if (isSuccess) {
            json = ParseJson.parseQueryUnlimitedGroupOnlineUsersJson(this, json);
        }
        final String info = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupInfoDialog.show();
                groupInfoDialog.setContent(info);
            }
        });
    }
}