package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.SettingsExchangeOnServer;
import com.sofac.fxmharmony.service.BackgroundFileUploadService;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.util.PermissionManager;
import com.sofac.fxmharmony.util.RequestMethods;
import com.sofac.fxmharmony.view.fragmentDialog.ChangeLanguageFragmentDialog;
import com.sofac.fxmharmony.view.fragmentDialog.ChangeNameFragmentDialog;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.AVATAR_IMAGE_SIZE;
import static com.sofac.fxmharmony.Constants.DELETE_AVATAR_REQUEST;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;

public class SettingsActivity extends BaseActivity implements DialogInterface.OnDismissListener {

    private ImageView avatarImage;
    private PopupMenu photoMenu;

    private TextView userName;
    private TextView userPosition;

    private LinearLayout languageButton;
    private TextView currentLanguage;
    private Switch pushMessageSwitch;

    private static final int PICK_PHOTO_FOR_AVATAR = 101;
    private static final int MAKE_PHOTO_FOR_AVATAR = 102;
    private Uri imageFileUri;


    private ChangeNameFragmentDialog changeNameFragmentDialog;
    private ChangeLanguageFragmentDialog changeLanguageFragmentDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initUI();

        setTitle(getString(R.string.title_activity_settings));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        AppMethods.putAvatarIntoImageView(this, avatarImage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        userName.setText(AppMethods.getUserName(this));
        userPosition.setText("Manager"); //temp

        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoMenu.show();
            }
        });

        photoMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (!PermissionManager.checkPermissionGranted(SettingsActivity.this, PermissionManager.REQUEST_CAMERA) || !PermissionManager.checkPermissionGranted(SettingsActivity.this, PermissionManager.REQUEST_STORAGE)) {
                    PermissionManager.verifyCameraPermissions(SettingsActivity.this);
                    PermissionManager.verifyStoragePermissions(SettingsActivity.this);
                    return true;
                } else {

                    if (id == R.id.photo_camera) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageFileUri = Uri.fromFile(AppMethods.getOutputMediaFile());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                        startActivityForResult(intent, MAKE_PHOTO_FOR_AVATAR);

                        return true;
                    }


                    if (id == R.id.photo_gallery) {

                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
                        return true;
                    }
                    if (id == R.id.photo_delete) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle(R.string.delete_photo_question);
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                new SettingsExchangeOnServer<Long>(AppMethods.getUserId(SettingsActivity.this), DELETE_AVATAR_REQUEST, SettingsActivity.this, new SettingsExchangeOnServer.SettingsAsyncResponse() {
                                    @Override
                                    public void processFinish(Boolean isSuccess) {
                                        Glide.with(SettingsActivity.this)
                                                .load(R.drawable.no_image)
                                                .override(AVATAR_IMAGE_SIZE, AVATAR_IMAGE_SIZE)
                                                .centerCrop()
                                                .into(avatarImage);

                                    }
                                }).execute();

                            }
                        });
                        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                }
                return onOptionsItemSelected(item);
            }
        });

        pushMessageSwitch.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (AppMethods.getPushState(SettingsActivity.this) != Constants.PUSH_ON) {
                    pushMessageSwitch.setChecked(true);
                } else {
                    pushMessageSwitch.setChecked(false);
                }
                AppMethods.changePushState(SettingsActivity.this);
            }
        });
        if (AppMethods.getPushState(this) == Constants.PUSH_ON)

        {
            pushMessageSwitch.setChecked(true);
        } else

        {
            pushMessageSwitch.setChecked(false);
        }

        languageButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                changeLanguageFragmentDialog.show(getFragmentManager().beginTransaction(), "ChangeLanguageFragmentDialog");
            }
        });

        currentLanguage.setText(Locale.getDefault().

                getLanguage());

    }

    public void initUI() {
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        photoMenu = new PopupMenu(SettingsActivity.this, avatarImage);
        MenuInflater inflater = photoMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_settings_photo, photoMenu.getMenu());
        userName = (TextView) findViewById(R.id.userName);
        userPosition = (TextView) findViewById(R.id.userPosition);
        pushMessageSwitch = (Switch) findViewById(R.id.pushMessagesSwitch);
        languageButton = (LinearLayout) findViewById(R.id.languageButton);
        currentLanguage = (TextView) findViewById(R.id.currentLanguage);
        changeNameFragmentDialog = ChangeNameFragmentDialog.newInstance();
        changeLanguageFragmentDialog = ChangeLanguageFragmentDialog.newInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_PHOTO_FOR_AVATAR) {
                if (data != null) {
                    imageFileUri = data.getData();
                } else {
                    return;
                }
            }


            Log.i("TEST", imageFileUri.getPath());

            //RequestMethods.startServiceAttachLoadAvatarToUser(this, imageFileUri);


            Glide.with(SettingsActivity.this)
                    .load(imageFileUri)
                    .error(R.drawable.no_avatar)
                    .override(AVATAR_IMAGE_SIZE, AVATAR_IMAGE_SIZE)
                    .centerCrop()
                    .into(avatarImage);


        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        userName.setText(AppMethods.getUserName(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.editName) {
            changeNameFragmentDialog.show(getFragmentManager().beginTransaction(), "ChangeNameFragmentDialog");
            return true;
        }

        if (id == R.id.logout) {

            Intent intentSplashActivity = new Intent(this, SplashActivity.class);
            SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(IS_AUTHORIZATION, false);
            editor.apply();
            intentSplashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            editor.commit();
            startActivity(intentSplashActivity);
            Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
