package com.sofac.fxmharmony.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import timber.log.Timber;

import static com.orm.SugarRecord.listAll;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;

public class PushFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public Intent intentDetailTaskActivity;
    public SwipeRefreshLayout groupSwipeRefreshLayout;
    public AdapterPushListView adapterTasksListView;
    public ListView listViewPush;
    public ArrayList<PushMessage> pushMessages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentDetailTaskActivity = new Intent(this.getActivity(), DetailPushMessageActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_push, container, false);
        listViewPush = (ListView) rootView.findViewById(R.id.idListPost);
        groupSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.idRefresh);
        groupSwipeRefreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);

        listViewPush.setEmptyView(rootView.findViewById(R.id.id_list_empty));
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
                        PushMessage.saveInTx(answerServerResponse.getDataTransferObject());
                        updateViewList();
                        Timber.e("update Push ViewList OK");
                    } else {
                        Timber.e("update Push ViewList ERROR!!");
                    }
                });
    }

    protected void updateViewList() {
        pushMessages = (ArrayList<PushMessage>) listAll(PushMessage.class);
        adapterTasksListView = new AdapterPushListView(this.getActivity(), pushMessages);
        listViewPush.setAdapter(adapterTasksListView);
        //adapterTasksListView.notifyDataSetChanged();
        groupSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_all:
                if (pushMessages != null) {
                    pushMessages.clear();
                    PushMessage.deleteAll(PushMessage.class);
                    adapterTasksListView.notifyDataSetChanged();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        groupSwipeRefreshLayout.setRefreshing(true);
        loadData();

    }

}


