package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterCreatePostMovies;
import com.sofac.fxmharmony.adapter.AdapterCreatePostPhotos;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.server.Connection;
import com.sofac.fxmharmony.util.ConvertorHTML;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.PART_POST;
import static com.sofac.fxmharmony.Constants.POST_ID;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_FILE;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_GALLERY_VIDEO;
import static com.sofac.fxmharmony.Constants.REQUEST_TAKE_PHOTO;
import static com.sofac.fxmharmony.R.id.idButtonDeleting;


public class ChangePost extends BaseActivity {


    private PostDTO postDTO;
    private FloatingActionMenu menuButton;

    public ArrayList<Uri> listPhoto = new ArrayList<>();
    public ArrayList<Uri> listMovies = new ArrayList<>();
    public ArrayList<Uri> listFiles = new ArrayList<>();
    public ArrayList<String> listDeleting = new ArrayList<>();

    AdapterCreatePostPhotos adapterCreatePostPhotos;
    AdapterCreatePostMovies adapterCreatePostMovies;

    @BindView(R.id.post_text_input)
    EditText postTextInput;

    @BindView(R.id.idListPhotos)
    RecyclerView idListPhotos;

    @BindView(R.id.idListMovies)
    RecyclerView idListMovies;

    @BindView(R.id.idLayoutPhotos)
    LinearLayout idLayoutPhotos;

    @BindView(R.id.idLayoutMovies)
    LinearLayout idLayoutMovies;

    @BindView(R.id.idLayoutFiles)
    LinearLayout idLayoutFiles;

