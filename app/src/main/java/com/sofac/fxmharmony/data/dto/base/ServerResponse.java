package com.sofac.fxmharmony.data.dto.base;



public class ServerResponse <T> {

    public ServerResponse(){}

    public ServerResponse(String responseStatus, T dataTransferObject) {
        this.responseStatus = responseStatus;
        this.dataTransferObject = dataTransferObject;
    }

    private String responseStatus;
    private T dataTransferObject;

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public T getDataTransferObject() {
        return dataTransferObject;
    }

    public void setDataTransferObject(T dataTransferObject) {
        this.dataTransferObject = dataTransferObject;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "responseStatus='" + responseStatus + '\'' +
                ", dataTransferObject=" + dataTransferObject +
                '}';
    }
}
