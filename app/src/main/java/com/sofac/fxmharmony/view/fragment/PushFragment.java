package com.sofac.fxmharmony.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPushListView;
import com.sofac.fxmharmony.dto.PushMessage;
import com.sofac.fxmharmony.dto.SenderContainerDTO;
import com.sofac.fxmharmony.server.Connection;
import com.sofac.fxmharmony.util.AppUserID;
import com.sofac.fxmharmony.view.BaseFragment;
import com.sofac.fxmharmony.view.DetailPushMessageActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;

public class PushFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public Intent intentDetailTaskActivity;
    public AdapterPushListView adapterTasksListView;
    public ArrayList<PushMessage> pushMessages = new ArrayList<>();
    @BindView(R.id.idListPost)
    ListView listViewPush;
    @BindView(R.id.idRefresh)
    SwipeRefreshLayout groupSwipeRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.id_list_empty)
    LinearLayout idListEmpty;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentDetailTaskActivity = new Intent(this.getActivity(), DetailPushMessageActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_push, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        groupSwipeRefreshLayout.setOnRefreshListener(this);

        adapterTasksListView = new AdapterPushListView(this.getActivity(), pushMessages);
        listViewPush.setAdapter(adapterTasksListView);
        listViewPush.setEmptyView(idListEmpty);
        listViewPush.setDivider(null);
        listViewPush.setOnItemClickListener((parent, itemClicked, position, id) -> {
            if (pushMessages != null) {
                intentDetailTaskActivity.putExtra(ONE_PUSH_MESSAGE_DATA, pushMessages.get(position));
                startActivity(intentDetailTaskActivity);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        loadData();
        super.onResume();
    }

    public void loadData() {
        AppUserID appUserID = new AppUserID(this.getActivity());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        new Connection<ArrayList<PushMessage>>().getListPush(
                new SenderContainerDTO(appUserID.getID(), sharedPref.getString(Constants.GOOGLE_CLOUD_PREFERENCE, "")),
                (isSuccess, answerServerResponse) -> {
                    if (isSuccess) {
                        pushMessages.clear();
                        pushMessages.addAll(answerServerResponse.getDataTransferObject());
                        updateViewList();
                    } else {
                        showToast(getResources().getString(R.string.errorServer));
                    }
                });
    }

    protected void updateViewList() {

        adapterTasksListView.notifyDataSetChanged();
        groupSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        groupSwipeRefreshLayout.setRefreshing(true);
        loadData();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}