    @BindView(R.id.idMenuButton)
    FloatingActionMenu idMenuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_post);
        ButterKnife.bind(this);

        setTitle(getString(R.string.change_post_activity_name));

        postDTO = PostDTO.findById(PostDTO.class, getIntent().getLongExtra(POST_ID, 0));
        Timber.e(postDTO.toString());


        idMenuButton.setClosedOnTouchOutside(true);

        if (postDTO != null) {
            postTextInput.setText(ConvertorHTML.fromHTML(postDTO.getBody_original()));

            if (postDTO.getImages() != null && !postDTO.getImages().isEmpty()) {
                for (String uriString : postDTO.getImages()) {
                    listPhoto.add(Uri.parse(BASE_URL + PART_POST + uriString));
                }
                idLayoutPhotos.setVisibility(View.VISIBLE);
            }

            if (postDTO.getMovies() != null && !postDTO.getMovies().isEmpty()) {
                for (String uriString : postDTO.getMovies()) {
                    listMovies.add(Uri.parse(BASE_URL + PART_POST + uriString));

                }
                idLayoutMovies.setVisibility(View.VISIBLE);
            }

            if (postDTO.getDocs() != null && !postDTO.getDocs().isEmpty()) {
                for (String nameFile : postDTO.getDocs()) {
                    idLayoutFiles.addView(createViewFileFromURL(nameFile));
                }
                idLayoutFiles.setVisibility(View.VISIBLE);
            }
        }

        adapterCreatePostPhotos = new AdapterCreatePostPhotos(listPhoto);
        adapterCreatePostPhotos.setItemClickListener((view, position) -> {
            switch (view.getId()) {
                case idButtonDeleting:
                    if (listPhoto.get(position).toString().contains("http://"))
                        listDeleting.add(FilenameUtils.getName(listPhoto.get(position).getPath()));
                    listPhoto.remove(position);
                    adapterCreatePostPhotos.notifyDataSetChanged();
                    if (listPhoto.isEmpty()) idLayoutPhotos.setVisibility(View.GONE);
                    break;
            }
        });

        idListPhotos.setAdapter(adapterCreatePostPhotos);
        idListPhotos.setHasFixedSize(true);
        idListPhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterCreatePostMovies = new AdapterCreatePostMovies(listMovies);
        adapterCreatePostMovies.setItemClickListener((view, position) -> {
            switch (view.getId()) {
                case idButtonDeleting:
                    if (listMovies.get(position).toString().contains("http://"))
                        listDeleting.add(FilenameUtils.getName(listMovies.get(position).getPath()));
                    listMovies.remove(position);
                    adapterCreatePostMovies.notifyDataSetChanged();
                    if (listMovies.isEmpty()) idLayoutMovies.setVisibility(View.GONE);
                    break;
            }
        });

        idListMovies.setAdapter(adapterCreatePostMovies);
        idListMovies.setHasFixedSize(true);
        idListMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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

                    postDTO.setBody_original(ConvertorHTML.toHTML(postTextInput.getText().toString()));

                    ArrayList<Uri> arrayListAll = new ArrayList<>();
                    for (Uri uriPhoto : listPhoto) {
                        if (!uriPhoto.toString().contains("http://")) arrayListAll.add(uriPhoto);
                    }
                    for (Uri uriMovies : listMovies) {
                        if (!uriMovies.toString().contains("http://")) arrayListAll.add(uriMovies);
                    }

                    arrayListAll.addAll(listFiles);

                    new Connection<String>().updatePost(this, postDTO, arrayListAll, listDeleting, (isSuccess, answerServerResponse) -> {
                        if (isSuccess) {
                            PostDTO.deleteAll(PostDTO.class, "type = ?", postDTO.getType());
                            new Connection<ArrayList<PostDTO>>().getListPosts(postDTO.getType(), (isSuccess1, answerServerResponse1) -> {
                                if (isSuccess1 && answerServerResponse1 != null) {
                                    PostDTO.saveInTx(answerServerResponse1.getDataTransferObject());
                                    Intent intent = new Intent(ChangePost.this, NavigationActivity.class);
                                    setResult(2, intent);
                                    progressBar.dismissView();
                                    finish();
                                    showToast("Finished edit post!");
                                } else {
                                    progressBar.dismissView();
                                }
                            });
                        } else {
                            if (answerServerResponse != null) {
                                Timber.e(answerServerResponse.toString());
                            } else {
                                Timber.e("SOME PROBLEM TO REQUEST ANSWER : null = answerServerResponse,       on up, check the log");
                            }
                            showToast("Some problem with editing post!");
                            progressBar.dismissView();
                        }

                    });

                } else {
                    showToast("Please input text post");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {

            final Uri fileUri = data.getData();

            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    for (Uri urlPhoto : listPhoto) {
                        if (fileUri.equals(urlPhoto)) return;
                    }
                    listPhoto.add(fileUri);
                    adapterCreatePostPhotos.notifyDataSetChanged();
                    idLayoutPhotos.setVisibility(View.VISIBLE);
                    break;

                case REQUEST_TAKE_GALLERY_VIDEO:
                    for (Uri urlMovie : listMovies) {
                        if (fileUri.equals(urlMovie)) return;
                    }

                    listMovies.add(fileUri);
                    adapterCreatePostMovies.notifyDataSetChanged();
                    idLayoutMovies.setVisibility(View.VISIBLE);
                    break;

                case REQUEST_TAKE_FILE:
                    for (Uri urlFiles : listFiles) {
                        if (fileUri.equals(urlFiles)) return;
                    }

                    listFiles.add(fileUri);
                    idLayoutFiles.addView(createViewFileFromContent(fileUri));
                    idLayoutFiles.setVisibility(View.VISIBLE);
                    break;
            }

        }

    }

    public View createViewFileFromURL(final String nameFile) {
        final View view = getLayoutInflater().inflate(R.layout.item_file_create_post, null);

        Uri fileUri = Uri.parse(BASE_URL + PART_POST + nameFile);
        listFiles.add(fileUri);
        ((TextView) view.findViewById(R.id.idTextFile)).setText(nameFile);
        //((TextView) view.findViewById(R.id.idSizeFile)).setText("Size file: none");

        (view.findViewById(idButtonDeleting)).setOnClickListener(v -> {
            idLayoutFiles.removeView(view);
            listDeleting.add(nameFile);
            listFiles.remove(fileUri);
            if (listFiles.isEmpty()) idLayoutFiles.setVisibility(View.GONE);
        });
        return view;
    }

    public View createViewFileFromContent(final Uri fileUri) {
        final View view = getLayoutInflater().inflate(R.layout.item_file_create_post, null);
        ((TextView) view.findViewById(R.id.idTextFile)).setText(FilenameUtils.getName(fileUri.toString()));

        (view.findViewById(idButtonDeleting)).setOnClickListener(v -> {
            idLayoutFiles.removeView(view);
            listFiles.remove(fileUri);
            if (listFiles.isEmpty()) idLayoutFiles.setVisibility(View.GONE);
        });
        return view;
    }

    @OnClick({R.id.buttonAddFiles, R.id.buttonAddPhotos, R.id.buttonAddMovies})
    void onButtonsClick(View v) {

        if (isStoragePermissionGranted()) {
            switch (v.getId()) {
                case R.id.buttonAddFiles:
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
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.buttonAddPhotos:
                    Intent takePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    takePhotoIntent.setType("image/*");
                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                    break;

                case R.id.buttonAddMovies:
                    Intent takeVideoIntent = new Intent();
                    takeVideoIntent.setType("video/*");
                    takeVideoIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(takeVideoIntent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                    break;
            }
            menuButton.toggle(true);
        }
    }


}




