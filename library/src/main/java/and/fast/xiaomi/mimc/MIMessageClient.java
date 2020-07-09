package and.fast.xiaomi.mimc;

import android.app.Application;

public class MIMessageClient {

    private MIMessageClient(){
        // 禁止手动创建对象
    }

    public static Application sApplication;

    public static void init(Application application){
        sApplication = application;
    }

//    public static void register(){
//
//    }




}
