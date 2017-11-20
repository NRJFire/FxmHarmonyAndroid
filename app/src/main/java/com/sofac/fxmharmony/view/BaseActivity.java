package com.sofac.fxmharmony.view;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.sofac.fxmharmony.dto.UserDTO;
import com.sofac.fxmharmony.util.AppUserID;
import com.sofac.fxmharmony.util.ProgressBar;

import static com.orm.SugarRecord.findById;

public class BaseActivity extends AppCompatActivity {

    public AppUserID appUserID;
    public UserDTO userDTO;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar = new ProgressBar(this);
        appUserID = new AppUserID(this);
        userDTO = findById(UserDTO.class, appUserID.getID());
    }


    static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    static String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Boolean IS_PERMISSIONS_STORAGE = false;
    Boolean IS_PERMISSIONS_CAMERA = false;

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                IS_PERMISSIONS_STORAGE = false;
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
                return IS_PERMISSIONS_STORAGE;
            }
        } else { //permission is automatically granted on sdk < 23 upon installation
            return true;
        }
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                IS_PERMISSIONS_CAMERA = false;
                ActivityCompat.requestPermissions(this, PERMISSIONS_CAMERA, 2);
                return IS_PERMISSIONS_CAMERA;
            }
        } else { //permission is automatically granted on sdk < 23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    IS_PERMISSIONS_STORAGE = true;
                }
                break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    IS_PERMISSIONS_CAMERA = true;
                }
                break;
        }
    }



}
