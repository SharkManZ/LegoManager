package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.BaseDto;

public class UserPartDto extends BaseDto {
    private UserDto user;
    private PartColorDto partColor;
    private Integer count;

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public PartColorDto getPartColor() {
        return partColor;
    }

    public void setPartColor(PartColorDto partColor) {
        this.partColor = partColor;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
