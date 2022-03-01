package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.BaseDto;

public class UserSetDto extends BaseDto {
    private UserDto user;
    private SetDto set;
    private Integer count;

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public SetDto getSet() {
        return set;
    }

    public void setSet(SetDto set) {
        this.set = set;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
