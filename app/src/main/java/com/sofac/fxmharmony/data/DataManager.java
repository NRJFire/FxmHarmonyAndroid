package com.sofac.fxmharmony.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.data.dto.ManagerInfoDTO;
import com.sofac.fxmharmony.dto.AppVersionDTO;
import com.sofac.fxmharmony.dto.CommentDTO;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.server.type.ServerRequest;
import com.sofac.fxmharmony.server.type.ServerResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import timber.log.Timber;

public class DataManager {

    private static final DataManager ourInstance = new DataManager();
    RequestResponseService requestResponseService;

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        requestResponseService = RequestResponseService.Creator.newAuthorizationService();
    }


    public ServerResponse<ManagerInfoDTO> sendAuthorizationRequest(ServerRequest serverRequest) {


        String response = sendRequest(serverRequest, Constants.APP_EXCHANGE, null);
        Timber.i(response);

        if (!response.equals(Constants.SERVER_REQUEST_ERROR)) {

            Type authorizationType = new TypeToken<ServerResponse<ManagerInfoDTO>>() {}.getType();

            ServerResponse<ManagerInfoDTO> managerInfoServerResponse = new Gson().fromJson(response, authorizationType);

            return managerInfoServerResponse;

        }

        return null;
    }

    public ServerResponse<AppVersionDTO> sendCheckVersion(ServerRequest serverRequest) {

        String response = sendRequest(serverRequest, Constants.APP_EXCHANGE, null);
        Timber.i(response);

        if (!response.equals(Constants.SERVER_REQUEST_ERROR)) {
            Type authorizationType = new TypeToken<ServerResponse<AppVersionDTO>>() {}.getType();
            ServerResponse<AppVersionDTO> serverResponse = new Gson().fromJson(response, authorizationType);
            return serverResponse;
        }
        return new ServerResponse<>(Constants.SERVER_REQUEST_ERROR, null);
    }

    public ServerResponse postGroupRequest(ServerRequest serverRequest, String groupRequestType) {

        String response = sendRequest(serverRequest, Constants.GROUP_EXCHANGE, null);
        Timber.i(response);

        if (!response.equals(Constants.SERVER_REQUEST_ERROR)) {
            Type authorizationType = null;

            if (groupRequestType.equals(Constants.LOAD_ALL_POSTS_REQUEST)) {
                Timber.i(Constants.LOAD_ALL_POSTS_REQUEST);
                authorizationType = new TypeToken<ServerResponse<List<PostDTO>>>() {
                }.getType();
            } else if (groupRequestType.equals(Constants.LOAD_COMMENTS_REQUEST)) {
                Timber.i(Constants.LOAD_COMMENTS_REQUEST);
                authorizationType = new TypeToken<ServerResponse<List<CommentDTO>>>() {
                }.getType();
            } else {
                authorizationType = new TypeToken<ServerResponse<String>>() {
                }.getType();
            }


            return new Gson().fromJson(response, authorizationType);

        }

        return null;

    }

    public ServerResponse postUploadFileRequest(ServerRequest serverRequest, Map<String, RequestBody> files) {


        String response = sendRequest(serverRequest, Constants.UPLOAD_MULTI_FILE_EXCHANGE, files);
        Timber.i(response);

        if (!response.equals(Constants.SERVER_REQUEST_ERROR)) {
            Type authorizationType = new TypeToken<ServerResponse>() {
            }.getType();

            if (response.equals(Constants.ATTACH_LOAD_USER_AVATAR)) {
                authorizationType = new TypeToken<ServerResponse<String>>() {
                }.getType();
            }

            return new Gson().fromJson(response, authorizationType);

        } else {
            return null;
        }

    }

    public ServerResponse postSettingsRequest(ServerRequest serverRequest) {

        String response = sendRequest(serverRequest, Constants.SETTINGS_EXCHANGE, null);
        Timber.i(response);

        if (!response.equals(Constants.SERVER_REQUEST_ERROR)) {
            Type authorizationType = new TypeToken<ServerResponse>() {
            }.getType();
            return new Gson().fromJson(response, authorizationType);
        } else {
            return null;
        }
    }


    private String sendRequest(ServerRequest serverRequest, String type, Map<String, RequestBody> files) {

        Call<ResponseBody> call = null;

        if (type.equals(Constants.APP_EXCHANGE)) {
            call = requestResponseService.postAuthorizationRequest(serverRequest);
        } else if (type.equals(Constants.GROUP_EXCHANGE)) {
            call = requestResponseService.postGroupRequest(serverRequest);
        } else if (type.equals(Constants.UPLOAD_MULTI_FILE_EXCHANGE)) {
            call = requestResponseService.postUploadFilesRequest(files, serverRequest);
        } else if (type.equals(Constants.SETTINGS_EXCHANGE)) {
            call = requestResponseService.postSettingsRequest(serverRequest);
        }

        String response = Constants.SERVER_REQUEST_ERROR;

        try (ResponseBody responseBody = call.execute().body()) {
            if (responseBody != null) {
                response = responseBody.string();
            } else {
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return response;
        }

        return response;
    }


}
