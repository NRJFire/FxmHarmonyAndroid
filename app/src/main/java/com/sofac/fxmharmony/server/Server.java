package com.sofac.fxmharmony.server;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.dto.AppVersionDTO;
import com.sofac.fxmharmony.data.dto.ManagerInfoDTO;
import com.sofac.fxmharmony.dto.AuthorizationDTO;
import com.sofac.fxmharmony.dto.UserDTO;
import com.sofac.fxmharmony.server.retrofit.ManagerRetrofit;
import com.sofac.fxmharmony.server.type.ServerResponse;

import timber.log.Timber;

/**
 * Created by Maxim on 03.08.2017.
 */

public class Server<T> {

    private AnswerServerResponse answerServerResponse = null;

    public interface AnswerServerResponse<T> {
        void processFinish(Boolean isSuccess, ServerResponse<T> answerServerResponse);
    }

    /**
     * Authorization DTO
     */
    public void authorizationUser(AuthorizationDTO authorizationDTO, AnswerServerResponse<T> async) { //Change name request / Change data in method parameters
        answerServerResponse = async;
        String requestType = new Object() {
        }.getClass().getEnclosingMethod().getName();

        new ManagerRetrofit<AuthorizationDTO>().sendRequest(authorizationDTO, requestType, new ManagerRetrofit.AsyncAnswerString() { // Change type Object sending / Change data
            @Override
            public void processFinish(Boolean isSuccess, String answerString) {
                if (isSuccess) {
                    Type typeAnswer = new TypeToken<ServerResponse<UserDTO>>() { //Change type response
                    }.getType();
                    answerServerResponse.processFinish(true, getObjectTransferFromJSON(answerString, typeAnswer));
                }else {
                    answerServerResponse.processFinish(false, null);
                }
            }
        });
    }

    /**
     * Get correct VERSION from Server
     */
    public void getCorrectVersion(AnswerServerResponse<T> async) {
        answerServerResponse = async;
        String requestType = new Object() {
        }.getClass().getEnclosingMethod().getName();

        new ManagerRetrofit<String>().sendRequest("", requestType, new ManagerRetrofit.AsyncAnswerString() { // Change type Object sending / Change data
            @Override
            public void processFinish(Boolean isSuccess, String answerString) {
                if (isSuccess) {
                    Type typeAnswer = new TypeToken<ServerResponse<AppVersionDTO>>() { //Change type response
                    }.getType();
                    answerServerResponse.processFinish(true, getObjectTransferFromJSON(answerString, typeAnswer));
                }else {
                    answerServerResponse.processFinish(false, null);
                }
            }
        });
    }

    /**
     * Get List VERSIONS history from Server
     */
    public void getVersionsHistory(AnswerServerResponse async) {

    }

    /**
     * Get MANAGER info from server
     */
    public void getManagerInfo(Long idManager, AnswerServerResponse async) {
    }

    /**
     * Get CUSTOMER info from server
     */
    public void getCustomerInfo(Long idCustomer, AnswerServerResponse async) {
    }

    /**
     * Get STAFF info from server
     */
    public void getStaffInfo(Long idStaff, AnswerServerResponse async) {
    }


    /**
     * Get SETTINGS from Server
     */
    public void getSettings() {

    }

    /**
     * Get ONE POST from Server
     */
    public void getPost(ManagerInfoDTO managerDTO, AnswerServerResponse async) {
    }

    /**
     * Get ONE COMMENT from Server
     */
    public void getComment(ManagerInfoDTO managerDTO, AnswerServerResponse async) {
    }


    /**   */
    public void getListPosts() {
    }

    /**   */
    public void getListComments() {
    }


    /**   */
    public void createPost() {
    }

    /**   */
    public void createComment() {
    }


    /**   */
    public void updatePost() {
    }

    /**   */
    public void updateComment() {
    }


    /**   */
    public void deletePost() {
    }

    /**   */
    public void deleteComment() {
    }

    private ServerResponse<T> getObjectTransferFromJSON(String string, Type type) {
        try {
            return new Gson().fromJson(string, type);
        } catch (JsonSyntaxException e) {
            Timber.e("Не соответствующий тип данных для парсинга JSON");
            e.printStackTrace();
        }
        return null;
    }


//    private AsyncAnswerServerResponse answerServerResponse = null;
//
//    public interface AsyncAnswerServerResponse<T> {
//        void processFinish(Boolean isSuccess, String answerString);
//    }

    // Type authorizationType = new TypeToken<ServerResponse<ManagerInfoDTO>>(){}.getType();


}
