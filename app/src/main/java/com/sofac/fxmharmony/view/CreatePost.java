package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.server.Server;
import com.sofac.fxmharmony.server.retrofit.ServiceRetrofit;
import com.sofac.fxmharmony.server.type.ServerResponse;
import com.sofac.fxmharmony.util.AppUserID;
import com.sofac.fxmharmony.util.ConvertorHTML;
import com.sofac.fxmharmony.util.PathUtil;
import com.sofac.fxmharmony.util.PermissionManager;
import com.sofac.fxmharmony.util.RequestMethods;
import com.sofac.fxmharmony.util.ServiceGenerator;
import com.sofac.fxmharmony.view.customView.FileItemWithCancel;
import com.sofac.fxmharmony.view.customView.LinearLayoutGallery;
import com.sofac.fxmharmony.view.customView.PhotoVideoItemWithCancel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_FILE;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_GALLERY_VIDEO;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_PHOTO;
import static com.sofac.fxmharmony.Constants.TYPE_GROUP;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;
import static com.sofac.fxmharmony.Constants.WRITE_POST_REQUEST;


public class CreatePost extends BaseActivity implements View.OnClickListener {

    public SharedPreferences preferences;
    private EditText postTextInput;
    private BottomNavigationView bottomNavigationView;

    private LinearLayoutGallery imagesGalleryLayout;
    private LinearLayoutGallery videoGalleryLayout;
    private LinearLayoutGallery fileGalleryLayout;

    private List<Uri> fileListToSend;
    public String stringTypeGroup = "membergroup";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_post);
        initUI();

        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        Intent intent = getIntent();
        stringTypeGroup = intent.getStringExtra(TYPE_GROUP);
        setTitle("Create post (" + stringTypeGroup + ")");

        fileListToSend = new ArrayList<>();

