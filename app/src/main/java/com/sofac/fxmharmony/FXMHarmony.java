package com.sofac.fxmharmony;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.orm.SchemaGenerator;
import com.orm.SugarApp;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.sofac.fxmharmony.util.FakeCrashLibrary;
import java.util.Locale;
import timber.log.Timber;

/**
 * Created by Maxim on 30.03.2017.
 * Always starting when app is start
 */

public class FXMHarmony  extends SugarApp {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(getApplicationContext());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        // create table if not exists
        SchemaGenerator schemaGenerator = new SchemaGenerator(this);
        schemaGenerator.createDatabase(new SugarDb(this).getDB());

        initLanguage(this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initLanguage(this);
    }


    public static void initLanguage(Context ctx) {

        SharedPreferences sPref = ctx.getSharedPreferences("Locale", ctx.MODE_PRIVATE);
        String language = sPref.getString("Locale", "");

        if (!language.equals("")) {
            Locale mNewLocale = new Locale(language);
            Locale.setDefault(mNewLocale);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.locale = mNewLocale;
            ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());

        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }


    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }

}


