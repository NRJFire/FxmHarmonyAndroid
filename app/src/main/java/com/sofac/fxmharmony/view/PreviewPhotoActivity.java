package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.util.FileLoadingListener;
import com.sofac.fxmharmony.util.FileLoadingTask;
import java.io.File;
import java.lang.annotation.Target;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.LINK_IMAGE;
import static com.sofac.fxmharmony.Constants.NAME_IMAGE;

public class PreviewPhotoActivity extends BaseActivity {
    public String linkImage;
    public String nameImage;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);
        Intent intent = getIntent();
        linkImage = intent.getStringExtra(LINK_IMAGE);
        nameImage = intent.getStringExtra(NAME_IMAGE);

        setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Timber.e(linkImage);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this)
                .load(linkImage)
                .error(R.drawable.no_image)
                .into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview_photo_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idDownloadPhoto:
                new FileLoadingTask(
                        linkImage,
                        new File(Environment.getExternalStorageDirectory() + "/Download/" + nameImage),
                        new FileLoadingListener() {
                            @Override
                            public void onBegin() {
                                Toast.makeText(PreviewPhotoActivity.this, "Begin download", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess() {
                                Toast.makeText(PreviewPhotoActivity.this, "Successful download", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Throwable cause) {
                                Toast.makeText(PreviewPhotoActivity.this, "Error download", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onEnd() {

                            }
                        }).execute();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
