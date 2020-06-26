package and.fast.xiaomi.mimc;

import android.app.Application;

public class MIMCManage {

    public static Application sApplication;

    public static void init(Application application){
        sApplication = application;
    }

    private MIMCManage(){

    }

}
