package org.fourthperson.domain.entity;

public record AppResponse(int status, Object data) {

    public static AppResponse create(int status, Object data) {
        return new AppResponse(status, data);
    }

    public static AppResponse success(Object data) {
        return new AppResponse(200, data);
    }

    public static AppResponse error(Object data) {
        return new AppResponse(500, data);
    }
}
