package com.sofac.fxmharmony.view;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.util.FileLoadingListener;
import com.sofac.fxmharmony.util.FileLoadingTask;
import java.io.File;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import timber.log.Timber;
import static com.sofac.fxmharmony.Constants.LINK_VIDEO;
import static com.sofac.fxmharmony.Constants.NAME_VIDEO;

public class PreviewVideoActivity extends BaseActivity {
    public String linkVideo;
    public String nameVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);

        setTitle("Video");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        linkVideo = intent.getStringExtra(LINK_VIDEO);
        nameVideo = intent.getStringExtra(NAME_VIDEO);
        Timber.e("!!!! linkVideo !!! " + linkVideo);

        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(linkVideo, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, nameVideo);
        //jcVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview_photo_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idDownloadPhoto:
                if(isStoragePermissionGranted()) {
                    new FileLoadingTask(
                            linkVideo,
                            new File(Environment.getExternalStorageDirectory() + "/Download/" + nameVideo),
                            new FileLoadingListener() {
                                @Override
                                public void onBegin() {
                                    Toast.makeText(PreviewVideoActivity.this, "Begin download", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess() {
                                    Toast.makeText(PreviewVideoActivity.this, "Successful download", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Throwable cause) {
                                    Toast.makeText(PreviewVideoActivity.this, "Error download", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onEnd() {

                                }
                            }).execute();
                }
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
