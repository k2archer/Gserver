package pk.wei.com.gserver.Common;

import android.app.Application;
import android.content.Context;


public class BaseApplication extends Application {

    private static Context context;

    public BaseApplication() {
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        BaseApplication.context = context;
    }
}
