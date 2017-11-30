package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.os.Bundle;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.util.AppPreference;
import com.sofac.fxmharmony.util.CheckVersionApp;

public class SplashActivity extends BaseActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkVersion();
    }

    public void checkVersion() {
        new CheckVersionApp(this, isFinishActivity -> {
            if (isFinishActivity) {
                finishAffinity();
            } else {
                checkAuthorization();
            }
        });
    }

    public void checkAuthorization() {
        if (new AppPreference(this).getAuthorization()) {
            startNavigationActivity();
        } else {
            startLoginActivity();
        }
    }

    public void startLoginActivity() {
        intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startNavigationActivity() {
        intent = new Intent(this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}



