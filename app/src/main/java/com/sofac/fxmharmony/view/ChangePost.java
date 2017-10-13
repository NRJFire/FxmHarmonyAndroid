package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sofac.fxmharmony.server.type.ServerResponse;
import com.sofac.fxmharmony.util.ConvertorHTML;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.Locale;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.PART_POST;
import static com.sofac.fxmharmony.Constants.POST_ID;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_FILE;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_GALLERY_VIDEO;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_PHOTO;
import static com.sofac.fxmharmony.R.id.idButtonDeleting;


public class ChangePost extends BaseActivity implements View.OnClickListener {

    private PostDTO postDTO;

    private EditText postTextInput;
    public String stringTypeGroup = "membergroup";
    private FloatingActionMenu menuButton;

    public ArrayList<Uri> listPhoto;
    public ArrayList<Uri> listMovies;
    public ArrayList<Uri> listFiles;
    public ArrayList<String> listDeleting;

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
        setTitle(getString(R.string.change_post_activity_name));
        postTextInput = (EditText) findViewById(R.id.post_text_input);

        postDTO = PostDTO.findById(PostDTO.class, getIntent().getLongExtra(POST_ID, 0));
        Timber.e(postDTO.toString());

        listPhoto = new ArrayList<>();
        listMovies = new ArrayList<>();
        listFiles = new ArrayList<>();
        listDeleting = new ArrayList<>();

        ScrollView scrollView = (ScrollView) findViewById(R.id.idScrollViewEditPost);
        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTextInput.setFocusable(true);
            }
        });


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

        if (postDTO != null) {
            postTextInput.setText(ConvertorHTML.fromHTML(postDTO.getBody_original()));

            if (postDTO.getImages() != null && !postDTO.getImages().isEmpty()) {
                for (String uriString : postDTO.getImages()) {
                    listPhoto.add(Uri.parse(BASE_URL + PART_POST + uriString));
                }
                linearLayoutPhoto.setVisibility(View.VISIBLE);
            }

            if (postDTO.getMovies() != null && !postDTO.getMovies().isEmpty()) {
                for (String uriString : postDTO.getMovies()) {
                    listMovies.add(Uri.parse(BASE_URL + PART_POST + uriString));

                }
                linearLayoutMovies.setVisibility(View.VISIBLE);
            }

            if (postDTO.getDocs() != null && !postDTO.getDocs().isEmpty()) {
                for (String nameFile : postDTO.getDocs()) {
                    linearLayoutFiles.addView(createViewFileFromURL(nameFile));
                }
                linearLayoutFiles.setVisibility(View.VISIBLE);
            }
        }

        adapterCreatePostPhotos = new AdapterCreatePostPhotos(listPhoto);
        adapterCreatePostPhotos.setItemClickListener(new AdapterCreatePostPhotos.ClickListener() {
            @Override
            public void onMyClick(View view, int position) {
                switch (view.getId()) {
                    case idButtonDeleting:
                        if (listPhoto.get(position).toString().contains("http://"))
                            listDeleting.add(FilenameUtils.getName(listPhoto.get(position).getPath()));
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
                        if (listMovies.get(position).toString().contains("http://"))
                            listDeleting.add(FilenameUtils.getName(listMovies.get(position).getPath()));
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
                    progressBar.showView();
                    PostDTO newPostDTO = new PostDTO(postDTO.getId(), postDTO.getUser_id(), "", "", ConvertorHTML.toHTML(postTextInput.getText().toString()), postDTO.getBody_ru(), postDTO.getBody_en(), postDTO.getBody_ko(), "", "", postDTO.getType());

                    ArrayList<Uri> arrayListAll = new ArrayList<>();
                    for (Uri uriPhoto : listPhoto) {
                        if (!uriPhoto.toString().contains("http://")) arrayListAll.add(uriPhoto);
                    }
                    for (Uri uriMovies : listMovies) {
                        if (!uriMovies.toString().contains("http://")) arrayListAll.add(uriMovies);
                    }

                    arrayListAll.addAll(listFiles);

                    new Server<String>().updatePost(this, newPostDTO, arrayListAll, listDeleting, new Server.AnswerServerResponse<String>() {
                        @Override
                        public void processFinish(Boolean isSuccess, ServerResponse<String> answerServerResponse) {
                            if (isSuccess) {
                                Intent intent = new Intent(ChangePost.this, NavigationActivity.class);
                                setResult(2, intent);
                                finish();
                                toastFinishTrans();
                            } else {
                                if (answerServerResponse != null) {
                                    Timber.e(answerServerResponse.toString());
                                } else {
                                    Timber.e("SOME PROBLEM TO REQUEST ANSWER : null = answerServerResponse,       on up, check the log");
                                }
                                toastCantCreatePost();
                            }
                            progressBar.dismissView();
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
        Toast.makeText(this, "Some problem with editing post!", Toast.LENGTH_SHORT).show();
    }

    public void toastFinishTrans() {
        Toast.makeText(this, "Finished edit post!", Toast.LENGTH_SHORT).show();
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
                    linearLayoutFiles.addView(createViewFileFromContent(fileUri));
                    linearLayoutFiles.setVisibility(View.VISIBLE);

                }

            }

        }

    }

    public View createViewFileFromURL(final String nameFile) {
        final View view = getLayoutInflater().inflate(R.layout.item_file_create_post, null);

        ((TextView) view.findViewById(R.id.idTextFile)).setText(nameFile);
        ((TextView) view.findViewById(R.id.idSizeFile)).setText("Size file: none");

        (view.findViewById(idButtonDeleting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutFiles.removeView(view); //TODO Нюанс, нельзя убрать вью, не с чего проверить что осталось в файлах (Обдумать)
                listDeleting.add(nameFile);
                //if (listFiles.isEmpty()) linearLayoutFiles.setVisibility(View.GONE);
            }
        });
        return view;
    }

    public View createViewFileFromContent(final Uri fileUri) {
        final View view = getLayoutInflater().inflate(R.layout.item_file_create_post, null);

        Cursor returnCursor = getContentResolver().query(fileUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        ((TextView) view.findViewById(R.id.idTextFile)).setText(returnCursor.getString(nameIndex));
        Long sizeFile = returnCursor.getLong(sizeIndex);
        if (sizeFile > 1024L) sizeFile = sizeFile / 1024L;
        ((TextView) view.findViewById(R.id.idSizeFile)).setText(String.format(Locale.ENGLISH, "Size file: %,d KB", sizeFile));
        (view.findViewById(idButtonDeleting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutFiles.removeView(view);
                listFiles.remove(fileUri);
                //if (listFiles.isEmpty()) linearLayoutFiles.setVisibility(View.GONE);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddFiles:
                if (isStoragePermissionGranted()) {

                    Intent takeFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    takeFileIntent.setType("*/*");
                    startActivityForResult(takeFileIntent, REQUEST_TAKE_FILE);
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
}



