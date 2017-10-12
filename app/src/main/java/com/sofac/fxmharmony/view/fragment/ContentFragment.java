package com.sofac.fxmharmony.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPushListView;
import com.sofac.fxmharmony.dto.PushMessage;
import com.sofac.fxmharmony.view.BaseFragment;
import com.sofac.fxmharmony.view.DetailPushMessageActivity;

import java.util.ArrayList;

import static com.orm.SugarRecord.listAll;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;

public class ContentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

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

        listViewPush.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                if (pushMessages != null) {
                    intentDetailTaskActivity.putExtra(ONE_PUSH_MESSAGE_DATA, pushMessages.get(position));
                    startActivity(intentDetailTaskActivity);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        updateViewList();
        super.onResume();
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
        updateViewList();

    }

}


