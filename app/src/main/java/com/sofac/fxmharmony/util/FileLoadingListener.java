package com.sofac.fxmharmony.util;

/**
 * Created by Maxim on 26.07.2017.
 */

public interface FileLoadingListener {

    void onBegin();

    void onSuccess();

    void onFailure(Throwable cause);

    void onEnd();

}
