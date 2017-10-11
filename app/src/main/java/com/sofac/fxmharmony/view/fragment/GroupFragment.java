package com.sofac.fxmharmony.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPostGroup;
import com.sofac.fxmharmony.adapter.RecyclerItemClickListener;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.dto.UserDTO;
import com.sofac.fxmharmony.server.Server;
import com.sofac.fxmharmony.server.type.ServerResponse;
import com.sofac.fxmharmony.util.AppUserID;
import com.sofac.fxmharmony.util.ProgressBar;
import com.sofac.fxmharmony.view.BaseFragment;
import com.sofac.fxmharmony.view.ChangePost;
import com.sofac.fxmharmony.view.CreatePost;
import com.sofac.fxmharmony.view.DetailPostActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;
import static com.orm.SugarRecord.findById;
import static com.sofac.fxmharmony.Constants.POST_ID;
import static com.sofac.fxmharmony.Constants.TYPE_GROUP;

public class GroupFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public Intent intentDetailPostActivity;

    public ArrayList<PostDTO> postDTOs;
    public SwipeRefreshLayout groupSwipeRefreshLayout;
    SharedPreferences preferences;
    public FloatingActionButton floatingActionButton;
    public static Long idPost;
    public PostDTO postDTO;
    public Intent intentChangePost;
    public String stringTypeGroup = "staffgroup";

    public RecyclerView recyclerViewPost;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapterPostGroup;

    public AppUserID appUserID;
    public UserDTO userDTO;
    public ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentDetailPostActivity = new Intent(this.getContext(), DetailPostActivity.class);
        intentChangePost = new Intent(this.getActivity(), ChangePost.class);
        preferences = getActivity().getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        progressBar = new ProgressBar(this.getContext());
        appUserID = new AppUserID(this.getContext());
        userDTO = findById(UserDTO.class, appUserID.getID());

        Bundle groupType = getArguments();
        stringTypeGroup = groupType.getString(TYPE_GROUP);


        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
        mLayoutManager = new LinearLayoutManager(getContext());

        recyclerViewPost = (RecyclerView) rootView.findViewById(R.id.idListGroup);
        recyclerViewPost.setHasFixedSize(true);
        recyclerViewPost.setLayoutManager(mLayoutManager);

        postDTOs = (ArrayList<PostDTO>) PostDTO.find(PostDTO.class, "type = ?", stringTypeGroup);
        Collections.sort(postDTOs, new Comparator<PostDTO>() {
            public int compare(PostDTO o1, PostDTO o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        adapterPostGroup = new AdapterPostGroup(getActivity(), postDTOs);
        recyclerViewPost.setAdapter(adapterPostGroup);

        groupSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        groupSwipeRefreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);

        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }
        });

        recyclerViewPost.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerViewPost, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (postDTOs != null) {
                    intentDetailPostActivity.putExtra(POST_ID, postDTOs.get(position).getId());
                    startActivity(intentDetailPostActivity);
                }
            }

            @Override
            public void onLongItemClick(View view, final int position) {
                postDTO = postDTOs.get(position);
                GroupFragment.idPost = postDTOs.get(position).getId();

                if (postDTO.getUser_id().equals(userDTO.getId()) || userDTO.isAdmin()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(R.array.choice_double_click_post, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: // Edit
                                    changePost(GroupFragment.idPost);
                                    break;
                                case 1: // Delete
                                    Timber.e("Click delete");
                                    deletePost();
                                    break;
                            }
                        }
                    });
                    builder.show();
                }
            }
        }));

        return rootView;
    }

    public void loadUpdate() {
        PostDTO.deleteAll(PostDTO.class, "type = ?", stringTypeGroup);
        new Server<ArrayList<PostDTO>>().getListPosts(stringTypeGroup, new Server.AnswerServerResponse<ArrayList<PostDTO>>() {
            @Override
            public void processFinish(Boolean isSuccess, ServerResponse<ArrayList<PostDTO>> answerServerResponse) {
                if (isSuccess && answerServerResponse != null) {
                    PostDTO.saveInTx(answerServerResponse.getDataTransferObject());
                    refreshRecyclerView();
                }
                groupSwipeRefreshLayout.setRefreshing(false);
                progressBar.dismissView();
            }
        });
    }

    public void refreshRecyclerView() {
        postDTOs.clear();
        postDTOs.addAll(PostDTO.find(PostDTO.class, "type = ?", stringTypeGroup));
        Collections.sort(postDTOs, new Comparator<PostDTO>() {
            public int compare(PostDTO o1, PostDTO o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        adapterPostGroup.notifyDataSetChanged();
    }

    public void changePost(Long post_id) {
        intentChangePost.putExtra(POST_ID, post_id);
        startActivityForResult(intentChangePost, 1);
    }

    public void createPost() {
        Intent intentButtonAdd = new Intent(GroupFragment.this.getActivity(), CreatePost.class);
        intentButtonAdd.putExtra(TYPE_GROUP, stringTypeGroup);
        startActivityForResult(intentButtonAdd, 1);
    }

    public void deletePost(){
        progressBar.showView();
        new Server<String>().deletePost(postDTO, new Server.AnswerServerResponse<String>() {
            @Override
            public void processFinish(Boolean isSuccess, ServerResponse<String> answerServerResponse) {
                if (isSuccess) {
                    postDTO.delete();
                    refreshRecyclerView();
                    Toast.makeText(getActivity(), R.string.post_was_delete, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.errorServer, Toast.LENGTH_SHORT).show();
                }
                progressBar.dismissView();
            }
        });
    }

    @Override
    public void onRefresh() {
        groupSwipeRefreshLayout.setRefreshing(true);
        loadUpdate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_group, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_update_list_post:
                progressBar.showView();
                loadUpdate();
                break;
            case R.id.menu_write_post:
                createPost();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            loadUpdate();
//            intentDetailPostActivity.putExtra(ONE_POST_DATA, (PostDTO) data.getSerializableExtra(ONE_POST_DATA));
//            startActivity(intentDetailPostActivity);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}



