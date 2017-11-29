package com.sofac.fxmharmony.server;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.dto.AppVersionDTO;
import com.sofac.fxmharmony.dto.AuthorizationDTO;
import com.sofac.fxmharmony.dto.CommentDTO;
import com.sofac.fxmharmony.dto.ManagerDTO;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.dto.PushMessage;
import com.sofac.fxmharmony.dto.SenderContainerDTO;
import com.sofac.fxmharmony.dto.TossDTO;
import com.sofac.fxmharmony.dto.UserDTO;
import com.sofac.fxmharmony.server.retrofit.ManagerRetrofit;
import com.sofac.fxmharmony.server.type.ServerResponse;
import com.sofac.fxmharmony.util.PathUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;


/**
 * Created by Maxim on 03.08.2017.
 */

public class Connection<T> {

    private AnswerServerResponse<T> answerServerResponse;

    public interface AnswerServerResponse<T> {
        void processFinish(Boolean isSuccess, ServerResponse<T> answerServerResponse);
    }

    /**
     * Authorization DTO
     */
    public void authorizationUser(AuthorizationDTO authorizationDTO, AnswerServerResponse<T> async) { //Change name request / Change data in method parameters
        answerServerResponse = async;
        new ManagerRetrofit<AuthorizationDTO>().sendRequest(authorizationDTO, new Object() {// Change type Object sending / Change data sending
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse<UserDTO>>() { //Change type response
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }

    public void getListPush(SenderContainerDTO senderContainerDTO, AnswerServerResponse<T> async) { //Change name request / Change data in method parameters
        answerServerResponse = async;
        new ManagerRetrofit<SenderContainerDTO>().sendRequest(senderContainerDTO, new Object() {// Change type Object sending / Change data sending
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse<ArrayList<PushMessage>>>() { //Change type response
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }

    /**
     * Get correct VERSION from Server
     */
    public void getCorrectVersion(AnswerServerResponse<T> async) { //Change name request() / Change data in method parameters
        answerServerResponse = async;
        new ManagerRetrofit<String>().sendRequest("", new Object() {// Change type Object sending / Change data sending
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse<AppVersionDTO>>() { //Change type response
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }

    /**
     * Get MANAGER info from server
     */
    public void getManagerInfo(Long idManager, AnswerServerResponse<T> async) {
        answerServerResponse = async;
        new ManagerRetrofit<Long>().sendRequest(idManager, new Object() {// Change type Object sending / Change data sending
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse<ManagerDTO>>() { //Change type response
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }

    /**
     * LIST COMMENTS
     */
    public void getListComments(Long postId, AnswerServerResponse<T> async) {
        answerServerResponse = async;
        new ManagerRetrofit<Long>().sendRequest(postId, new Object() {// Change (type sending) / (data sending)
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse<ArrayList<CommentDTO>>>() { //Change type response(тип ответа)
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }

    /**
     * LIST POSTS
     */
    public void getListPosts(String stringTypeGroup, AnswerServerResponse<T> async) {
        answerServerResponse = async;
        new ManagerRetrofit<String>().sendRequest(stringTypeGroup, new Object() {// Change (type sending) / (data sending)
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse<List<PostDTO>>>() { //Change type response(тип ответа)
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }

    /**
     * LIST TOSSES
     */
    public void getListToss(SenderContainerDTO senderContainerDTO, AnswerServerResponse<T> async) {
        answerServerResponse = async;
        new ManagerRetrofit<SenderContainerDTO>().sendRequest(senderContainerDTO, new Object() {// Change (type sending) / (data sending)
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse<ArrayList<TossDTO>>>() { //Change type response(тип ответа)
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }
    /**
     * CREATE POST
     */
    public void createPost(Context context, PostDTO postDTO, ArrayList<Uri> listUri, AnswerServerResponse<T> async) {
        answerServerResponse = async;

        new ManagerRetrofit<PostDTO>().sendMultiPartRequest(postDTO, new Object() {// Change (type sending) / (data sending)
        }.getClass().getEnclosingMethod().getName(), generateMultiPartList(listUri, context), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse>() { //Change type response(тип ответа)
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });

    }


    /**
     * UPDATE POST
     */
    public void updatePost(Context context, PostDTO postDTO, ArrayList<Uri> listUri, ArrayList<String> listDeletingFiles, AnswerServerResponse<T> async) {
        answerServerResponse = async;

        new ManagerRetrofit<PostDTO>().sendMultiPartWithTwoObj(postDTO, new Object() {// Change (type sending) / (data sending)
        }.getClass().getEnclosingMethod().getName(), generateMultiPartList(listUri, context), listDeletingFiles, (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse>() { //Change type response(тип ответа)
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });

    }

    /**
     * DELETE POST
     */
    public void deletePost(PostDTO postDTO, AnswerServerResponse<T> async) {
        answerServerResponse = async;
        new ManagerRetrofit<Long>().sendRequest(postDTO.getId(), new Object() {// Change (type sending) / (data sending)
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse>() { //Change type response(тип ответа)
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }

    /**
     * COMMENT
     */
    public void createComment(CommentDTO commentDTO, AnswerServerResponse<T> async) {
        answerServerResponse = async;
        new ManagerRetrofit<CommentDTO>().sendRequest(commentDTO, new Object() {// Change (type sending) / (data sending)
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse>() { //Change type response(тип ответа)
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }

    /**
     * UPDATE COMMENT
     */
    public void updateComment(CommentDTO commentDTO, AnswerServerResponse<T> async) {
        answerServerResponse = async;
        new ManagerRetrofit<CommentDTO>().sendRequest(commentDTO, new Object() {// Change (type sending) / (data sending)
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse>() { //Change type response(тип ответа)
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }

    /**
     * DELETE COMMENT
     */
    public void deleteComment(CommentDTO commentDTO, AnswerServerResponse<T> async) {
        answerServerResponse = async;
        new ManagerRetrofit<Long>().sendRequest(commentDTO.getId(), new Object() {// Change (type sending) / (data sending)
        }.getClass().getEnclosingMethod().getName(), (isSuccess, answerString) -> {
            if (isSuccess) {
                Type typeAnswer = new TypeToken<ServerResponse>() { //Change type response(тип ответа)
                }.getType();
                tryParsing(answerString, typeAnswer);
            } else {
                answerServerResponse.processFinish(false, null);
            }
        });
    }


    /**
     * Supporting methods
     */

    public ArrayList<MultipartBody.Part> generateMultiPartList(ArrayList<Uri> listFileUri, Context context) {
        ArrayList<MultipartBody.Part> arrayListMulti = new ArrayList<>();
        for (int i = 0; i < listFileUri.size(); i++) {
            try {
                File file = new File(PathUtil.getPath(context, listFileUri.get(i)));
                arrayListMulti.add(MultipartBody.Part.createFormData("files[" + i + "]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file)));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        return arrayListMulti;
    }

    private void tryParsing(String answerString, Type typeAnswer) {
        try {
            answerServerResponse.processFinish(true, getObjectTransferFromJSON(answerString, typeAnswer));
        } catch (Exception e) {
            answerServerResponse.processFinish(false, null);
            e.printStackTrace();
        }
    }

    private ServerResponse<T> getObjectTransferFromJSON(String string, Type type) {
        try {
            return new Gson().fromJson(string, type);
        } catch (JsonSyntaxException e) {

            Timber.e("Не соответствующий тип данных для парсинга JSON");
            e.printStackTrace();
            return null;
        }
    }

}
