package org.fourthperson.domain.entity;

public class AppResponse {
    public final int status;
    public final Object data;

    public AppResponse(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public static AppResponse create(int status, Object data) {
        return new AppResponse(status, data);
    }
}
