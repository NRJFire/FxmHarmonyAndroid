package com.sofac.fxmharmony.data.dto.base;




public class ServerRequest <T>{

    public ServerRequest(){}

    public ServerRequest(String requestType, T dataTransferObject) {
        this.requestType = requestType;
        this.dataTransferObject = dataTransferObject;
    }

    private String requestType;
    private T dataTransferObject;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public T getDataTransferObject() {
        return dataTransferObject;
    }

    public void setDataTransferObject(T dataTransferObject) {
        this.dataTransferObject = dataTransferObject;
    }

    @Override
    public String toString() {
        return "ServerRequest{" +
                "requestType='" + requestType + '\'' +
                ", dataTransferObject=" + dataTransferObject +
                '}';
    }
}
