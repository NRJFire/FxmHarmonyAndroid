package com.sofac.fxmharmony.view.customView;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.util.AppMethods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileItemWithCancel extends RelativeLayout {

    private Uri fileUri;
    private List<String> fileList;
    private List<Uri> fileListToSend;

    public FileItemWithCancel(Context context, Uri uri, List<String> fileList, List<Uri> fileListToSend) {
        super(context);

        this.fileUri = uri;
        this.fileList = fileList;
        this.fileListToSend = fileListToSend;

        int padding = AppMethods.getPxFromDp(5, context);
        int height = AppMethods.getPxFromDp(80, context);


        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        this.setLayoutParams(layoutParams);
        this.setPadding(padding, padding, 0, padding);

        TextView textView = new TextView(context);
        textView.setText("attached file");

        Button cancelButton = new Button(context);

        this.addView(textView);
        this.addView(cancelButton);

        final RelativeLayout.LayoutParams fileLayoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
    /*    fileLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);*/
        fileLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, cancelButton.getId());
        fileLayoutParams.addRule(RelativeLayout.ALIGN_END, cancelButton.getId());
        fileLayoutParams.addRule(CENTER_VERTICAL);
        fileLayoutParams.height = AppMethods.getPxFromDp(height, context);
        fileLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;


        RelativeLayout.LayoutParams cancelButtonLayoutParams = (RelativeLayout.LayoutParams) cancelButton.getLayoutParams();
        cancelButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cancelButtonLayoutParams.topMargin = padding;
        cancelButtonLayoutParams.rightMargin = padding;
        cancelButtonLayoutParams.width = AppMethods.getPxFromDp(30, context);
        cancelButtonLayoutParams.height = AppMethods.getPxFromDp(30, context);


        cancelButton.setBackground(context.getResources().getDrawable(R.drawable.remove_symbol));


        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                
                Iterator<String> fileListIterator = FileItemWithCancel.this.fileList.iterator();
                while (fileListIterator.hasNext()) {


                    String fileURL = Constants.BASE_URL + Constants.GET_POST_FILES_END_URL + fileListIterator.next();

                    if (fileURL.equals(fileUri.toString())) {
                        fileListIterator.remove();
                    }
                }

                Iterator<Uri> fileListToSendIterator = FileItemWithCancel.this.fileListToSend.iterator();
                while (fileListToSendIterator.hasNext()) {
                    Uri fileUrl = fileListToSendIterator.next();

                    if (fileUrl.equals(fileUri)) {
                        fileListToSendIterator.remove();
                    }
                }

                LinearLayoutGallery parent = (LinearLayoutGallery) FileItemWithCancel.this.getParent();
                parent.removeView(FileItemWithCancel.this);


            }
        });

    }

}
