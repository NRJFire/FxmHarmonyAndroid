package com.sofac.fxmharmony.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.DELETE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.LOAD_ALL_POSTS_REQUEST;
import static com.sofac.fxmharmony.Constants.UPDATE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.UPDATE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.WRITE_POST_REQUEST;

public class GroupExchangeOnServer<T> extends AsyncTask<String, Void, String> {

    private ServerResponse serverResponse;

    private String type;
    private T serverObject;
    private Context context;
    private Boolean toDoProgressDialog = false;
    private ProgressDialog pd;


    public interface AsyncResponseWithAnswer {
        void processFinish(Boolean isSuccess, String answer);
    }


    private AsyncResponseWithAnswer asyncResponseWithAnswer = null;

    public GroupExchangeOnServer(T serverObject, Boolean toDoProgressDialog, String type, Context context, AsyncResponseWithAnswer asyncResponseWithAnswer) {
        pd = new ProgressDialog(context, R.style.MyTheme);
        this.serverObject = serverObject;
        this.type = type;
        this.toDoProgressDialog = toDoProgressDialog;
        this.context = context;
        this.asyncResponseWithAnswer = asyncResponseWithAnswer;
    }


    @Override
    protected void onPreExecute() {
        if (toDoProgressDialog) {
            pd.setCancelable(false);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pd.show();
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    protected String doInBackground(String... urls) {

        ServerRequest serverRequest = new ServerRequest(type, null);
        DataManager dataManager = DataManager.getInstance();

        PostDTO postDTO;
        CommentDTO commentDTO;


        switch (type) {
            case Constants.LOAD_ALL_POSTS_REQUEST:

                serverRequest.setDataTransferObject(serverObject);

                Log.e("!!!!!serverRequest!!!! ",serverRequest.toString());

                serverResponse = dataManager.postGroupRequest(serverRequest, type);//postID = serverRequest
                break;

            case Constants.LOAD_COMMENTS_REQUEST:

                serverRequest.setDataTransferObject(serverObject);

                serverResponse = dataManager.postGroupRequest(serverRequest, type);

                break;

            case WRITE_POST_REQUEST:

                postDTO = (PostDTO) serverObject;

                serverRequest = new ServerRequest<>(type, postDTO);
                serverResponse = dataManager.postGroupRequest(serverRequest, type);

                break;

            case Constants.WRITE_COMMENT_REQUEST:

                commentDTO = (CommentDTO) serverObject;
                serverRequest.setDataTransferObject(commentDTO);

                serverResponse = dataManager.postGroupRequest(serverRequest, type);

                break;

            case Constants.DELETE_POST_REQUEST:
                Long postID = (Long) serverObject;
                serverRequest.setDataTransferObject(postID); //postID = serverRequest

                serverResponse = dataManager.postGroupRequest(serverRequest, type);

                break;

            case DELETE_COMMENT_REQUEST:

                Long commentID = (Long) serverObject;
                serverRequest.setDataTransferObject(commentID);

                serverResponse = dataManager.postGroupRequest(serverRequest, type);

                break;
            case UPDATE_POST_REQUEST:

                postDTO = (PostDTO) serverObject;
                serverRequest.setDataTransferObject(postDTO);

                serverResponse = dataManager.postGroupRequest(serverRequest, type);

                break;

            case UPDATE_COMMENT_REQUEST:

                commentDTO = (CommentDTO) serverObject;
                serverRequest.setDataTransferObject(commentDTO);

                serverResponse = dataManager.postGroupRequest(serverRequest, type);

                break;

        }

        if (serverResponse != null) {
            return serverResponse.getResponseStatus();
        }


        return Constants.SERVER_REQUEST_ERROR;
    }


    @Override
    protected void onPostExecute(String result) {
        Timber.e("Response Server: " + result);

        if (result.equals(Constants.REQUEST_SUCCESS)) {

            String answer = null;

            switch (type) {
                case LOAD_ALL_POSTS_REQUEST:

                    ServerResponse<List<PostDTO>> loadAllPostsServerResponse = serverResponse;
                    ArrayList<PostDTO> postDTOs = (ArrayList<PostDTO>) loadAllPostsServerResponse.getDataTransferObject();

                    PostDTO.deleteAll(PostDTO.class);
                    for (PostDTO postDTO : postDTOs) {
                        postDTO.save();
                    }

                    break;
                case Constants.LOAD_COMMENTS_REQUEST: {

                    ServerResponse<List<CommentDTO>> loadCommentsServerResponse = serverResponse;

                    //List<CommentDTO> oldCommentsDTO = CommentDTO.find(CommentDTO.class, "post_ID = ?", String.valueOf((Long) serverObject));

                    CommentDTO.deleteAll(CommentDTO.class);

                    ArrayList<CommentDTO> commentDTOs = (ArrayList<CommentDTO>) loadCommentsServerResponse.getDataTransferObject();

                    for (CommentDTO commentDTO : commentDTOs) {
                        commentDTO.save();
                    }
                    break;

                }
                case Constants.WRITE_POST_REQUEST: {

                    //Timber.i(serverResponse.toString());
                    Log.e("RESPONSE" , serverResponse.toString());

                    ServerResponse<String> writePostServerResponse = serverResponse;

                    answer = writePostServerResponse.getDataTransferObject();

                    Log.e("ANSWER" ,  answer);
                    break;
                }

            }

            asyncResponseWithAnswer.processFinish(true, answer);

        } else {
            Toast.makeText(context, R.string.errorServer, Toast.LENGTH_SHORT).show();
            asyncResponseWithAnswer.processFinish(false, null);
        }
        pd.dismiss();
    }
}