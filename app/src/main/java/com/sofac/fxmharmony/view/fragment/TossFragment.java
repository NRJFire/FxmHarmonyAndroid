package com.sofac.fxmharmony.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterTossItems;
import com.sofac.fxmharmony.adapter.RecyclerItemClickListener;
import com.sofac.fxmharmony.dto.SenderContainerDTO;
import com.sofac.fxmharmony.dto.TossDTO;
import com.sofac.fxmharmony.server.Connection;
import com.sofac.fxmharmony.util.AppUserID;
import com.sofac.fxmharmony.view.DetailTossActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class TossFragment extends Fragment {

    public ArrayList<TossDTO> tossDTOs = new ArrayList<>();
    public AdapterTossItems adapterTossItems;

    @BindView(R.id.recyclerViewToss)
    RecyclerView recyclerViewToss;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toss, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapterTossItems = new AdapterTossItems(tossDTOs);

        recyclerViewToss.setAdapter(adapterTossItems);
        recyclerViewToss.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recyclerViewToss.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), recyclerViewToss, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), DetailTossActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    @Override
    public void onResume() {
        updateRecyclerView( "");
        super.onResume();
    }

    ///"status" available values: "open", "process", "pause", "closed", "my"

    public void updateRecyclerView(String filter) {
        new Connection<ArrayList<TossDTO>>().getListToss(
                new SenderContainerDTO(filter, new AppUserID(this.getActivity()).getID().toString()),
                (isSuccess, answerServerResponse) -> {
                    if (isSuccess) {
                        Timber.e(answerServerResponse.getDataTransferObject().toString());
                        tossDTOs.clear();
                        tossDTOs.addAll(answerServerResponse.getDataTransferObject());
                        adapterTossItems.notifyDataSetChanged();
                    } else {
                        showToast(getResources().getString(R.string.errorConnection));
                    }
                });
    }

    public void showToast(String messageToast){
        Toast.makeText(getActivity(), messageToast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.viewButtonMyToss, R.id.viewButtonAllToss})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.viewButtonMyToss:
                updateRecyclerView( "my");

                break;
            case R.id.viewButtonAllToss:
                updateRecyclerView( "");
                break;
        }
    }
}
