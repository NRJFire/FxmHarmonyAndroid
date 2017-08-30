package com.sofac.fxmharmony.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPostGroup;
import com.sofac.fxmharmony.adapter.RecyclerItemClickListener;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.ManagerInfoDTO;
import com.sofac.fxmharmony.data.dto.PermissionDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.view.ChangePost;
import com.sofac.fxmharmony.view.CreatePost;
import com.sofac.fxmharmony.view.DetailPostActivity;

import java.util.ArrayList;

import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;
import static com.sofac.fxmharmony.Constants.DELETE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.LOAD_ALL_POSTS_REQUEST;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.TYPE_GROUP;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;

public class GroupFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Intent intentDetailPostActivity;

    public ArrayList<PostDTO> postDTOs;
    public SwipeRefreshLayout groupSwipeRefreshLayout;
    SharedPreferences preferences;
    public FloatingActionButton floatingActionButton;
    public static Long idPost;
    public static PostDTO postDTO;
    public Intent intentChangePost;
    public String stringTypeGroup = "staff";


    public RecyclerView recyclerViewPost;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapterPostGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentDetailPostActivity = new Intent(this.getActivity(), DetailPostActivity.class);
        intentChangePost = new Intent(this.getActivity(), ChangePost.class);
        preferences = getActivity().getSharedPreferences(USER_SERVICE, MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle groupType =getArguments();
        stringTypeGroup = groupType.getString(TYPE_GROUP);

        View rootView = inflater.inflate(R.layout.fragment_group, container, false);

        mLayoutManager = new LinearLayoutManager(getContext());

        recyclerViewPost = (RecyclerView) rootView.findViewById(R.id.idListGroup);
        recyclerViewPost.setHasFixedSize(true);
        recyclerViewPost.setLayoutManager(mLayoutManager);



        groupSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        groupSwipeRefreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);

        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentButtonAdd = new Intent(GroupFragment.this.getActivity(), CreatePost.class);
                intentButtonAdd.putExtra(TYPE_GROUP, stringTypeGroup);
                startActivityForResult(intentButtonAdd, 1);
            }
        });


        recyclerViewPost.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerViewPost, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (postDTOs != null) {
                    intentDetailPostActivity.putExtra(ONE_POST_DATA, postDTOs.get(position));
                    startActivity(intentDetailPostActivity);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                postDTO = postDTOs.get(position);
                GroupFragment.idPost = postDTOs.get(position).getServerID();
                PermissionDTO permissionDTO = PermissionDTO.findById(PermissionDTO.class, preferences.getLong(USER_ID_PREF, 1L));

                if (postDTO.getUserID() == preferences.getLong(USER_ID_PREF, 0L) || permissionDTO.getSuperAdminPermission()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(R.array.choice_double_click_post, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: //Edit
                                    writePost();
                                    break;
                                case 1: //Delete
                                    new GroupExchangeOnServer<>(GroupFragment.idPost, true, DELETE_POST_REQUEST, getActivity(), new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                                        @Override
                                        public void processFinish(Boolean isSuccess, String answer) {
                                            if (isSuccess) {
                                                updateViewList(true);
                                                Toast.makeText(getActivity(), "Post was delete!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).execute();
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

    public void updateViewList(Boolean toDoProgressDialog) {
        ManagerInfoDTO managerInfoDTO = ManagerInfoDTO.findById(ManagerInfoDTO.class, preferences.getLong(USER_ID_PREF, 1L));
        Timber.e(managerInfoDTO.toString());

        new GroupExchangeOnServer<>(stringTypeGroup, toDoProgressDialog, LOAD_ALL_POSTS_REQUEST, getActivity(), new GroupExchangeOnServer.AsyncResponseWithAnswer() {
            @Override
            public void processFinish(Boolean isSuccess, String answer) {
                if (isSuccess) {

                    postDTOs = (ArrayList<PostDTO>) PostDTO.listAll(PostDTO.class);

                    if (postDTOs != null) {
                        adapterPostGroup = new AdapterPostGroup(getActivity(), postDTOs);
                        recyclerViewPost.setAdapter(adapterPostGroup);
                        recyclerViewPost.setHasFixedSize(true);
                        adapterPostGroup.notifyDataSetChanged();
                    }
                }
            }
        }).execute();


    }

    @Override
    public void onResume() {
        updateViewList(true);
        super.onResume();
    }

    public void writePost() {
        intentChangePost.putExtra(ONE_POST_DATA, postDTO);
        startActivityForResult(intentChangePost, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 2) {
            intentDetailPostActivity.putExtra(ONE_POST_DATA, (PostDTO) data.getSerializableExtra(ONE_POST_DATA));
            startActivity(intentDetailPostActivity);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRefresh() {
        groupSwipeRefreshLayout.setRefreshing(true);
        updateViewList(false);
        groupSwipeRefreshLayout.setRefreshing(false);
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
                updateViewList(true);
                break;
            case R.id.menu_write_post:
                startActivityForResult(new Intent(GroupFragment.this.getActivity(), CreatePost.class), 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}



