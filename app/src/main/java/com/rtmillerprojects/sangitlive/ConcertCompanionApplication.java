package com.rtmillerprojects.sangitlive;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Ryan on 10/2/2016.
 */

public class ConcertCompanionApplication extends Application {

    private static ConcertCompanionApplication singleton;
    private static final String TAG = "ConcertCompanionApplication";

    public static String versionName;
    public static int versionCode;

    public static ConcertCompanionApplication getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        versionName = getApplicationVersionName();
        versionCode = getApplicationVersionCode();


        if (BuildConfig.DEBUG) {
            Picasso.with(this).setIndicatorsEnabled(true);
            Picasso.with(this).setLoggingEnabled(Boolean.valueOf(getString(R.string.picasso_logging_enabled)));
        }

        /* Turn on StrictMode for Development */
        new StrictModeHelper().setupStrictMode();

        /* Initialize GoogleAnalytics */
        //GoogleAnalyticsHelper.initialize(this);

        /* Initialize our SharedPreferences Singleton */
        //SharedPreferencesHelper.initialize(this);

        /* Initialize our Database Helper */
        //DBManager.getHelper(this);

        registerHandlersWithEventBus();
    }

    /**
     * Register all the Handlers with the EventBus singleton.
     */
    private void registerHandlersWithEventBus() {
        // Register all our Handlers on the EventBus.
        //EventBus.register(new ServiceEventHandler(this));
    }

    /**
     * Get the application version code as stored in the manifest or build.gradle file.
     *
     * @return the application version code as an int.
     */
    public int getApplicationVersionCode() {
        try {
            return this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Get the application version name and append a "d" if we're using a developer build.
     *
     * @return the application version name as a String.
     */
    public String getApplicationVersionName() {
        //String devBuild = BuildConfig.DEBUG ? "d" : "";
        try {
            return this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;// + devBuild;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public class StrictModeHelper {

        public final String TAG = "StrictModeHelper";

        public void setupStrictMode() {
            if (shouldEnableStrictMode()) {
                Log.wtf(TAG, "*** Strict Mode Enforced ***");
                enableStrictMode();
            } else {
                Log.wtf(TAG, "*** Strict Mode NOT Enforced ***");
            }
        }

        private boolean shouldEnableStrictMode() {
            return BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
        }

        private void enableStrictMode() {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    //.penaltyDeath()  // No need to go to this extreme all the time
                    .build());

        }
    }
}
