package ru.shark.home.legomanager.services.dto;

import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.legomanager.enums.UserPartRequestType;

public class UserPartRequestDto extends PageRequest {
    private UserPartRequestType requestType;

    public UserPartRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(UserPartRequestType requestType) {
        this.requestType = requestType;
    }
}
