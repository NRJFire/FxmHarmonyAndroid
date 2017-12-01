package com.sofac.fxmharmony.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPostGroup;
import com.sofac.fxmharmony.adapter.RecyclerItemClickListener;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.server.Connection;
import com.sofac.fxmharmony.util.AppPreference;
import com.sofac.fxmharmony.util.ProgressBar;
import com.sofac.fxmharmony.view.BaseFragment;
import com.sofac.fxmharmony.view.ChangePost;
import com.sofac.fxmharmony.view.CreatePostActivity;
import com.sofac.fxmharmony.view.DetailPostActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.POST_ID;
import static com.sofac.fxmharmony.Constants.TYPE_GROUP;

public class GroupFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public ArrayList<PostDTO> postDTOs = new ArrayList<>();
    public static Long idPost;
    public PostDTO postDTO;

    public String typeUsersGroup = "leadergroup";

    public String typeLeaderGroup = "leadergroup";
    public String typeMemberGroup = "membergroup";
    public String typeStaffGroup = "staffgroup";

    Unbinder unbinder;

    private RecyclerView.Adapter adapterPostGroup;
    public AppPreference appPreference;
    public ProgressBar progressBar;

    @BindView(R.id.idListGroup)
    RecyclerView recyclerViewPost;
    @BindView(R.id.refresh)
    SwipeRefreshLayout groupSwipeRefreshLayout;

    @BindView(R.id.viewButtonLeader)
    Button viewButtonLeader;
    @BindView(R.id.viewButtonMember)
    Button viewButtonMember;
    @BindView(R.id.viewButtonStaff)
    Button viewButtonStaff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = new ProgressBar(getActivity());
        appPreference = new AppPreference(getActivity());
        adapterPostGroup = new AdapterPostGroup(getActivity(), postDTOs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        groupSwipeRefreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);
        initializationUI();
        setupButtonFilter();
        updateRecyclerView(typeUsersGroup);
        return rootView;
    }

    public void setupButtonFilter() {
        if (appPreference.getUser().isAdmin() != null && appPreference.getUser().isAdmin()) {
            typeUsersGroup = typeLeaderGroup;
        } else {
            if (appPreference.getUser().isAccessStaffGroup()) {
                typeUsersGroup = typeStaffGroup;
            } else {
                viewButtonStaff.setEnabled(false);
                viewButtonStaff.setTextColor(getResources().getColor(R.color.ColorGrey));
            }

            if (appPreference.getUser().isAccessMemberGroup()) {
                typeUsersGroup = typeMemberGroup;
            } else {
                viewButtonMember.setEnabled(false);
                viewButtonMember.setTextColor(getResources().getColor(R.color.ColorGrey));
            }

            if (appPreference.getUser().isAccessLeaderGroup()) {
                typeUsersGroup = typeLeaderGroup;
            } else {
                viewButtonLeader.setEnabled(false);
                viewButtonLeader.setTextColor(getResources().getColor(R.color.ColorGrey));
            }
        }
    }

    @OnClick({R.id.viewButtonLeader, R.id.viewButtonMember, R.id.viewButtonStaff})
    public void onViewClicked(View view) {
        progressBar.showView();
        switch (view.getId()) {
            case R.id.viewButtonLeader:
                updateRecyclerView(typeLeaderGroup);
                selectedButton(viewButtonLeader);
                unSelectedButton(viewButtonMember);
                unSelectedButton(viewButtonStaff);
                break;
            case R.id.viewButtonMember:
                updateRecyclerView(typeMemberGroup);
                selectedButton(viewButtonMember);
                unSelectedButton(viewButtonLeader);
                unSelectedButton(viewButtonStaff);
                break;
            case R.id.viewButtonStaff:
                updateRecyclerView(typeStaffGroup);
                selectedButton(viewButtonStaff);
                unSelectedButton(viewButtonLeader);
                unSelectedButton(viewButtonMember);
                break;
        }
    }

    public void selectedButton(Button button) {
        button.setSelected(true);
        button.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    public void unSelectedButton(Button button) {
        button.setSelected(false);
        button.setTextColor(getResources().getColor(R.color.colorPrimary));
    }


    public void initializationUI() {
        recyclerViewPost.setAdapter(adapterPostGroup);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPost.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerViewPost, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (postDTOs != null) {
                    startActivityDetailPost(postDTOs.get(position).getId());
                }
            }

            @Override
            public void onLongItemClick(View view, final int position) {
                postDTO = postDTOs.get(position);
                GroupFragment.idPost = postDTOs.get(position).getId();

                if (postDTO.getUser_id().equals(appPreference.getUser().getId()) || appPreference.getUser().isAdmin()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(R.array.choice_double_click_post, (dialog, which) -> {
                        switch (which) {
                            case 0: // Edit
                                startActivityChangePost(GroupFragment.idPost);
                                break;
                            case 1: // Delete
                                deletePost();
                                break;
                        }
                    });
                    builder.show();
                }
            }
        }));
    }

    public void updateRecyclerView(String filter) {
        typeUsersGroup = filter;
        new Connection<ArrayList<PostDTO>>().getListPosts(filter, (isSuccess, answerServerResponse) -> {
            if (isSuccess && answerServerResponse != null) {
                postDTOs.clear();
                postDTOs.addAll(answerServerResponse.getDataTransferObject());
                adapterPostGroup.notifyDataSetChanged();
            }
            groupSwipeRefreshLayout.setRefreshing(false);
            progressBar.dismissView();
        });
    }

    public void startActivityDetailPost(Long post_id) {
        startActivityForResult(new Intent(getActivity(), DetailPostActivity.class).putExtra(POST_ID, post_id), 1);
    }

    public void startActivityChangePost(Long post_id) {
        startActivityForResult(new Intent(getActivity(), ChangePost.class).putExtra(POST_ID, post_id), 1);
    }

    public void startActivityCreatePost() {
        startActivityForResult((new Intent(getActivity(), CreatePostActivity.class)).putExtra(TYPE_GROUP, typeUsersGroup), 1);
    }

    public void deletePost() {
        progressBar.showView();
        new Connection<String>().deletePost(postDTO, (isSuccess, answerServerResponse) -> {
            if (isSuccess) {
                updateRecyclerView(typeUsersGroup);
                showToast(getResources().getString(R.string.post_was_delete));
            } else {
                showToast(getResources().getString(R.string.errorServer));
            }
            progressBar.dismissView();
        });
    }

    @Override
    public void onRefresh() {
        groupSwipeRefreshLayout.setRefreshing(true);
        updateRecyclerView(typeUsersGroup);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_group, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_write_post)
            startActivityCreatePost();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            updateRecyclerView(typeUsersGroup);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}



