package pk.wei.com.gserver.Common;


import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

public class ContextHolder {
    private static Context context;

    public static Context getContext() {
        try {
            // 魔法获得 Context
            Class<?> aClass = Class.forName("android.app.ActivityThread");
            Method method = aClass.getMethod("currentApplication");
            Application applications = (Application) method.invoke(null, null);
            context = applications.getApplicationContext();
            return context;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 魔法获得 Context
            Class<?> aClass = Class.forName("android.app.AppGlobals");
            Method method = aClass.getMethod("getInitialApplication");
            Application applications = (Application) method.invoke(null, null);
            context = applications.getApplicationContext();
            return context;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }
}
