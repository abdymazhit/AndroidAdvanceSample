package com.autel.sdksample.evo.mission.util;


import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by A16343 on 2016/7/5.
 */
@SuppressWarnings("DefaultFileTemplate")
public class AutelConfigManager {

    private AutelConfigManager() {

    }

    private static final class AutelConfigManagerHolder {
        @SuppressLint("StaticFieldLeak")
        static final AutelConfigManager autelConfigManager = new AutelConfigManager();
    }

    public static AutelConfigManager instance() {
        return AutelConfigManagerHolder.autelConfigManager;
    }
    private Context mContext;

    private void initManager(Context context) {
        this.mContext = context;
    }

    public static void init(Context context) {
        instance().initManager(context);
    }

    public Context getAppContext() {
        return mContext;
    }

}
