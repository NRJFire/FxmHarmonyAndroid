package com.sofac.fxmharmony.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.PushMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdapterPushListView extends BaseAdapter {
    private ArrayList<PushMessage> pushMessageArrayList;
    private Context ctx;
    private LayoutInflater inflater;

    public AdapterPushListView(Context context, ArrayList<PushMessage> pushMessageArrayList) {
        if (pushMessageArrayList != null) {
            if (pushMessageArrayList.size() > 1) {
                Collections.sort(pushMessageArrayList, new Comparator<PushMessage>() {
                    public int compare(PushMessage o1, PushMessage o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });
            }
        }
        this.pushMessageArrayList = pushMessageArrayList;
        this.ctx = context;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pushMessageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return pushMessageArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // используем созданные, но не используемые view
        //View view = convertView;
        //if (view == null) {
        View view = inflater.inflate(R.layout.item_task, parent, false);
        //}

        PushMessage pushMessage = getPushMessage(position);

        ((TextView) view.findViewById(R.id.idTitleItemTask)).setText(pushMessage.getTitle());
        ((TextView) view.findViewById(R.id.idDateItemTask)).setText(pushMessage.getDate());
        ((TextView) view.findViewById(R.id.idMessageItemTask)).setText(Html.fromHtml(pushMessage.getMessage()));

        return view;
    }

    PushMessage getPushMessage(int position) {
        return ((PushMessage) getItem(position));
    }

}
