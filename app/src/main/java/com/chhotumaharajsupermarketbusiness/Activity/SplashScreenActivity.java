package com.chhotumaharajsupermarketbusiness.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.chhotumaharajsupermarketbusiness.R;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class SplashScreenActivity extends AppCompatActivity {

    private final static int SPLASH_SCREEN_TIMEOUT = 3000;
    private final static int REQUEST_CODE_APPLICATION_UPDATE = 1736;

    private AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                callNextActivity();

            }
        }, SPLASH_SCREEN_TIMEOUT);

    }

    private void requestUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, REQUEST_CODE_APPLICATION_UPDATE);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == REQUEST_CODE_APPLICATION_UPDATE) {
            if (resultCode == RESULT_OK) {
                callNextActivity();
            } else {
                checkApplicationUpdate();
            }
        }*/
    }

    private void checkApplicationUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                System.out.println("AppUpdate:result.updateAvailability()=" + result.updateAvailability());
                System.out.println("AppUpdate:result.availableVersionCode()=" + result.availableVersionCode());
                System.out.println("AppUpdate:UpdateAvailability.UPDATE_AVAILABLE=" + UpdateAvailability.UPDATE_AVAILABLE);
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    Toast.makeText(getApplicationContext(), "Please Update Application", Toast.LENGTH_LONG).show();
                    requestUpdate(result);
                } else {
                    callNextActivity();
                }
            }
        });
    }

    private void callNextActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}