package ru.shark.home.legomanager.dao.dto.request;

import ru.shark.home.legomanager.enums.UserPartRequestType;

public class UserPartListRequest {
    private UserPartRequestType requestType;
    private Long userId;

    public UserPartListRequest(UserPartRequestType requestType, Long userId) {
        this.requestType = requestType;
        this.userId = userId;
    }

    public UserPartRequestType getRequestType() {
        return requestType;
    }

    public Long getUserId() {
        return userId;
    }
}
