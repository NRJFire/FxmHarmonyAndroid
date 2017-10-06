package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterCreatePostMovies;
import com.sofac.fxmharmony.adapter.AdapterCreatePostPhotos;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.server.Server;
import com.sofac.fxmharmony.server.retrofit.ServiceGenerator;
import com.sofac.fxmharmony.server.retrofit.ServiceRetrofit;
import com.sofac.fxmharmony.server.type.ServerResponse;
import com.sofac.fxmharmony.util.ConvertorHTML;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
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
import static com.sofac.fxmharmony.R.id.idButtonDeleting;


public class CreatePost extends BaseActivity implements View.OnClickListener {

    private EditText postTextInput;
    public String stringTypeGroup = "membergroup";
    private FloatingActionMenu menuButton;

    public ArrayList<Uri> listPhoto;
    public ArrayList<Uri> listMovies;
    public ArrayList<Uri> listFiles;

    FloatingActionButton buttonAddFiles;
    FloatingActionButton buttonAddMovies;
    FloatingActionButton buttonAddPhotos;

    LinearLayout linearLayoutPhoto;
    LinearLayout linearLayoutMovies;
    LinearLayout linearLayoutFiles;

    RecyclerView recyclerViewPhoto;
    RecyclerView recyclerViewMovie;

    AdapterCreatePostPhotos adapterCreatePostPhotos;
    AdapterCreatePostMovies adapterCreatePostMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_post);

        Intent intent = getIntent();
        stringTypeGroup = intent.getStringExtra(TYPE_GROUP);
        setTitle("Create post (" + stringTypeGroup + ")");

        listPhoto = new ArrayList<>();
        listMovies = new ArrayList<>();
        listFiles = new ArrayList<>();

        ScrollView scrollView = (ScrollView) findViewById(R.id.idScrollViewEditPost);
        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTextInput.setFocusable(true);
            }
        });

        postTextInput = (EditText) findViewById(R.id.post_text_input);
        buttonAddFiles = (FloatingActionButton) findViewById(R.id.buttonAddFiles);
        buttonAddMovies = (FloatingActionButton) findViewById(R.id.buttonAddMovies);
        buttonAddPhotos = (FloatingActionButton) findViewById(R.id.buttonAddPhotos);
        menuButton = (FloatingActionMenu) findViewById(R.id.idMenuButton);

        linearLayoutPhoto = (LinearLayout) findViewById(R.id.idLayoutPhotos);
        linearLayoutMovies = (LinearLayout) findViewById(R.id.idLayoutMovies);
        linearLayoutFiles = (LinearLayout) findViewById(R.id.idLayoutFiles);

        recyclerViewPhoto = (RecyclerView) findViewById(R.id.idListPhotos);
        recyclerViewMovie = (RecyclerView) findViewById(R.id.idListMovies);

        buttonAddFiles.setOnClickListener(this);
        buttonAddMovies.setOnClickListener(this);
        buttonAddPhotos.setOnClickListener(this);
        menuButton.setClosedOnTouchOutside(true);

        adapterCreatePostPhotos = new AdapterCreatePostPhotos(listPhoto);
        adapterCreatePostPhotos.setItemClickListener(new AdapterCreatePostPhotos.ClickListener() {
            @Override
            public void onMyClick(View view, int position) {
                switch (view.getId()) {
                    case idButtonDeleting:
                        listPhoto.remove(position);
                        adapterCreatePostPhotos.notifyDataSetChanged();
                        if (listPhoto.isEmpty()) linearLayoutPhoto.setVisibility(View.GONE);
                        break;
                }
            }
        });
        recyclerViewPhoto.setAdapter(adapterCreatePostPhotos);
        recyclerViewPhoto.setHasFixedSize(true);
        recyclerViewPhoto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterCreatePostMovies = new AdapterCreatePostMovies(listMovies);
        adapterCreatePostMovies.setItemClickListener(new AdapterCreatePostMovies.ClickListener() {
            @Override
            public void onMyClick(View view, int position) {
                switch (view.getId()) {
                    case idButtonDeleting:
                        listMovies.remove(position);
                        adapterCreatePostMovies.notifyDataSetChanged();
                        if (listMovies.isEmpty()) linearLayoutMovies.setVisibility(View.GONE);
                        break;
                }
            }
        });
        recyclerViewMovie.setAdapter(adapterCreatePostMovies);
        recyclerViewMovie.setHasFixedSize(true);
        recyclerViewMovie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_post_button:
                if (!postTextInput.getText().toString().equals("")) {

                    PostDTO postDTO = new PostDTO(0L, appUserID.getID(), "", "", ConvertorHTML.toHTML(postTextInput.getText().toString()), "", "", "", "", "", stringTypeGroup);

                    ArrayList<Uri> arrayListAll = new ArrayList<>();
                    arrayListAll.addAll(listPhoto);
                    arrayListAll.addAll(listMovies);
                    arrayListAll.addAll(listFiles);

                    new Server<String>().createPost(postDTO, arrayListAll, this, new Server.AnswerServerResponse<String>() {
                        @Override
                        public void processFinish(Boolean isSuccess, ServerResponse<String> answerServerResponse) {
                            if (isSuccess) {
                                Intent intent = new Intent(CreatePost.this, NavigationActivity.class);
                                setResult(2, intent);
                                finish();
                                toastFinishTrans();
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
    public void toastFinishTrans() {
        Toast.makeText(this, "Finish creating post!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (data != null) {

                final Uri fileUri = data.getData();

                if (requestCode == REQUEST_TAKE_PHOTO) {

                    for (Uri urlPhoto : listPhoto) {
                        if (fileUri.equals(urlPhoto)) return;
                    }

                    listPhoto.add(fileUri);
                    adapterCreatePostPhotos.notifyDataSetChanged();
                    linearLayoutPhoto.setVisibility(View.VISIBLE);

                } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {

                    for (Uri urlMovie : listMovies) {
                        if (fileUri.equals(urlMovie)) return;
                    }

                    listMovies.add(fileUri);
                    adapterCreatePostMovies.notifyDataSetChanged();
                    linearLayoutMovies.setVisibility(View.VISIBLE);

                } else if (requestCode == REQUEST_TAKE_FILE) {

                    for (Uri urlFiles : listFiles) {
                        if (fileUri.equals(urlFiles)) return;
                    }

                    listFiles.add(fileUri);
                    linearLayoutFiles.addView(createViewFile(fileUri));
                    linearLayoutFiles.setVisibility(View.VISIBLE);

                }

            }

        }

    }


    public View createViewFile(final Uri fileUri) {
        final View view = getLayoutInflater().inflate(R.layout.item_file_create_post, null);

        Cursor returnCursor = getContentResolver().query(fileUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        ((TextView) view.findViewById(R.id.idTextFile)).setText(returnCursor.getString(nameIndex));
        Long sizeFile = returnCursor.getLong(sizeIndex);
        if (sizeFile > 1024L) sizeFile = sizeFile / 1024L;
        ((TextView) view.findViewById(R.id.idSizeFile)).setText(sizeFile.toString() + " KB");

        (view.findViewById(R.id.idButtonDeleting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutFiles.removeView(view);
                listFiles.remove(fileUri);
                if (listFiles.isEmpty()) linearLayoutFiles.setVisibility(View.GONE);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddFiles:
                if (isStoragePermissionGranted()) {
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
                    menuButton.toggle(true);
                }
                break;
            case R.id.buttonAddPhotos:
                if (isStoragePermissionGranted()) {
                    Intent takePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    takePhotoIntent.setType("image/*");
                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                    menuButton.toggle(true);
                }
                break;
            case R.id.buttonAddMovies:
                if (isStoragePermissionGranted()) {
                    Intent takeVideoIntent = new Intent();
                    takeVideoIntent.setType("video/*");
                    takeVideoIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(takeVideoIntent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                    menuButton.toggle(true);
                }
                break;
        }
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


}



