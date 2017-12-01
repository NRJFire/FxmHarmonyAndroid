package com.sofac.fxmharmony.view.fragment;


import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterTossItems;
import com.sofac.fxmharmony.adapter.RecyclerItemClickListener;
import com.sofac.fxmharmony.dto.SenderContainerDTO;
import com.sofac.fxmharmony.dto.TossDTO;
import com.sofac.fxmharmony.server.Connection;
import com.sofac.fxmharmony.util.AppPreference;
import com.sofac.fxmharmony.util.ProgressBar;
import com.sofac.fxmharmony.view.BaseFragment;
import com.sofac.fxmharmony.view.DetailTossActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class TossFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public ArrayList<TossDTO> tossDTOs = new ArrayList<>();
    public AdapterTossItems adapterTossItems = new AdapterTossItems(tossDTOs);
    public ProgressBar progressBar;
    private String status = "";
    private Unbinder unbinder;

    @BindView(R.id.recyclerViewToss)
    RecyclerView recyclerViewToss;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.viewButtonAllToss)
    Button viewButtonAllToss;
    @BindView(R.id.viewButtonMyToss)
    Button viewButtonMyToss;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toss, container, false);
        initializationRecyclerView(view);
        setHasOptionsMenu(true);
        return view;
    }


    public void initializationRecyclerView(View view){
        unbinder = ButterKnife.bind(this, view);
        progressBar = new ProgressBar(getActivity());
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerViewToss.setAdapter(adapterTossItems);
        recyclerViewToss.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewToss.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this.getActivity(),
                        recyclerViewToss,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                startActivity(new Intent(getActivity(), DetailTossActivity.class));
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }
                )
        );
        selectedButton(viewButtonAllToss);
    }

    @Override
    public void onResume() {
        updateRecyclerView(status);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateRecyclerView(String filter) {
        status = filter;
        new Connection<ArrayList<TossDTO>>().getListToss(
                new SenderContainerDTO(filter, new AppPreference(this.getActivity()).getID().toString()),
                (isSuccess, answerServerResponse) -> {
                    if (isSuccess) {
                        Timber.e(answerServerResponse.getDataTransferObject().toString());
                        tossDTOs.clear();
                        tossDTOs.addAll(answerServerResponse.getDataTransferObject());
                        adapterTossItems.notifyDataSetChanged();
                    } else {
                        showToast(getResources().getString(R.string.errorServer));
                    }
                    progressBar.dismissView();
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    public void showToast(String messageToast) {
        Toast.makeText(getActivity(), messageToast, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.viewButtonMyToss, R.id.viewButtonAllToss})
    public void onViewClicked(View view) {
        progressBar.showView();
        switch (view.getId()) {
            case R.id.viewButtonMyToss:
                updateRecyclerView("my");
                selectedButton(viewButtonMyToss);
                unSelectedButton(viewButtonAllToss);
                break;
            case R.id.viewButtonAllToss:
                updateRecyclerView("");
                selectedButton(viewButtonAllToss);
                unSelectedButton(viewButtonMyToss);
                break;
        }
    }

    public void selectedButton(Button button){
        button.setSelected(true);
        button.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    public void unSelectedButton(Button button){
        button.setSelected(false);
        button.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toss, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.filterOpen:
                progressBar.showView();
                updateRecyclerView("open");
                break;
            case R.id.filterProcess:
                progressBar.showView();
                updateRecyclerView("process");
                break;
            case R.id.filterPause:
                progressBar.showView();
                updateRecyclerView("pause");
                break;
            case R.id.filterClosed:
                progressBar.showView();
                updateRecyclerView("closed");
                break;
            case R.id.createNewToss:
                showToast("Button create press!");
                //updateRecyclerView("create");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        updateRecyclerView(status);
    }
}
