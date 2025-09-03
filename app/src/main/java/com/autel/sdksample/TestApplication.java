package com.autel.sdksample;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.SDKConfig;
import com.autel.common.error.AutelError;
import com.autel.internal.DeviceTypeManager;
import com.autel.internal.sdk.util.AutelDirPathUtils;
import com.autel.sdk.Autel;
import com.autel.sdk.AutelSdkConfig;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdksample.evo.mission.util.AutelConfigManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestApplication extends Application {
    private final String TAG = getClass().getSimpleName();
    private BaseProduct currentProduct;

    private static final long MAX_TIME = 1000 * 60 * 60 * 24 * 2; // two days
    private static  String LOG_PATH;

    public void onCreate() {
        super.onCreate();
        Log.v("connectDebug", "TestApplication onCreate ");
        initXlog();
        /**
         * 初始化SDK，通过网络验证APPKey的有效性
         */
        Thread.setDefaultUncaughtExceptionHandler(new EHandle(Thread.getDefaultUncaughtExceptionHandler()));
        String appKey = "<SDK license should be input>";
        AutelSdkConfig config = new AutelSdkConfig.AutelSdkConfigBuilder()
                .setAppKey(appKey)
                .setPostOnUi(true)
                .create();
        DeviceTypeManager.getInstance().setAdvance(true);
        SDKConfig.setDevice(SDKConfig.DEVICE_ADVANCE);
        Autel.init(this, config, new CallbackWithNoParam() {
            @Override
            public void onSuccess() {
                Log.v(TAG, "checkAppKeyValidate onSuccess");
            }

            @Override
            public void onFailure(AutelError error) {
                Log.v(TAG, "checkAppKeyValidate " + error.getDescription());
            }
        });

        AutelConfigManager.instance();
        AutelConfigManager.init(this);
    }

    public BaseProduct getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(BaseProduct baseProduct) {
        currentProduct = baseProduct;
    }

    public class EHandle implements Thread.UncaughtExceptionHandler {
        final Thread.UncaughtExceptionHandler defaultExceptionHandler;

        public EHandle(Thread.UncaughtExceptionHandler defaultExceptionHandler) {
            this.defaultExceptionHandler = defaultExceptionHandler;
        }

        @Override
        public void uncaughtException(Thread thread, final Throwable ex) {
            new ExceptionWriter(ex, getApplicationContext()).saveStackTraceToSD();
            defaultExceptionHandler.uncaughtException(thread, ex);
        }
    }

    public static class ExceptionWriter {
        private final Throwable exception;
        private final Context mContext;

        public ExceptionWriter(Throwable ex, Context c) {
            this.exception = ex;
            this.mContext = c;
        }

        public void saveStackTraceToSD() {
            try {
                PackageManager packageManager = mContext.getPackageManager();
                String appId = null;
                String appVersion = null;
                try {
                    appId = mContext.getPackageName();
                    PackageInfo packageInfo = packageManager.getPackageInfo(appId, 0);
                    appVersion = packageInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                FileOutputStream excep = this.getExceptionFileStream();
                StringBuilder sb = new StringBuilder();
                sb.append("android sdk version = ").append(Build.VERSION.SDK_INT).append("\n");
                sb.append("phoneType = ").append(Build.MODEL).append("\n");
                sb.append(appId).append(" ").append(appVersion).append("\n");
                sb.append("error occured Time = ").append(getTimeStamp()).append("\n\n");
                StringWriter writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                this.exception.printStackTrace(printWriter);

                for (Throwable cause = this.exception.getCause(); cause != null; cause = cause.getCause()) {
                    cause.printStackTrace(printWriter);
                }

                printWriter.close();
                String result = writer.toString();
                sb.append(result);

                try {
                    if (Environment.getExternalStorageState().equals("mounted")) {
                        excep.write(sb.toString().getBytes());
                        excep.close();
                    }
                } catch (Exception var8) {
                }
            } catch (Exception var9) {
                var9.printStackTrace();
            }

        }

        private FileOutputStream getExceptionFileStream() throws FileNotFoundException {
            File myDir = new File(AutelDirPathUtils.getLogCatPath());
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            myDir.mkdirs();
            File file = new File(myDir, getTimeStampForFileName() + ".txt");
            if (file.exists()) {
                file.delete();
            }

            return new FileOutputStream(file);
        }
    }

    public static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.US);
        return sdf.format(new Date());
    }

    public static String getTimeStampForFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
        return sdf.format(new Date());
    }

    /**
     * Initialize XLog.
     */
    private void initXlog() {
        LOG_PATH = "sdcard/maxlink/";//AutelDirPathUtils.getLogCatPath();
    }
}
