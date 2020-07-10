package and.fast.xiaomi.mimc.common;

import android.app.Application;

public class MIMessageClient {

    private MIMessageClient() {
        // 禁止手动创建对象
    }

    static Application sApplication;


    // online
    //private long appId = 2882303761517669588L;
    //private String appKey = "5111766983588";
    //private String appSecret = "b0L3IOz/9Ob809v8H2FbVg==";
    //private String regionKey = "REGION_CN";
    //private String domain = "https://mimc.chat.xiaomi.net/";

    static long sAppId;
    static String sAppKey;
    static String sAppSecret;
    static String sRegionKey;
    static String sDomain;

    /**
     * appId/appKey/appSecret，小米开放平台(https://dev.mi.com/console/appservice/mimc.html)申请
     * 其中appKey和appSecret不可存储于APP端，应存储于APP自己的服务器，以防泄漏。
     *
     * 此处appId/appKey/appSec为小米MimcDemo所有，会在一定时间后失效，建议开发者自行申请
     **/
    public static void init(Application application, long appId, String appKey, String appSecret, String regionKey, String domain) {
        sApplication = application;

        sAppId = appId;
        sAppKey = appKey;
        sAppSecret = appSecret;
        sRegionKey = regionKey;
        sDomain = domain;
    }


}
