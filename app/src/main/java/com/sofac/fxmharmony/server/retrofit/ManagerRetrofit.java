package com.sofac.fxmharmony.server.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.server.type.ServerRequest;
import com.sofac.fxmharmony.server.type.ServerResponse;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.BASE_URL;

/**
 * Created by Maxim on 03.08.2017.
 */

public class ManagerRetrofit<T> {

    private static final String SERVER_RESPONSE_ERROR = "SERVER_RESPONSE_ERROR";
    private static final String SERVER_RESPONSE_SUCCESS = "SERVER_RESPONSE_SUCCESS";

    //Константы
    private String serverResponseError = SERVER_RESPONSE_ERROR;
    private String serverResponseSuccess = SERVER_RESPONSE_SUCCESS;
    private String serverResponse = serverResponseError;
    private String baseUrl = BASE_URL;

    //Данные
    private ServiceRetrofit serviceRetrofit;
    private ServerRequest serverRequest;

    //Обработчики
    private AsyncAnswerString answerString = null;

    /**
     * Иницалиазация сервиса передачи
     * */
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        serviceRetrofit = retrofit.create(ServiceRetrofit.class);
    }

    /**
     * Интерфейсы обработки событий в потоке
     */
    public interface AsyncAnswerString {
        void processFinish(Boolean isSuccess, String answerString);
    }

    /**
     * Get Callback<ResponseBody> for this Request;
     */
    @SuppressWarnings("unchecked")
    public void sendRequest(T object, String requestType, Callback<ResponseBody> responseBodyCallback) {
        serverRequest = new ServerRequest(requestType, object);
        serviceRetrofit.getData(logServerRequest(serverRequest)).enqueue(responseBodyCallback);
    }

    /**
     * String
     * Get AsyncAnswer with True(SUCCESS) or False(ERROR) and String <= json (body response) for this Request;
     */
    public void sendRequest(T object, String requestType, AsyncAnswerString asyncAnswer) {
        answerString = asyncAnswer;

        sendRequest(object, requestType, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    serverResponse = logServerResponse(response.body().string());
                    if (serverResponseSuccess.equals(getServerResponseStringFromJSON(serverResponse).getResponseStatus())) {
                        answerString.processFinish(true, serverResponse);
                    } else {
                        answerString.processFinish(false, null);
                    }
                } catch (IOException e) {
                    answerString.processFinish(false, null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                answerString.processFinish(false, null);
            }
        });
    }


//    /**
//     * ServerResponse<String>
//     * Get AsyncAnswer with True(SUCCESS) or False(ERROR) and (AsyncAnswerServerResponseString asyncAnswer <= body response) for this Request;
//     */
//    public void sendRequest(T object, String requestType, AsyncAnswerServerResponseString asyncAnswer) {
//        answerServerResponseString = asyncAnswer;
//
//        sendRequest(object, requestType, new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    serverResponse = logServerResponse(response.body().string());
//                    if (!serverResponseError.equals(getServerResponseStringFromJSON(serverResponse).getResponseStatus())) {
//                        answerServerResponseString.processFinish(true, getServerResponseStringFromJSON(serverResponse));
//                    } else {
//                        answerServerResponseString.processFinish(false, null);
//                    }
//                } catch (IOException e) {
//                    answerServerResponseString.processFinish(false, null);
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                answerServerResponseString.processFinish(false, null);
//            }
//        });
//    }


//    /**
//     * ServerResponse
//     * Get AsyncAnswer with True(SUCCESS) or False(ERROR) and (AsyncAnswerServerResponse asyncAnswer <= body response) for this Request;
//     */
//    public void sendRequest(T object, String requestType, final Type typeObjectResponse, AsyncAnswerServerResponse asyncAnswer) {
//        answerServerResponse = asyncAnswer;
//
//        sendRequest(object, requestType, new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    serverResponse = logServerResponse(response.body().string());
//                    if (!serverResponseError.equals(getServerResponseStringFromJSON(serverResponse).getResponseStatus())) {
//                        answerServerResponse.processFinish(true, getServerResponseObjectFromJSON(serverResponse, typeObjectResponse));
//                    } else {
//                        answerServerResponse.processFinish(false, null);
//                    }
//                } catch (IOException e) {
//                    answerServerResponse.processFinish(false, null);
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                answerServerResponse.processFinish(false, null);
//            }
//        });
//    }


    /**
     * //////// Вспомогательные методы ////////
     */

    /**
     * Получение ServerResponse из String JSON
     */
    private ServerResponse<String> getServerResponseStringFromJSON(String stringJSON) {
        Type typeServerResponse = new TypeToken<ServerResponse>() {
        }.getType();
        try {
            return new Gson().fromJson(stringJSON, typeServerResponse);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return new ServerResponse<>(serverResponseError,"");
    }


    /**
     * Логирование данных передачи
     */
    private ServerRequest logServerRequest(ServerRequest serverRequest) {
        Timber.e(">>>>> class:ManagerRetrofit; object:serverRequest : \n" + serverRequest);
        return serverRequest;
    }

    /**
     * Логирование данных приема
     */
    private String logServerResponse(String serverResponse) {
        Timber.e("<<<<< class:ManagerRetrofit; object:serverResponse : \n" + serverResponse);
        return serverResponse;
    }

}
