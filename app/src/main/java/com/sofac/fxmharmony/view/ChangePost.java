package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.util.ConvertorHTML;
import com.sofac.fxmharmony.util.FxmPostFile;
import com.sofac.fxmharmony.util.PermissionManager;
import com.sofac.fxmharmony.util.RequestMethods;
import com.sofac.fxmharmony.view.customView.FileItemWithCancel;
import com.sofac.fxmharmony.view.customView.LinearLayoutGallery;
import com.sofac.fxmharmony.view.customView.PhotoVideoItemWithCancel;

import java.util.ArrayList;
import java.util.List;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.GET_POST_FILES_END_URL;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_FILE;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_GALLERY_VIDEO;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_PHOTO;
import static com.sofac.fxmharmony.Constants.UPDATE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;


public class ChangePost extends BaseActivity {

    public SharedPreferences preferences;
    private EditText postTextInput;
    private PostDTO postDTO;
    private BottomNavigationView bottomNavigationView;

    private LinearLayoutGallery imagesGalleryLayout;
    private LinearLayoutGallery videoGalleryLayout;
    private LinearLayoutGallery fileGalleryLayout;

    private FxmPostFile fxmPostFile;

    private List<String> videoList;
    private List<String> imageList;
    private List<String> fileList;

    private List<Uri> fileListToSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_post);
        initUI();


        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        Intent intent = getIntent();
        postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);
        fxmPostFile = new FxmPostFile(postDTO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            postTextInput.setText(Html.fromHtml(postDTO.getPostTextOriginal() , Html.FROM_HTML_MODE_LEGACY));
        } else {
            postTextInput.setText(Html.fromHtml(postDTO.getPostTextOriginal()));
        }


        videoList = fxmPostFile.getVideoList();
        imageList = fxmPostFile.getImageList();
        fileList = fxmPostFile.getFileList();

        fileListToSend = new ArrayList<>();

        for (String imgName : imageList) {
            imagesGalleryLayout.addView(new PhotoVideoItemWithCancel(this, Uri.parse(BASE_URL + GET_POST_FILES_END_URL + imgName), imageList, fileListToSend, false));
        }
        for (String videoName : videoList) {
            videoGalleryLayout.addView(new PhotoVideoItemWithCancel(this, Uri.parse(BASE_URL + GET_POST_FILES_END_URL + videoName), videoList, fileListToSend , true));
        }
        for (String fileName : fileList) {
            fileGalleryLayout.addView(new FileItemWithCancel(this, Uri.parse(BASE_URL + GET_POST_FILES_END_URL + fileName), fileList, fileListToSend));
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        if (!PermissionManager.checkPermissionGranted(ChangePost.this, PermissionManager.REQUEST_CAMERA) || !PermissionManager.checkPermissionGranted(ChangePost.this, PermissionManager.REQUEST_STORAGE)) {
                            PermissionManager.verifyCameraPermissions(ChangePost.this);
                            PermissionManager.verifyStoragePermissions(ChangePost.this);
                            return false;
                        } else {

                            switch (item.getItemId()) {

                                case R.id.action_take_photo:

                                    Intent takePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    takePhotoIntent.setType("image/*");
                                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                                    return false;


                                case R.id.action_take_video:

                                    Intent takeVideoIntent = new Intent();
                                    takeVideoIntent.setType("video/*");
                                    takeVideoIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(takeVideoIntent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                                    return false;


                                case R.id.action_take_file:

                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("*/*");
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                                    // special intent for Samsung file manager
                                    Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                                    // if you want any file type, you can skip next line
                                    sIntent.putExtra("CONTENT_TYPE", "*/*");
                                    sIntent.addCategory(Intent.CATEGORY_DEFAULT);

                                    Intent chooserIntent;
                                    if (getPackageManager().resolveActivity(sIntent, 0) != null){
                                        // it is device with samsung file manager
                                        chooserIntent = Intent.createChooser(sIntent, "Open file");
                                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
                                    }
                                    else {
                                        chooserIntent = Intent.createChooser(intent, "Open file");
                                    }

                                    try {
                                        startActivityForResult(chooserIntent, REQUEST_TAKE_FILE);
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
                                    }

                                    return false;

                                case R.id.add_files:

                                    showFileUI();
                                    return true;

                            }
                            return true;
                        }
                    }
                });


        hideFileUI();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_post, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            Log.i("CODE", "" + requestCode);

            if (data != null) {

                Uri fileUri = data.getData();

                for (Uri uriToSend : fileListToSend) {
                    if (fileUri.equals(uriToSend)) return;
                }
                fileListToSend.add(fileUri);

                if (requestCode == REQUEST_TAKE_PHOTO) {

                    PhotoVideoItemWithCancel photoVideoItemWithCancel = new PhotoVideoItemWithCancel(this, fileUri, imageList, fileListToSend , false);
                    imagesGalleryLayout.addView(photoVideoItemWithCancel);

                } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {

                    PhotoVideoItemWithCancel photoVideoItemWithCancel = new PhotoVideoItemWithCancel(this, fileUri, videoList, fileListToSend , false);
                    videoGalleryLayout.addView(photoVideoItemWithCancel);

                } else if (requestCode == REQUEST_TAKE_FILE) {

                    FileItemWithCancel fileItemWithCancel = new FileItemWithCancel(this, fileUri, fileList, fileListToSend);
                    fileGalleryLayout.addView(fileItemWithCancel);

                }
            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_post_button:
                if (!postTextInput.getText().toString().equals("")) {

                    postDTO.setPostTextOriginal(postTextInput.getText().toString());

                    String images = "";
                    String videos = "";
                    String files = "";

                    for (String image : imageList) {
                        images += image + ";#";
                    }
                    for (String video : videoList) {
                        videos += video + ";#";
                    }
                    for (String file : fileList) {
                        files += file + ";#";
                    }


                    final PostDTO postDTOtoSend =
                            new PostDTO(
                                    postDTO.getId(),
                                    postDTO.getServerID(),
                                    preferences.getLong(USER_ID_PREF, 0L),
                                    postDTO.getUserName(),
                                    postDTO.getDate(),
                                    ConvertorHTML.toHTML(postDTO.getPostTextOriginal()),
                                    ConvertorHTML.toHTML(postDTO.getPostTextRu()),
                                    ConvertorHTML.toHTML(postDTO.getPostTextEn()),
                                    ConvertorHTML.toHTML(postDTO.getPostTextKo()),
                                    files,
                                    videos,
                                    images,
                                    postDTO.getPostUserAvatarImage(),
                                    postDTO.getGroupType());
                    new GroupExchangeOnServer<PostDTO>(
                            new PostDTO(
                                    postDTO.getId(),
                                    postDTO.getServerID(),
                                    preferences.getLong(USER_ID_PREF, 0L),
                                    postDTO.getUserName(),
                                    null,
                                    ConvertorHTML.toHTML(postDTO.getPostTextOriginal()),
                                    ConvertorHTML.toHTML(postDTO.getPostTextRu()),
                                    ConvertorHTML.toHTML(postDTO.getPostTextEn()),
                                    ConvertorHTML.toHTML(postDTO.getPostTextKo()),
                                    files,
                                    videos,
                                    images,
                                    null,
                                    postDTO.getGroupType()),
                            true,
                            UPDATE_POST_REQUEST,
                            this,
                            new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                        @Override
                        public void processFinish(Boolean isSuccess , String answer) {
                            if (isSuccess) {
                                if (fileListToSend.size() > 0) {
                                    RequestMethods.startServiceAttachLoadFilesToPost(ChangePost.this, (ArrayList<Uri>) fileListToSend, postDTO.getServerID());
                                }

                                Intent intentDetailPost = new Intent(ChangePost.this, DetailPostActivity.class);
                                intentDetailPost.putExtra(ONE_POST_DATA, postDTOtoSend);

                                setResult(2, intentDetailPost);
                                finish();
                            }
                        }
                    }).execute();


                } else {
                    Toast.makeText(this, "Please input text post", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void initUI() {
        setTitle(getString(R.string.change_post_activity_name));
        postTextInput = (EditText) findViewById(R.id.post_text_input);
        imagesGalleryLayout = (LinearLayoutGallery) findViewById(R.id.image_gallery_layout);
        imagesGalleryLayout.setGalleryView((TextView) findViewById(R.id.image_gallery_name));
        videoGalleryLayout = (LinearLayoutGallery) findViewById(R.id.video_gallery_layout);
        videoGalleryLayout.setGalleryView((TextView) findViewById(R.id.video_gallery_name));
        fileGalleryLayout = (LinearLayoutGallery) findViewById(R.id.file_gallery_layout);
        fileGalleryLayout.setGalleryView((TextView) findViewById(R.id.file_gallery_name));
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

    }

    private void hideFileUI() {

        bottomNavigationView.findViewById(R.id.action_take_photo).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.action_take_video).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.action_take_file).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.add_files).setVisibility(View.VISIBLE);

    }

    private void showFileUI() {

        bottomNavigationView.findViewById(R.id.action_take_photo).setVisibility(View.VISIBLE);
        bottomNavigationView.findViewById(R.id.action_take_video).setVisibility(View.VISIBLE);
        bottomNavigationView.findViewById(R.id.action_take_file).setVisibility(View.VISIBLE);
        bottomNavigationView.findViewById(R.id.add_files).setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