//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                        if (!PermissionManager.checkPermissionGranted(CreatePost.this, PermissionManager.REQUEST_CAMERA) || !PermissionManager.checkPermissionGranted(CreatePost.this, PermissionManager.REQUEST_STORAGE)) {
//                            PermissionManager.verifyCameraPermissions(CreatePost.this);
//                            PermissionManager.verifyStoragePermissions(CreatePost.this);
//                            return false;
//                        } else {
//
//                            switch (item.getItemId()) {
//
//                                case R.id.action_take_photo:
//
//                                    Intent takePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                                    takePhotoIntent.setType("image/*");
//                                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
//                                    return false;
//
//
//                                case R.id.action_take_video:
//
//                                    Intent takeVideoIntent = new Intent();
//                                    takeVideoIntent.setType("video/*");
//                                    takeVideoIntent.setAction(Intent.ACTION_GET_CONTENT);
//                                    startActivityForResult(Intent.createChooser(takeVideoIntent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
//                                    return false;
//
//
//                                case R.id.action_take_file:
//
//
//                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                    intent.setType("*/*");
//                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//                                    // special intent for Samsung file manager
//                                    Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
//                                    // if you want any file type, you can skip next line
//                                    sIntent.putExtra("CONTENT_TYPE", "*/*");
//                                    sIntent.addCategory(Intent.CATEGORY_DEFAULT);
//
//                                    Intent chooserIntent;
//                                    if (getPackageManager().resolveActivity(sIntent, 0) != null) {
//                                        // it is device with samsung file manager
//                                        chooserIntent = Intent.createChooser(sIntent, "Open file");
//                                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
//                                    } else {
//                                        chooserIntent = Intent.createChooser(intent, "Open file");
//                                    }
//
//                                    try {
//                                        startActivityForResult(chooserIntent, REQUEST_TAKE_FILE);
//                                    } catch (android.content.ActivityNotFoundException ex) {
//                                        Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
//                                    }
//
//
//                                case R.id.add_files:
//
//                                    showFileUI();
//                                    return true;
//
//                            }
//                            return true;
//                        }
//                    }
//                });
//
//
//        hideFileUI();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }

    Uri fileUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Log.i("CODE", "" + requestCode);

            if (data != null) {

                fileUri = data.getData();
                Timber.e("!!!!!!!!!" + fileUri.toString());

            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_take_file:
                Toast.makeText(this, "!!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // special intent for Samsung file manager
                Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                // if you want any file type, you can skip next line
                sIntent.putExtra("CONTENT_TYPE", "*/*");
                sIntent.addCategory(Intent.CATEGORY_DEFAULT);

                Intent chooserIntent;
                if (getPackageManager().resolveActivity(sIntent, 0) != null) {
                    // it is device with samsung file manager
                    chooserIntent = Intent.createChooser(sIntent, "Open file");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
                } else {
                    chooserIntent = Intent.createChooser(intent, "Open file");
                }

                try {
                    startActivityForResult(chooserIntent, REQUEST_TAKE_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_take_photo:
                if (isStoragePermissionGranted())
                    uploadFile(fileUri);
                break;
        }
    }

    public ArrayList<MultipartBody.Part> generateMultiPartList(ArrayList<Uri> listFileUri) {

        ArrayList<MultipartBody.Part> arrayListMulti = new ArrayList<>();

        for (int i = 0; i < listFileUri.size(); i++) {
            try {
                File file = new File(PathUtil.getPath(this, listFileUri.get(i)));
                arrayListMulti.add(MultipartBody.Part.createFormData(i + "", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file)));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        return arrayListMulti;
    }

    private void uploadFile(Uri fileUri) {

        ServiceRetrofit service = ServiceGenerator.createService(ServiceRetrofit.class);


        ArrayList<Uri> uriArrayList = new ArrayList<>();
        uriArrayList.add(fileUri);
        uriArrayList.add(fileUri);
        uriArrayList.add(fileUri);
        // finally, execute the request
        Call<ResponseBody> call = service.uploadFiles(RequestBody.create(MediaType.parse("text/plain"), "123456789"), generateMultiPartList(uriArrayList));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Upload", "success");
                Timber.e("response.toString() " + response.toString());
                try {
                    Timber.e("response.body().string() " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Timber.e("response.message() = " + response.message());
                Timber.e("response.code() = " + response.code());
                Timber.e("response.errorBody() = " + response.errorBody());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//
//            Log.i("CODE", "" + requestCode);
//
//            if (data != null) {
//
//                Uri fileUri = data.getData();
//
//                for (Uri uriToSend : fileListToSend) {
//                    if (fileUri.equals(uriToSend)) return;
//                }
//                fileListToSend.add(fileUri);
//
//                if (requestCode == REQUEST_TAKE_PHOTO) {
//
//                    PhotoVideoItemWithCancel photoVideoItemWithCancel = new PhotoVideoItemWithCancel(this, fileUri, new ArrayList<String>(), fileListToSend, false);
//                    imagesGalleryLayout.addView(photoVideoItemWithCancel);
//
//                } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
//
//                    PhotoVideoItemWithCancel photoVideoItemWithCancel = new PhotoVideoItemWithCancel(this, fileUri, new ArrayList<String>(), fileListToSend, false);
//                    videoGalleryLayout.addView(photoVideoItemWithCancel);
//
//                } else if (requestCode == REQUEST_TAKE_FILE) {
//
//                    FileItemWithCancel fileItemWithCancel = new FileItemWithCancel(this, fileUri, new ArrayList<String>(), fileListToSend);
//                    fileGalleryLayout.addView(fileItemWithCancel);
//
//                }
//            }
//
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_post_button:
                if (!postTextInput.getText().toString().equals("")) {

                    Editable postText = postTextInput.getText();

//                    new GroupExchangeOnServer<PostDTO>(
//                            new PostDTO(
//                                    1L,
//                                    1L,
//                                    preferences.getLong(USER_ID_PREF, 0L),
//                                    "",
//                                    null,
//                                    ConvertorHTML.toHTML(postText.toString()),
//                                    "",
//                                    "",
//                                    "",
//                                    null,
//                                    null,
//                                    null,
//                                    null,
//                                    stringTypeGroup),
//                            true,
//                            WRITE_POST_REQUEST, this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
//                        @Override
//                        public void processFinish(Boolean isSuccess, String answer) {
//                            if (isSuccess) {
//
//                                Timber.e("answer!!!!!!!!!! " + answer);
//                                Long postID = Long.valueOf(answer);
//
//                                if (fileListToSend.size() > 0) {
        //                            RequestMethods.startServiceAttachLoadFilesToPost(CreatePost.this, (ArrayList<Uri>) fileListToSend, postID);
//                                }
//
//                                Intent intent = new Intent(CreatePost.this, NavigationActivity.class);
//                                setResult(2, intent);
//                                finish();
//                            }
//                        }
//                    }).execute();

                    PostDTO postDTO = new PostDTO(0L, appUserID.getID(), "", "", ConvertorHTML.toHTML(postTextInput.getText().toString()), "", "", "", "", "", stringTypeGroup);
                    new Server<String>().createPost(postDTO, new Server.AnswerServerResponse<String>() {
                        @Override
                        public void processFinish(Boolean isSuccess, ServerResponse<String> answerServerResponse) {
                            if (isSuccess) {
                                Intent intent = new Intent(CreatePost.this, NavigationActivity.class);
                                setResult(2, intent);
                                finish();
                            } else {
                                toastCantCreatePost();
                            }
                        }
                    });

                } else {
                    Toast.makeText(this, "Please input text post", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toastCantCreatePost() {
        Toast.makeText(this, "Some problem with creating post!", Toast.LENGTH_SHORT).show();
    }


    private void initUI() {
        postTextInput = (EditText) findViewById(R.id.post_text_input);
//        imagesGalleryLayout = (LinearLayoutGallery) findViewById(R.id.image_gallery_layout);
//        imagesGalleryLayout.setGalleryView((TextView) findViewById(R.id.image_gallery_name));
//        videoGalleryLayout = (LinearLayoutGallery) findViewById(R.id.video_gallery_layout);
//        videoGalleryLayout.setGalleryView((TextView) findViewById(R.id.video_gallery_name));
//        fileGalleryLayout = (LinearLayoutGallery) findViewById(R.id.file_gallery_layout);
//        fileGalleryLayout.setGalleryView((TextView) findViewById(R.id.file_gallery_name));
//        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

    }
//
//    private void hideFileUI() {
//
//        bottomNavigationView.findViewById(R.id.action_take_photo).setVisibility(View.GONE);
//        bottomNavigationView.findViewById(R.id.action_take_video).setVisibility(View.GONE);
//        bottomNavigationView.findViewById(R.id.action_take_file).setVisibility(View.GONE);
//        bottomNavigationView.findViewById(R.id.add_files).setVisibility(View.VISIBLE);
//
//    }
//
//    private void showFileUI() {
//
//        bottomNavigationView.findViewById(R.id.action_take_photo).setVisibility(View.VISIBLE);
//        bottomNavigationView.findViewById(R.id.action_take_video).setVisibility(View.VISIBLE);
//        bottomNavigationView.findViewById(R.id.action_take_file).setVisibility(View.VISIBLE);
//        bottomNavigationView.findViewById(R.id.add_files).setVisibility(View.GONE);
//
//    }

}



